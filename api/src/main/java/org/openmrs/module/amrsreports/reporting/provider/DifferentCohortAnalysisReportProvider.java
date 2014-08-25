package org.openmrs.module.amrsreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.module.amrsreports.reporting.CohortAnalysisIndicatorLibrary;
import org.openmrs.module.amrsreports.reporting.CommonIndicatorLibrary;
import org.openmrs.module.amrsreports.reporting.ReportUtils;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CCCPatientCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CurrentlyOnARTCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatients12CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatients24CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatients36CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatientsStartCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.EnrolledInCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.StoppedARTCare12CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.StoppedARTCare24CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.StoppedARTCare36CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.StoppedARTCareStartCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferIN12CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferIN24CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferIN36CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferINStartCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferOUT12CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferOUT24CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferOUT36CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferOUTStartCohortDefinition;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.BaseSQLCohortLibrary;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.cohortAnalysis.CohortAnalysisSQLCohortLibrary;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportDesignResource;
import org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.renderer.ExcelTemplateRenderer;
import org.openmrs.util.OpenmrsClassLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides mechanisms for rendering the MOH 361A Pre-ART Register
 */
public class DifferentCohortAnalysisReportProvider extends ReportProvider {

    private BaseSQLCohortLibrary baseSQLCohortLibrary = new BaseSQLCohortLibrary();
    private CohortAnalysisSQLCohortLibrary sqlQueries = new CohortAnalysisSQLCohortLibrary();
    private CohortAnalysisIndicatorLibrary indicatorLibrary = new CohortAnalysisIndicatorLibrary();

	public DifferentCohortAnalysisReportProvider() {
		this.name = "Different Cohorts Analysis";
		this.visible = true;
	}

	@Override
	public ReportDefinition getReportDefinition() {

		ReportDefinition report = new PeriodIndicatorReportDefinition();
		report.setName("Different Cohorts Analysis");

        // set up parameters
        Parameter facility = new Parameter();
        facility.setName("locationList");
        facility.setType(Location.class);

        /*Define cohorts 12 months from reporting period*/
        CohortDefinition originalFirstLineAtStart = baseSQLCohortLibrary.createCohortDefinition("Original First line Cohort At Start",sqlQueries.patientsOnOriginalFirstLineRegimen(12));
        CohortDefinition alternativeFirstLineAtStart = baseSQLCohortLibrary.createCohortDefinition("Alternative First Line Cohort At Start",sqlQueries.patientsOnAlternativeFirstLineRegimen(12));
        CohortDefinition secondLineAtStart =baseSQLCohortLibrary.createCohortDefinition("Second Line Cohort At Start",sqlQueries.patientsOnSecondLineOrHigher(12));

        CohortIndicator originalFirstLineAtStartInd = CommonIndicatorLibrary.createCohortIndicator("originalFirstLineAtStartInd", ReportUtils.map(originalFirstLineAtStart,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator alternativeFirstLineAtStartInd = CommonIndicatorLibrary.createCohortIndicator("alternativeFirstLineAtStartInd",ReportUtils.map(alternativeFirstLineAtStart,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator secondLineAtStartInd = CommonIndicatorLibrary.createCohortIndicator("secondLineAtStartInd", ReportUtils.map(secondLineAtStart,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));


        /*Define cohorts 24 months from reporting period*/
        CohortDefinition originalFirstLineAt12 = baseSQLCohortLibrary.createCohortDefinition("Original First line Cohort At 12",sqlQueries.patientsOnOriginalFirstLineRegimen(24));
        CohortDefinition alternativeFirstLineAt12 = baseSQLCohortLibrary.createCohortDefinition("Alternative First Line Cohort At 12",sqlQueries.patientsOnAlternativeFirstLineRegimen(24));
        CohortDefinition secondLineAt12 =baseSQLCohortLibrary.createCohortDefinition("Second Line Cohort At 12",sqlQueries.patientsOnSecondLineOrHigher(24));

        CohortIndicator originalFirstLineAt12Ind = CommonIndicatorLibrary.createCohortIndicator("originalFirstLineAt12Ind", ReportUtils.map(originalFirstLineAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator alternativeFirstLineAt12Ind = CommonIndicatorLibrary.createCohortIndicator("alternativeFirstLineAt12Ind",ReportUtils.map(alternativeFirstLineAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator secondLineAt12Ind = CommonIndicatorLibrary.createCohortIndicator("secondLineAt12Ind", ReportUtils.map(secondLineAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));


         /*Define cohorts 36 months from reporting period*/
        CohortDefinition originalFirstLineAt36 = baseSQLCohortLibrary.createCohortDefinition("Original First line Cohort At 36",sqlQueries.patientsOnOriginalFirstLineRegimen(36));
        CohortDefinition alternativeFirstLineAt36 = baseSQLCohortLibrary.createCohortDefinition("Alternative First Line Cohort At 36",sqlQueries.patientsOnAlternativeFirstLineRegimen(36));
        CohortDefinition secondLineAt36 =baseSQLCohortLibrary.createCohortDefinition("Second Line Cohort At 36",sqlQueries.patientsOnSecondLineOrHigher(36));

        CohortIndicator originalFirstLineAt36Ind = CommonIndicatorLibrary.createCohortIndicator("originalFirstLineAt36Ind", ReportUtils.map(originalFirstLineAt36,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator alternativeFirstLineAt36Ind = CommonIndicatorLibrary.createCohortIndicator("alternativeFirstLineAt36Ind",ReportUtils.map(alternativeFirstLineAt36,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator secondLineAt36Ind = CommonIndicatorLibrary.createCohortIndicator("secondLineAt36Ind", ReportUtils.map(secondLineAt36,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));


        Map<String, Object> periodMappings = new HashMap<String, Object>();
        periodMappings.put("startDate", "${startDate}");
        periodMappings.put("endDate", "${endDate}");
        periodMappings.put("locationList", "${locationList}");


        CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
        dsd.addParameter(ReportingConstants.START_DATE_PARAMETER);
        dsd.addParameter(ReportingConstants.END_DATE_PARAMETER);
        dsd.addParameter(facility);


        dsd.addColumn("originalCohort12", "Original Cohort", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Original Cohort",new EnrolledInCareCohortDefinition()), periodMappings), "");
        dsd.addColumn("transferIn12", "Patients who transferred In", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Original Transfer In",new TransferINStartCohortDefinition()), periodMappings), "");
        dsd.addColumn("transferOut12", "Patients who transferred out", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Original Transfer Out",new TransferOUTStartCohortDefinition()), periodMappings), "");
        dsd.addColumn("firstLineRegimen12", "Patients on original first line regimen", new Mapped<CohortIndicator>(originalFirstLineAtStartInd, periodMappings), "");
        dsd.addColumn("alternativeFirstLineRegimen12", "Patients on alternative first line regimen", new Mapped<CohortIndicator>(alternativeFirstLineAtStartInd, periodMappings), "");
        dsd.addColumn("secondLineRegimen12", "Patients on second line regimen or higher", new Mapped<CohortIndicator>(secondLineAtStartInd, periodMappings), "");
        dsd.addColumn("stopped12", "Patients who stopped ART", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Original Stopped ART",new StoppedARTCareStartCohortDefinition()), periodMappings), "");
        //dsd.addColumn("lost12", "Lost patients", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("original lftu",new LFTUCohortDefinition()), periodMappings), "");
        dsd.addColumn("died12", "Dead patients", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Original Dead Patients",new DeadPatientsStartCohortDefinition()), periodMappings), "");
        dsd.addColumn("currentlyOnART12", "Patients who are alive and on ART", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Alive and on ART",new CurrentlyOnARTCohortDefinition()), periodMappings), "");


        dsd.addColumn("originalCohort24", "Original Cohort", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month cohort",new EnrolledInCareCohortDefinition()), periodMappings), "");
        dsd.addColumn("transferIn24", "Patients who transferred In", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month TI", new TransferIN24CohortDefinition()), periodMappings), "");
        dsd.addColumn("transferOut24", "Patients who transferred out", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month TO", new TransferOUT24CohortDefinition()), periodMappings), "");
        dsd.addColumn("firstLineRegimen24", "Patients on original first line regimen", new Mapped<CohortIndicator>(originalFirstLineAt12Ind, periodMappings), "");
        dsd.addColumn("alternativeFirstLineRegimen24", "Patients on alternative first line regimen", new Mapped<CohortIndicator>(alternativeFirstLineAt12Ind, periodMappings), "");
        dsd.addColumn("secondLineRegimen24", "Patients on second line regimen or higher", new Mapped<CohortIndicator>(secondLineAt12Ind, periodMappings), "");
        dsd.addColumn("stopped24", "Patients who stopped ART", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month stopped",new StoppedARTCare24CohortDefinition()), periodMappings), "");
        //dsd.addColumn("lost24", "Lost patients", new Mapped<CohortIndicator>(lostAt12Ind, periodMappings), "");
        dsd.addColumn("died24", "Dead patients", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month dead",new DeadPatients24CohortDefinition()), periodMappings), "");
        dsd.addColumn("currentlyOnART24", "Patients who are alive and on ART", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month alive and on ART",new CurrentlyOnARTCohortDefinition()), periodMappings), "");



        dsd.addColumn("originalCohort36", "Original Cohort", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("24 month cohort",new EnrolledInCareCohortDefinition()), periodMappings), "");
        dsd.addColumn("transferIn36", "Patients who transferred In", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("24 month TI",new TransferIN36CohortDefinition()), periodMappings), "");
        dsd.addColumn("transferOut36", "Patients who transferred out", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("24 month TO",new TransferOUT36CohortDefinition()), periodMappings), "");
        dsd.addColumn("firstLineRegimen36", "Patients on original first line regimen", new Mapped<CohortIndicator>(originalFirstLineAt36Ind, periodMappings), "");
        dsd.addColumn("alternativeFirstLineRegimen36", "Patients on alternative first line regimen", new Mapped<CohortIndicator>(alternativeFirstLineAt36Ind, periodMappings), "");
        dsd.addColumn("secondLineRegimen36", "Patients on second line regimen or higher", new Mapped<CohortIndicator>(secondLineAt36Ind, periodMappings), "");
        dsd.addColumn("stopped36", "Patients who stopped ART", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("24 month stopped",new StoppedARTCare36CohortDefinition()), periodMappings), "");
        //dsd.addColumn("lost36", "Lost patients", new Mapped<CohortIndicator>(lostAt24Ind, periodMappings), "");
        dsd.addColumn("died36", "Dead patients", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("24 month dead",new DeadPatients36CohortDefinition()), periodMappings), "");
        dsd.addColumn("currentlyOnART36", "Patients who are alive and on ART", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("24 month alive and on art",new CurrentlyOnARTCohortDefinition()), periodMappings), "");
        report.addDataSetDefinition(dsd, periodMappings);

		return report;
	}

	@Override
	public CohortDefinition getCohortDefinition() {
		return new CCCPatientCohortDefinition();
	}

	@Override
	public ReportDesign getReportDesign() {
		ReportDesign design = new ReportDesign();
		design.setName("Different Cohorts Analysis Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/DifferentCohortAnalysisReportTemplate.xls");

		if (is == null)
			throw new APIException("Could not find report template.");

		try {
			resource.setContents(IOUtils.toByteArray(is));
		} catch (IOException ex) {
			throw new APIException("Could not create report design for Different Cohort Analysis Report Template.", ex);
		}

		IOUtils.closeQuietly(is);
		design.addResource(resource);

		return design;
	}


}