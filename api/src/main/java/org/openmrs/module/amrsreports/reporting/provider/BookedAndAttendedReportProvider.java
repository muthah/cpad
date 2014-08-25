package org.openmrs.module.amrsreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.module.amrsreports.reporting.converter.DecimalAgeConverter;
import org.openmrs.module.amrsreports.reporting.converter.OnARTOrCareConverter;
import org.openmrs.module.amrsreports.reporting.data.*;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
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
 * Provides mechanisms for rendering the Eligible and not on ARV
 */
public class BookedAndAttendedReportProvider extends ReportProvider {

	public BookedAndAttendedReportProvider() {
		this.name = "Booked and Attended Report";
		this.visible = true;
        this.isIndicator= false;
	}

	@Override
	public ReportDefinition getReportDefinition() {

		String nullString = null;
		ReportDefinition report = new PeriodIndicatorReportDefinition();
		report.setName("Booked and Attended");

        report.addParameter(new Parameter("startDate", "Report Date", Date.class));
        report.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        report.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        Map<String, Object> periodMappings = new HashMap<String, Object>();
        periodMappings.put("startDate", "${startDate}");
        periodMappings.put("endDate", "${endDate}");
        periodMappings.put("locationList", "${locationList}");

		PatientDataSetDefinition dsd = new PatientDataSetDefinition();
		dsd.setName("bookedAndAttended");

        dsd.addParameter(new Parameter("startDate", "Report Date", Date.class));
        dsd.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        dsd.addParameter(new Parameter("locationList", "List of Locations", Location.class));


        dsd.addSortCriteria("id", SortCriteria.SortDirection.ASC);
        dsd.addColumn("id", new ICAPCCCNoDataDefinition(), nullString);
		dsd.addColumn("name", new PreferredNameDataDefinition(), nullString, new ObjectFormatter());
		dsd.addColumn("sex", new GenderDataDefinition(), nullString);

		AgeAtEvaluationDateDataDefinition add = new AgeAtEvaluationDateDataDefinition();
		dsd.addColumn("age", add, nullString, new DecimalAgeConverter(0));

        PatientOnARTDataDefinition art = new PatientOnARTDataDefinition();
        art.addParameter(new Parameter("startDate", "Report Date", Date.class));
        art.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        art.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        dsd.addColumn("patientOn", art,periodMappings, new OnARTOrCareConverter());

        BookedStatusDataDefinition booked = new BookedStatusDataDefinition();
        booked.addParameter(new Parameter("startDate", "Report Date", Date.class));
        booked.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        booked.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        dsd.addColumn("booked", booked,periodMappings);

        AttendedStatusDataDefinition attended = new AttendedStatusDataDefinition();
        attended.addParameter(new Parameter("startDate", "Report Date", Date.class));
        attended.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        attended.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        dsd.addColumn("attended", art,periodMappings);

		report.addDataSetDefinition(dsd, null);

		return report;
	}

	@Override
	public CohortDefinition getCohortDefinition() {

        String attended ="select   e.patient_id  " +
                "  from encounter e  " +
                "  inner join person p  " +
                "  on p.person_id=e.patient_id   " +
                "    where e.voided = 0  " +
                "    and p.voided=0   " +
                "    and e.encounter_datetime = (:startDate)  " +
                "    and e.location_id in ( :locationList ) ";

        String booked ="select  o.person_id  " +
                "  from obs o  " +
                "  inner join person p  " +
                "  on p.person_id=o.person_id   " +
                "    where o.voided = 0  " +
                "    and p.voided=0   " +
                "    and o.concept_id = 5096 and value_datetime between date_add((:startDate),INTERVAL -1 DAY) and date_add((:startDate),INTERVAL 1 DAY)  " +
                "    and o.location_id in ( :locationList ) ";


        String sql = attended + " UNION " + booked;
        CohortDefinition generalCOhort = new SqlCohortDefinition(sql);
        generalCOhort.setName("Booked and Attended");

        generalCOhort.addParameter(new Parameter("startDate", "Report Date", Date.class));
        generalCOhort.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        generalCOhort.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        return generalCOhort;
	}

	@Override
	public ReportDesign getReportDesign() {
		ReportDesign design = new ReportDesign();
		design.setName("Booked and Attended Report Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		Properties props = new Properties();
		props.put("repeatingSections", "sheet:1,row:6,dataset:bookedAndAttended");

		design.setProperties(props);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/BookedAndAttendedReportTemplate.xls");

		if (is == null)
			throw new APIException("Could not find report template for Booked and Attended Report.");

		try {
			resource.setContents(IOUtils.toByteArray(is));
		} catch (IOException ex) {
			throw new APIException("Could not create report design for Booked and Attended Report", ex);
		}

		IOUtils.closeQuietly(is);
		design.addResource(resource);

		return design;
	}
}