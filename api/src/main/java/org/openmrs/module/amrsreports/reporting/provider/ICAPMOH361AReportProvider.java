package org.openmrs.module.amrsreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.common.EncounterRepresentation;
import org.openmrs.module.amrsreports.reporting.converter.ARVPatientSnapshotReasonConverter;
import org.openmrs.module.amrsreports.reporting.converter.DateListCustomConverter;
import org.openmrs.module.amrsreports.reporting.converter.DecimalAgeConverter;
import org.openmrs.module.amrsreports.reporting.converter.ICAPEntryPointConverter;
import org.openmrs.module.amrsreports.reporting.converter.ICAPFirstWHOStageConverter;
import org.openmrs.module.amrsreports.reporting.converter.WHOStageAndDateConverter;
import org.openmrs.module.amrsreports.reporting.data.FirstEncounterAtFacilityDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAP2EligibilityForARTDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPAgeAtEnrollmentDateDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPArvStartDateDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPCCCNoDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPCtxStartStopDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPEligibilityForARVDateDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPEnrollmentDateDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPEntryPointDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPFirstWHOStageDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPLTFUTODeadDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPLastAppointmentDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPTbStartStopDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.LastHIVEncounterDataDefinition;
import org.openmrs.module.amrsreports.service.MohCoreService;
import org.openmrs.module.amrsreports.util.MOHReportUtil;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.data.MappedData;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.converter.PropertyConverter;
import org.openmrs.module.reporting.data.person.definition.AgeAtDateOfOtherDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportDesignResource;
import org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.renderer.ExcelTemplateRenderer;
import org.openmrs.util.OpenmrsClassLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Provides mechanisms for rendering the MOH 361A Pre-ART Register
 */
public class ICAPMOH361AReportProvider extends ReportProvider {

	public ICAPMOH361AReportProvider() {
		this.name = "MOH 361A";
		this.visible = true;
        this.isIndicator=false;
	}

	@Override
	public ReportDefinition getReportDefinition() {

		String nullString = null;
		ObjectFormatter nullStringConverter = new ObjectFormatter();
		MohCoreService service = Context.getService(MohCoreService.class);

		ReportDefinition report = new PeriodIndicatorReportDefinition();
		report.setName("MOH 361A Report");

		// set up the DSD
		PatientDataSetDefinition dsd = new PatientDataSetDefinition();
		dsd.setName("allPatients");

        // set up parameters
        Parameter facility = new Parameter();
        facility.setName("locationList");
        facility.setType(Location.class);


        Map<String, Object> periodMappings = new HashMap<String, Object>();
        periodMappings.put("startDate", "${startDate}");
        periodMappings.put("endDate", "${endDate}");
        periodMappings.put("locationList", "${locationList}");

		// add to report and data set definition
		report.addParameter(facility);
        report.addParameter(ReportingConstants.START_DATE_PARAMETER);
        report.addParameter(ReportingConstants.END_DATE_PARAMETER);

        dsd.addParameter(ReportingConstants.START_DATE_PARAMETER);
        dsd.addParameter(ReportingConstants.END_DATE_PARAMETER);
        dsd.addParameter(facility);
/*
		// sort by serial number, then by date
		dsd.addSortCriteria("Transfer Status", SortCriteria.SortDirection.ASC);
		dsd.addSortCriteria("Date Chronic HIV Care Started", SortCriteria.SortDirection.ASC);
		dsd.addSortCriteria("Serial Number", SortCriteria.SortDirection.ASC);

		// set up the columns ...

		// patient id ... until we get this thing working proper
		dsd.addColumn("Person ID", new PersonIdDataDefinition(), nullString);

		// a. serial number
		dsd.addColumn("Serial Number", new SerialNumberDataDefinition(), "facility=${facility}");
  */
		// b. date chronic HIV+ care started
        ICAPEnrollmentDateDataDefinition enrollmentDate = new ICAPEnrollmentDateDataDefinition();
		dsd.addColumn("Date Chronic HIV Care Started",  enrollmentDate, nullString,new DateListCustomConverter("yyyy-MM-dd"));
        dsd.addColumn("Unique Patient Number", new ICAPCCCNoDataDefinition(), nullString);
        dsd.addColumn("Name", new PreferredNameDataDefinition(), nullString);
        dsd.addColumn("Sex", new GenderDataDefinition(), nullString);
        dsd.addColumn("Date of Birth", new BirthdateDataDefinition(), nullString,
                new BirthdateConverter(MOHReportUtil.DATE_FORMAT));
        /*dsd.addColumn("First Encounter Date At Facility", new FirstEncounterAtFacilityDataDefinition(),
                "startDate=${startDate},locationList=${locationList},endDate=${endDate}", new EncounterDatetimeConverter());*/
        dsd.addColumn("First Encounter Date At Facility", new FirstEncounterAtFacilityDataDefinition(),nullString,new DateListCustomConverter("yyyy-MM-dd"));
        //ICAPEligibilityForARVDateDataDefinition
        // q. Date medically eligible for ART
        ICAPEligibilityForARVDateDataDefinition eligibility = new ICAPEligibilityForARVDateDataDefinition();
        dsd.addColumn("Date Medically Eligible for ART", eligibility, nullString, new DateListCustomConverter("yyyy-MM-dd"));

        // s. Date ART started (Transfer to ART register)
        dsd.addColumn("Date ART Started", new ICAPArvStartDateDataDefinition(), nullString,new DateListCustomConverter("yyyy-MM-dd"));
        // e2. Age at Enrollment

        // informative column for the destination clinics
        dsd.addColumn("Last Return to Clinic Date", new ICAPLastAppointmentDataDefinition(), nullString,
                new DateListCustomConverter("yyyy-MM-dd"));

        LastHIVEncounterDataDefinition lastHIVEncounter = new LastHIVEncounterDataDefinition();

        dsd.addColumn("Last HIV Encounter Date", lastHIVEncounter, nullString,
                new PropertyConverter(EncounterRepresentation.class, "encounterDatetime"));

        dsd.addColumn("Last HIV Encounter Location", lastHIVEncounter, nullString,
                new PropertyConverter(EncounterRepresentation.class, "locationName"));

        ICAPAgeAtEnrollmentDateDataDefinition ageAtEnrollmentIcap = new ICAPAgeAtEnrollmentDateDataDefinition();

        MappedData<ICAPAgeAtEnrollmentDateDataDefinition> mappedDef = new MappedData<ICAPAgeAtEnrollmentDateDataDefinition>();
        mappedDef.setParameterizable(ageAtEnrollmentIcap);
        mappedDef.addConverter(new DateConverter());
        AgeAtDateOfOtherDataDefinition ageAtEnrollment = new AgeAtDateOfOtherDataDefinition();
        ageAtEnrollment.setEffectiveDateDefinition(mappedDef);
        dsd.addColumn("Age at Enrollment", ageAtEnrollment, nullString, new DecimalAgeConverter(0));
        dsd.addColumn("Entry Point", new ICAPEntryPointDataDefinition(), nullString, new ICAPEntryPointConverter());

        // h. Confirmed HIV+ Date
        dsd.addColumn("Confirmed HIV Date", enrollmentDate, nullString,new DateListCustomConverter("yyyy-MM-dd"));

        // m. TB treatment startdate and stopdate
        dsd.addColumn("TB Treatment Start Stop Date", new ICAPTbStartStopDataDefinition(), nullString);

        // k. CTX startdate and stopdate:
        dsd.addColumn("CTX Start Stop Date", new ICAPCtxStartStopDataDefinition(), nullString);

        // p. WHO clinical Stage and date
        dsd.addColumn("WHO Clinical Stage", new ICAPFirstWHOStageDataDefinition(), nullString, new ICAPFirstWHOStageConverter());

        // o. LTFU / TO / Dead and date when the event occurred
        dsd.addColumn("LTFU TO DEAD", new ICAPLTFUTODeadDataDefinition(), nullString, nullStringConverter);

        // r. Reason Medically Eligible for ART
        dsd.addColumn("Reason Medically Eligible for ART", new ICAP2EligibilityForARTDataDefinition(), nullString, new ARVPatientSnapshotReasonConverter());


   /*
		// extra column to help understand reason for including in this cohort
		dsd.addColumn("First Encounter Date At Facility", new FirstEncounterAtFacilityDataDefinition(),
				"facility=${facility}", new EncounterDatetimeConverter());

		// c. Unique Patient Number
		PatientIdentifierType pit = service.getCCCNumberIdentifierType();
		PatientIdentifierDataDefinition cccColumn = new PatientIdentifierDataDefinition("CCC", pit);
		cccColumn.setIncludeFirstNonNullOnly(true);
		dsd.addColumn("Unique Patient Number", cccColumn, nullString);

		// AMRS Universal ID
		PatientIdentifierDataDefinition uidColumn = new PatientIdentifierDataDefinition(
				"AMRS Universal ID", Context.getPatientService().getPatientIdentifierType(8));
		uidColumn.setIncludeFirstNonNullOnly(true);
		dsd.addColumn("AMRS Universal ID", uidColumn, nullString);

		// AMRS Medical Record Number
		PatientIdentifierDataDefinition mrnColumn = new PatientIdentifierDataDefinition(
				"AMRS Medical Record Number", Context.getPatientService().getPatientIdentifierType(3));
		mrnColumn.setIncludeFirstNonNullOnly(true);
		dsd.addColumn("AMRS Medical Record Number", mrnColumn, nullString);

		// d. Patient's Name
		dsd.addColumn("Name", new PreferredNameDataDefinition(), nullString);

		// e1. Date of Birth
		dsd.addColumn("Date of Birth", new BirthdateDataDefinition(), nullString,
				new BirthdateConverter(MOHReportUtil.DATE_FORMAT));

		// e2. Age at Enrollment

		MappedData<EnrollmentDateDataDefinition> mappedDef = new MappedData<EnrollmentDateDataDefinition>();
		mappedDef.setParameterizable(enrollmentDate);
		mappedDef.addConverter(new DateConverter());
		AgeAtDateOfOtherDataDefinition ageAtEnrollment = new AgeAtDateOfOtherDataDefinition();
		ageAtEnrollment.setEffectiveDateDefinition(mappedDef);
		dsd.addColumn("Age at Enrollment", ageAtEnrollment, nullString, new DecimalAgeConverter(2));

		// f. Sex
		dsd.addColumn("Sex", new GenderDataDefinition(), nullString);

		// g. Entry point: From where?
		PersonAttributeType pat = Context.getPersonService().getPersonAttributeTypeByName(MohEvaluableNameConstants.POINT_OF_HIV_TESTING);
		dsd.addColumn("Entry Point", new PersonAttributeDataDefinition("entryPoint", pat), nullString, new EntryPointConverter());

		// h. Confirmed HIV+ Date
		dsd.addColumn("Confirmed HIV Date", enrollmentDate, nullString);

		// k. CTX startdate and stopdate:
		dsd.addColumn("CTX Start Stop Date", new CtxStartStopDataDefinition(), nullString);

		// l. Fluconazole startdate and stopdate
		dsd.addColumn("Fluconazole Start Stop Date", new FluconazoleStartStopDataDefinition(), nullString);

		// m. TB treatment startdate and stopdate
		dsd.addColumn("TB Treatment Start Stop Date", new TbStartStopDataDefinition(), nullString);

		// n. Pregnancy Yes?, Due date, PMTCT refer
		dsd.addColumn("Pregnancy EDD and Referral", new PmtctPregnancyDataDefinition(), nullString, new FormattedDateSetConverter());

		// o. LTFU / TO / Dead and date when the event occurred
		dsd.addColumn("LTFU TO DEAD", new LTFUTODeadDataDefinition(), nullString, nullStringConverter);

		// p. WHO clinical Stage and date
		dsd.addColumn("WHO Clinical Stage", new FirstWHOStageDataDefinition(), nullString, new WHOStageAndDateConverter());

		// q. Date medically eligible for ART
		EligibilityForARTDataDefinition eligibility = new EligibilityForARTDataDefinition();
		dsd.addColumn("Date Medically Eligible for ART", eligibility, nullString, new ARVPatientSnapshotDateConverter());

		// r. Reason Medically Eligible for ART
		dsd.addColumn("Reason Medically Eligible for ART", eligibility, nullString, new ARVPatientSnapshotReasonConverter());

		// s. Date ART started (Transfer to ART register)
		dsd.addColumn("Date ART Started", new DateARTStartedDataDefinition(), nullString);

		// additional columns for troubleshooting
		LastHIVEncounterDataDefinition lastHIVEncounter = new LastHIVEncounterDataDefinition();

		dsd.addColumn("Last HIV Encounter Date", lastHIVEncounter, nullString,
				new PropertyConverter(EncounterRepresentation.class, "encounterDatetime"));

		dsd.addColumn("Last HIV Encounter Location", lastHIVEncounter, nullString,
				new PropertyConverter(EncounterRepresentation.class, "locationName"));

		// informative column for the destination clinics
		dsd.addColumn("Last Return to Clinic Date", new LastRTCDateDataDefinition(), nullString,
				new PropertyConverter(ObsRepresentation.class, "valueDatetime"));

		// transfer status column (used for sorting, not needed in output)
		dsd.addColumn("Transfer Status", new TransferStatusDataDefinition(), "facility=${facility}");*/


		report.addDataSetDefinition(dsd, periodMappings);

		return report;
	}

	@Override
	public CohortDefinition getCohortDefinition() {
        String sql ="select obs.person_id from obs " +
                "  inner join person p " +
                "  on p.person_id=obs.person_id  " +
                "  where concept_id in (160555,160534,162227) " +
                "  and location_id in(:locationList) " +
                "  and value_datetime between (:startDate) and (:endDate) ";

        CohortDefinition generalCOhort = new SqlCohortDefinition(sql);
        generalCOhort.setName("MOH 361A Report");

        generalCOhort.addParameter(new Parameter("startDate", "Report Date", Date.class));
        generalCOhort.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        generalCOhort.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        return generalCOhort;
	}

	@Override
	public ReportDesign getReportDesign() {
		ReportDesign design = new ReportDesign();
		design.setName("MOH 361A Register Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		Properties props = new Properties();
		props.put("repeatingSections", "sheet:2,row:4,dataset:allPatients");

		design.setProperties(props);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/MOH361AReportTemplate_0_2.xls");

		if (is == null)
			throw new APIException("Could not find report template.");

		try {
			resource.setContents(IOUtils.toByteArray(is));
		} catch (IOException ex) {
			throw new APIException("Could not create report design for MOH 361A Register.", ex);
		}

		IOUtils.closeQuietly(is);
		design.addResource(resource);

		return design;
	}
}
