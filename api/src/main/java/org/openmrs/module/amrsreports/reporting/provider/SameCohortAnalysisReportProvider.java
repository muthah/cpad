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
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatientsCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatientsStartCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.EnrolledInCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.LFTUCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.LFTUStartCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.PatientsWithRecentEncAtStartCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.StartedARTCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.StartedARTCareStartCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.StoppedARTCare12CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.StoppedARTCare24CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.StoppedARTCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferIN12CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferIN24CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferINCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferINStartCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferOUT12CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferOUT24CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferOUTCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferOUTStartCohortDefinition;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.BaseSQLCohortLibrary;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.cohortAnalysis.CohortAnalysisSQLCohortLibrary;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.palliativeCare.PalliativeCareSQLCohortLibrary;
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
public class SameCohortAnalysisReportProvider extends ReportProvider {

    private BaseSQLCohortLibrary baseSQLCohortLibrary = new BaseSQLCohortLibrary();
    private CohortAnalysisSQLCohortLibrary sqlQueries = new CohortAnalysisSQLCohortLibrary();
    private CohortAnalysisIndicatorLibrary indicatorLibrary = new CohortAnalysisIndicatorLibrary();

	public SameCohortAnalysisReportProvider() {
		this.name = "Same Cohorts Analysis";
		this.visible = true;
	}

	@Override
	public ReportDefinition getReportDefinition() {

		ReportDefinition report = new PeriodIndicatorReportDefinition();
		report.setName("Same Cohorts Analysis");

        // set up parameters
        Parameter facility = new Parameter();
        facility.setName("locationList");
        facility.setType(Location.class);

        /*Define cohorts start months from reporting period*/
        CohortDefinition originalCohortAtStart = baseSQLCohortLibrary.createCohortDefinition("Original Cohort At Start", sqlQueries.patientsEnrolledInCare(null));
        CohortDefinition transferInAtStart = baseSQLCohortLibrary.createCohortDefinition("TI Cohort At Start", sqlQueries.patientsWhoTransferredIN(null));
        CohortDefinition transferOutAtStart = baseSQLCohortLibrary.createCohortDefinition("TO Cohort At Start", sqlQueries.patientsWhoTransferredOut(null));
        CohortDefinition originalFirstLineAtStart = baseSQLCohortLibrary.createCohortDefinition("Original First line Cohort At Start", sqlQueries.patientsOnOriginalFirstLineRegimen(null));
        CohortDefinition alternativeFirstLineAtStart = baseSQLCohortLibrary.createCohortDefinition("Alternative First Line Cohort At Start", sqlQueries.patientsOnAlternativeFirstLineRegimen(null));
        CohortDefinition secondLineAtStart =baseSQLCohortLibrary.createCohortDefinition("Second Line Cohort At Start", sqlQueries.patientsOnSecondLineOrHigher(null));
        CohortDefinition stoppedAtStart = baseSQLCohortLibrary.createCohortDefinition("Stopped Cohort At Start", sqlQueries.patientsWhoStoppedArt(null));
        CohortDefinition lostAtStart = baseSQLCohortLibrary.createCohortDefinition("Lost Cohort At Start", sqlQueries.patientsLostToFollowUP(null));
        CohortDefinition diedAtStart = baseSQLCohortLibrary.createCohortDefinition("Dead Cohort At Start", sqlQueries.patientsWhoDied(null));
        CohortDefinition aliveOnARTAtStart = baseSQLCohortLibrary.createCohortDefinition("Currently on ART Cohort At Start", sqlQueries.patientsCurrentlyOnART(null));

        CohortIndicator originalCohortAtStartInd = CommonIndicatorLibrary.createCohortIndicator("originalCohortAtStartInd", ReportUtils.map(originalCohortAtStart, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator transferInAtStartInd = CommonIndicatorLibrary.createCohortIndicator("transferInAtStartInd", ReportUtils.map(transferInAtStart, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator transferOutAtStartInd = CommonIndicatorLibrary.createCohortIndicator("transferOutAtStartInd", ReportUtils.map(transferOutAtStart, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator originalFirstLineAtStartInd = CommonIndicatorLibrary.createCohortIndicator("originalFirstLineAtStartInd", ReportUtils.map(originalFirstLineAtStart, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator alternativeFirstLineAtStartInd = CommonIndicatorLibrary.createCohortIndicator("alternativeFirstLineAtStartInd", ReportUtils.map(alternativeFirstLineAtStart, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator secondLineAtStartInd = CommonIndicatorLibrary.createCohortIndicator("secondLineAtStartInd", ReportUtils.map(secondLineAtStart, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator stoppedAtStartInd = CommonIndicatorLibrary.createCohortIndicator("stoppedAtStartInd", ReportUtils.map(stoppedAtStart, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator lostAtStartInd = CommonIndicatorLibrary.createCohortIndicator("lostAtStartInd", ReportUtils.map(lostAtStart, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator diedAtStartInd = CommonIndicatorLibrary.createCohortIndicator("diedAtStartInd", ReportUtils.map(diedAtStart, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator aliveOnARTAtStartInd = CommonIndicatorLibrary.createCohortIndicator("aliveOnARTAtStartInd", ReportUtils.map(aliveOnARTAtStart, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));


        /*Define cohorts 12 months from reporting period*/
        CohortDefinition originalCohortAt12 = baseSQLCohortLibrary.createCohortDefinition("Original Cohort At 12", sqlQueries.patientsEnrolledInCare(null));
        CohortDefinition transferInAt12 = baseSQLCohortLibrary.createCohortDefinition("TI Cohort At 12", sqlQueries.patientsWhoTransferredIN(12));
        CohortDefinition transferOutAt12 = baseSQLCohortLibrary.createCohortDefinition("TO Cohort At 12", sqlQueries.patientsWhoTransferredOut(12));
        CohortDefinition originalFirstLineAt12 = baseSQLCohortLibrary.createCohortDefinition("Original First line Cohort At 12", sqlQueries.patientsOnOriginalFirstLineRegimen(12));
        CohortDefinition alternativeFirstLineAt12 = baseSQLCohortLibrary.createCohortDefinition("Alternative First Line Cohort At 12", sqlQueries.patientsOnAlternativeFirstLineRegimen(12));
        CohortDefinition secondLineAt12 =baseSQLCohortLibrary.createCohortDefinition("Second Line Cohort At 12", sqlQueries.patientsOnSecondLineOrHigher(12));
        CohortDefinition stoppedAt12 = baseSQLCohortLibrary.createCohortDefinition("Stopped Cohort At 12", sqlQueries.patientsWhoStoppedArt(12));
        CohortDefinition lostAt12 = baseSQLCohortLibrary.createCohortDefinition("Lost Cohort At 12", sqlQueries.patientsLostToFollowUP(12));
        CohortDefinition diedAt12 = baseSQLCohortLibrary.createCohortDefinition("Dead Cohort At 12", sqlQueries.patientsWhoDied(12));
        CohortDefinition aliveOnARTAt12 = baseSQLCohortLibrary.createCohortDefinition("Currently on ART Cohort At 12", sqlQueries.patientsCurrentlyOnART(12));

        CohortIndicator originalCohortAt12Ind = CommonIndicatorLibrary.createCohortIndicator("originalCohortAt12Ind",ReportUtils.map(originalCohortAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator transferInAt12Ind = CommonIndicatorLibrary.createCohortIndicator("transferInAt12Ind", ReportUtils.map(transferInAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator transferOutAt12Ind = CommonIndicatorLibrary.createCohortIndicator("transferOutAt12Ind", ReportUtils.map(transferOutAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator originalFirstLineAt12Ind = CommonIndicatorLibrary.createCohortIndicator("originalFirstLineAt12Ind", ReportUtils.map(originalFirstLineAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator alternativeFirstLineAt12Ind = CommonIndicatorLibrary.createCohortIndicator("alternativeFirstLineAt12Ind",ReportUtils.map(alternativeFirstLineAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator secondLineAt12Ind = CommonIndicatorLibrary.createCohortIndicator("secondLineAt12Ind", ReportUtils.map(secondLineAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator stoppedAt12Ind = CommonIndicatorLibrary.createCohortIndicator("stoppedAt12Ind", ReportUtils.map(stoppedAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator lostAt12Ind = CommonIndicatorLibrary.createCohortIndicator("lostAt12Ind", ReportUtils.map(lostAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator diedAt12Ind = CommonIndicatorLibrary.createCohortIndicator("diedAt12Ind", ReportUtils.map(diedAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator aliveOnARTAt12Ind = CommonIndicatorLibrary.createCohortIndicator("aliveOnARTAt12Ind", ReportUtils.map(aliveOnARTAt12,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));


         /*Define cohorts 24 months from reporting period*/
        CohortDefinition originalCohortAt24 = baseSQLCohortLibrary.createCohortDefinition("Original Cohort At 36",sqlQueries.patientsEnrolledInCare(null));
        CohortDefinition transferInAt24 = baseSQLCohortLibrary.createCohortDefinition("TI Cohort At 24", sqlQueries.patientsWhoTransferredIN(24));
        CohortDefinition transferOutAt24 = baseSQLCohortLibrary.createCohortDefinition("TO Cohort At 24", sqlQueries.patientsWhoTransferredOut(24));
        CohortDefinition originalFirstLineAt24 = baseSQLCohortLibrary.createCohortDefinition("Original First line Cohort At 24", sqlQueries.patientsOnOriginalFirstLineRegimen(24));
        CohortDefinition alternativeFirstLineAt24 = baseSQLCohortLibrary.createCohortDefinition("Alternative First Line Cohort At 24", sqlQueries.patientsOnAlternativeFirstLineRegimen(24));
        CohortDefinition secondLineAt24 =baseSQLCohortLibrary.createCohortDefinition("Second Line Cohort At 24",sqlQueries.patientsOnSecondLineOrHigher(24));
        CohortDefinition stoppedAt24 = baseSQLCohortLibrary.createCohortDefinition("Stopped Cohort At 24", sqlQueries.patientsWhoStoppedArt(24));
        CohortDefinition lostAt24 = baseSQLCohortLibrary.createCohortDefinition("Lost Cohort At 24", sqlQueries.patientsLostToFollowUP(24));
        CohortDefinition diedAt24 = baseSQLCohortLibrary.createCohortDefinition("Dead Cohort At 24", sqlQueries.patientsWhoDied(24));
        CohortDefinition aliveOnARTAt24 = baseSQLCohortLibrary.createCohortDefinition("Currently on ART Cohort At 24", sqlQueries.patientsCurrentlyOnART(24));

        CohortIndicator originalCohortAt24Ind = CommonIndicatorLibrary.createCohortIndicator("originalCohortAt24Ind",ReportUtils.map(originalCohortAt24,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator transferInAt24Ind = CommonIndicatorLibrary.createCohortIndicator("transferInAt24Ind", ReportUtils.map(transferInAt24,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator transferOutAt24Ind = CommonIndicatorLibrary.createCohortIndicator("transferOutAt24Ind", ReportUtils.map(transferOutAt24,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator originalFirstLineAt24Ind = CommonIndicatorLibrary.createCohortIndicator("originalFirstLineAt24Ind", ReportUtils.map(originalFirstLineAt24,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator alternativeFirstLineAt24Ind = CommonIndicatorLibrary.createCohortIndicator("alternativeFirstLineAt24Ind",ReportUtils.map(alternativeFirstLineAt24,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator secondLineAt24Ind = CommonIndicatorLibrary.createCohortIndicator("secondLineAt24Ind", ReportUtils.map(secondLineAt24,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator stoppedAt24Ind = CommonIndicatorLibrary.createCohortIndicator("stoppedAt24Ind", ReportUtils.map(stoppedAt24,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator lostAt24Ind = CommonIndicatorLibrary.createCohortIndicator("lostAt24Ind", ReportUtils.map(lostAt24,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator diedAt24Ind = CommonIndicatorLibrary.createCohortIndicator("diedAt24Ind", ReportUtils.map(diedAt24,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator aliveOnARTAt24Ind = CommonIndicatorLibrary.createCohortIndicator("aliveOnARTAt24Ind", ReportUtils.map(aliveOnARTAt24,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));


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
        dsd.addColumn("firstLineRegimen12", "Patients on original first line regimen", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Original First Line Patients",new StartedARTCareStartCohortDefinition()), periodMappings), "");
        dsd.addColumn("alternativeFirstLineRegimen12", "Patients on alternative first line regimen", new Mapped<CohortIndicator>(alternativeFirstLineAtStartInd, periodMappings), "");
        dsd.addColumn("secondLineRegimen12", "Patients on second line regimen or higher", new Mapped<CohortIndicator>(secondLineAtStartInd, periodMappings), "");
        dsd.addColumn("stopped12", "Patients who stopped ART", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Original Stopped ART",new StoppedARTCareCohortDefinition()), periodMappings), "");
        dsd.addColumn("lost12", "Lost patients", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("original lftu",new LFTUStartCohortDefinition()), periodMappings), "");
        dsd.addColumn("died12", "Dead patients", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Original Dead Patients",new DeadPatientsStartCohortDefinition()), periodMappings), "");
        dsd.addColumn("currentlyOnART12", "Patients who are alive and on ART", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Alive and on ART",new CurrentlyOnARTCohortDefinition()), periodMappings), "");


        dsd.addColumn("originalCohort24", "Original Cohort", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month cohort",new EnrolledInCareCohortDefinition()), periodMappings), "");
        dsd.addColumn("transferIn24", "Patients who transferred In", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month TI", new TransferIN12CohortDefinition()), periodMappings), "");
        dsd.addColumn("transferOut24", "Patients who transferred out", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month TO", new TransferOUT12CohortDefinition()), periodMappings), "");
        dsd.addColumn("firstLineRegimen24", "Patients on original first line regimen", new Mapped<CohortIndicator>(originalFirstLineAt12Ind, periodMappings), "");
        dsd.addColumn("alternativeFirstLineRegimen24", "Patients on alternative first line regimen", new Mapped<CohortIndicator>(alternativeFirstLineAt12Ind, periodMappings), "");
        dsd.addColumn("secondLineRegimen24", "Patients on second line regimen or higher", new Mapped<CohortIndicator>(secondLineAt12Ind, periodMappings), "");
        dsd.addColumn("stopped24", "Patients who stopped ART", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month stopped",new StoppedARTCare12CohortDefinition()), periodMappings), "");
        //dsd.addColumn("lost24", "Lost patients", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month lost",new PatientsWithRecentEncAtStartCohortDefinition()), periodMappings), "");
        dsd.addColumn("died24", "Dead patients", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month dead",new DeadPatients12CohortDefinition()), periodMappings), "");
        dsd.addColumn("currentlyOnART24", "Patients who are alive and on ART", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("12 month alive and on ART",new CurrentlyOnARTCohortDefinition()), periodMappings), "");



        dsd.addColumn("originalCohort36", "Original Cohort", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("24 month cohort",new EnrolledInCareCohortDefinition()), periodMappings), "");
        dsd.addColumn("transferIn36", "Patients who transferred In", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("24 month TI",new TransferIN24CohortDefinition()), periodMappings), "");
        dsd.addColumn("transferOut36", "Patients who transferred out", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("24 month TO",new TransferOUT24CohortDefinition()), periodMappings), "");
        dsd.addColumn("firstLineRegimen36", "Patients on original first line regimen", new Mapped<CohortIndicator>(originalFirstLineAt24Ind, periodMappings), "");
        dsd.addColumn("alternativeFirstLineRegimen36", "Patients on alternative first line regimen", new Mapped<CohortIndicator>(alternativeFirstLineAt24Ind, periodMappings), "");
        dsd.addColumn("secondLineRegimen36", "Patients on second line regimen or higher", new Mapped<CohortIndicator>(secondLineAt24Ind, periodMappings), "");
        dsd.addColumn("stopped36", "Patients who stopped ART", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("24 month stopped",new StoppedARTCare24CohortDefinition()), periodMappings), "");
        //dsd.addColumn("lost36", "Lost patients", new Mapped<CohortIndicator>(lostAt24Ind, periodMappings), "");
        dsd.addColumn("died36", "Dead patients", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("24 month dead",new DeadPatients24CohortDefinition()), periodMappings), "");
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
		design.setName("Same Cohorts Analysis Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/SameCohortAnalysisReportTemplate.xls");

		if (is == null)
			throw new APIException("Could not find report template.");

		try {
			resource.setContents(IOUtils.toByteArray(is));
		} catch (IOException ex) {
			throw new APIException("Could not create report design for Same Cohort Analysis Report Template.", ex);
		}

		IOUtils.closeQuietly(is);
		design.addResource(resource);

		return design;
	}


}