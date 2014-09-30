package org.openmrs.module.amrsreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.module.amrsreports.reporting.ICAPCommonIndicatorLibrary;
import org.openmrs.module.amrsreports.reporting.ReportUtils;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CCCPatientCohortDefinition;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.BaseSQLCohortLibrary;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.artCareFollowup.ArtCareSQLCohortLibrary;
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
public class ARTCareFollowUpProvider extends ReportProvider {

    private BaseSQLCohortLibrary baseSQLCohortLibrary = new BaseSQLCohortLibrary();
    private ArtCareSQLCohortLibrary sqlQueries = new ArtCareSQLCohortLibrary();

	public ARTCareFollowUpProvider() {
		this.name = "ART Care Follow-Up";
		this.visible = true;
	}

	@Override
	public ReportDefinition getReportDefinition() {


        ReportDefinition report = new PeriodIndicatorReportDefinition();
        report.setName("ART Care Follow Up");

        // set up parameters
        Parameter facility = new Parameter();
        facility.setName("locationList");
        facility.setType(Location.class);

        //define general cohorts

        CohortDefinition malesZeroTo14Cohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14,sqlQueries.malesWhoStoppedArtBetweenDatesQry());
        CohortDefinition malesAbove15Cohort = baseSQLCohortLibrary.compositionMinAgeCohort(15, sqlQueries.malesWhoStoppedArtBetweenDatesQry());
        CohortDefinition femalesZeroTo14Cohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14,sqlQueries.femalesWhoStoppedArtBetweenDatesQry());
        CohortDefinition femalesAbove15Cohort = baseSQLCohortLibrary.compositionMinAgeCohort(15, sqlQueries.femalesWhoStoppedArtBetweenDatesQry());
        CohortDefinition pedsMalesZeroTo1Cohort = baseSQLCohortLibrary.compositionMaxAgeCohort(1, sqlQueries.malesWhoStoppedArtBetweenDatesQry());
        CohortDefinition pedsFemalesZeroTo1Cohort = baseSQLCohortLibrary.compositionMaxAgeCohort(1, sqlQueries.femalesWhoStoppedArtBetweenDatesQry());
        CohortDefinition pedsmales2To4Cohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.malesWhoStoppedArtBetweenDatesQry());
        CohortDefinition pedsFemales2To4Cohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.femalesWhoStoppedArtBetweenDatesQry());
        CohortDefinition pedsmales5To14Cohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.malesWhoStoppedArtBetweenDatesQry());
        CohortDefinition pedsFemales5To14Cohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.femalesWhoStoppedArtBetweenDatesQry());

        //add cohorts for transfer out
        CohortDefinition malesZeroTo14TCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14, sqlQueries.malesWhoTOBetweenDatesQry());
        CohortDefinition malesAbove15TCohort = baseSQLCohortLibrary.compositionMinAgeCohort(15,sqlQueries.malesWhoTOBetweenDatesQry());
        CohortDefinition femalesZeroTo14TCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14, sqlQueries.femalesWhoTOBetweenDatesQry());
        CohortDefinition femalesAbove15TCohort = baseSQLCohortLibrary.compositionMinAgeCohort(15,sqlQueries.femalesWhoTOBetweenDatesQry());
        CohortDefinition pedsMalesZeroTo1TCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(1, sqlQueries.malesWhoTOBetweenDatesQry());
        CohortDefinition pedsFemalesZeroTo1TCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(1, sqlQueries.femalesWhoTOBetweenDatesQry());
        CohortDefinition pedsmales2To4TCohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.malesWhoTOBetweenDatesQry());
        CohortDefinition pedsFemales2To4TCohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.femalesWhoTOBetweenDatesQry());
        CohortDefinition pedsmales5To14TCohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.malesWhoTOBetweenDatesQry());
        CohortDefinition pedsFemales5To14TCohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.femalesWhoTOBetweenDatesQry());

        //Define cohort for the dead
        CohortDefinition malesZeroTo14DCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14, sqlQueries.malesWhoDEADBetweenDatesQry());
        CohortDefinition malesAbove15DCohort = baseSQLCohortLibrary.compositionMinAgeCohort(15,sqlQueries.malesWhoDEADBetweenDatesQry());
        CohortDefinition femalesZeroTo14DCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14, sqlQueries.femalesWhoDEADBetweenDatesQry());
        CohortDefinition femalesAbove15DCohort = baseSQLCohortLibrary.compositionMinAgeCohort(15, sqlQueries.femalesWhoDEADBetweenDatesQry());
        CohortDefinition pedsMalesZeroTo1DCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(1, sqlQueries.malesWhoDEADBetweenDatesQry());
        CohortDefinition pedsFemalesZeroTo1DCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(1, sqlQueries.femalesWhoDEADBetweenDatesQry());
        CohortDefinition pedsmales2To4DCohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.malesWhoDEADBetweenDatesQry());
        CohortDefinition pedsFemales2To4DCohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.femalesWhoDEADBetweenDatesQry());
        CohortDefinition pedsmales5To14DCohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.malesWhoDEADBetweenDatesQry());
        CohortDefinition pedsFemales5To14DCohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.femalesWhoDEADBetweenDatesQry());


        CohortIndicator malesZeroTo14ind = ICAPCommonIndicatorLibrary.createCohortIndicator("malesZeroTo14CohortIndicator",ReportUtils.map(malesZeroTo14Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator malesAbove15ind = ICAPCommonIndicatorLibrary.createCohortIndicator("malesAbove15CohortIndicator", ReportUtils.map(malesAbove15Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesZeroTo14ind = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesZeroTo14CohortIndicator", ReportUtils.map(femalesZeroTo14Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesAbove15ind = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesAbove15CohortIndicator", ReportUtils.map(femalesAbove15Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator pedsMalesZeroTo1ind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsMalesZeroTo1CohortIndicator", ReportUtils.map(pedsMalesZeroTo1Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemalesZeroTo1ind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemalesZeroTo1CohortIndicator", ReportUtils.map(pedsFemalesZeroTo1Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales2To4ind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsmales2To4CohortIndicator", ReportUtils.map(pedsmales2To4Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales2To4ind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemales2To4CohortIndicator", ReportUtils.map(pedsFemales2To4Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales5To14ind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsmales5To14CohortIndicator", ReportUtils.map(pedsmales5To14Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales5To14ind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemales5To14CohortIndicator", ReportUtils.map(pedsFemales5To14Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));


        //indicators for transfers
        CohortIndicator malesZeroTo14Tind = ICAPCommonIndicatorLibrary.createCohortIndicator("malesZeroTo14CohortIndicator",ReportUtils.map(malesZeroTo14TCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator malesAbove15Tind = ICAPCommonIndicatorLibrary.createCohortIndicator("malesAbove15CohortIndicator", ReportUtils.map(malesAbove15TCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesZeroTo14Tind = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesZeroTo14CohortIndicator", ReportUtils.map(femalesZeroTo14TCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesAbove15Tind = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesAbove15CohortIndicator", ReportUtils.map(femalesAbove15TCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator pedsMalesZeroTo1Tind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsMalesZeroTo1CohortIndicator", ReportUtils.map(pedsMalesZeroTo1TCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemalesZeroTo1Tind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemalesZeroTo1CohortIndicator", ReportUtils.map(pedsFemalesZeroTo1TCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales2To4Tind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsmales2To4CohortIndicator", ReportUtils.map(pedsmales2To4TCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales2To4Tind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemales2To4CohortIndicator", ReportUtils.map(pedsFemales2To4TCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales5To14Tind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsmales5To14CohortIndicator", ReportUtils.map(pedsmales5To14TCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales5To14Tind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemales5To14CohortIndicator", ReportUtils.map(pedsFemales5To14TCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));


        //indicators for dead
        CohortIndicator malesZeroTo14Dind = ICAPCommonIndicatorLibrary.createCohortIndicator("malesZeroTo14CohortIndicator",ReportUtils.map(malesZeroTo14DCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator malesAbove15Dind = ICAPCommonIndicatorLibrary.createCohortIndicator("malesAbove15CohortIndicator", ReportUtils.map(malesAbove15DCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesZeroTo14Dind = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesZeroTo14CohortIndicator", ReportUtils.map(femalesZeroTo14DCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesAbove15Dind = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesAbove15CohortIndicator", ReportUtils.map(femalesAbove15DCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator pedsMalesZeroTo1Dind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsMalesZeroTo1CohortIndicator", ReportUtils.map(pedsMalesZeroTo1DCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemalesZeroTo1Dind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemalesZeroTo1CohortIndicator", ReportUtils.map(pedsFemalesZeroTo1DCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales2To4Dind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsmales2To4CohortIndicator", ReportUtils.map(pedsmales2To4DCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales2To4Dind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemales2To4CohortIndicator", ReportUtils.map(pedsFemales2To4DCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales5To14Dind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsmales5To14CohortIndicator", ReportUtils.map(pedsmales5To14DCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales5To14Dind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemales5To14CohortIndicator", ReportUtils.map(pedsFemales5To14DCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        Map<String, Object> periodMappings = new HashMap<String, Object>();
        periodMappings.put("startDate", "${startDate}");
        periodMappings.put("endDate", "${endDate}");
        periodMappings.put("locationList", "${locationList}");


        CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
        dsd.addParameter(ReportingConstants.START_DATE_PARAMETER);
        dsd.addParameter(ReportingConstants.END_DATE_PARAMETER);
        dsd.addParameter(facility);

        dsd.addColumn("ms14", "Males Below 15", new Mapped<CohortIndicator>(malesZeroTo14ind, periodMappings), "");
        dsd.addColumn("ms15", "Males 15 or more", new Mapped<CohortIndicator>(malesAbove15ind, periodMappings), "");
        dsd.addColumn("fs14", "Females Below 15", new Mapped<CohortIndicator>(femalesZeroTo14ind, periodMappings), "");
        dsd.addColumn("fs15", "Females 15 or more", new Mapped<CohortIndicator>(femalesAbove15ind, periodMappings), "");

        dsd.addColumn("mps01", "Male Peds up to one year", new Mapped<CohortIndicator>(pedsMalesZeroTo1ind, periodMappings), "");
        dsd.addColumn("mps24", "Males peds between 2 and 4", new Mapped<CohortIndicator>(pedsmales2To4ind, periodMappings), "");
        dsd.addColumn("mps514", "Male Ped btw 5 and 14", new Mapped<CohortIndicator>(pedsmales5To14ind, periodMappings), "");
        dsd.addColumn("fps01", "Female peds at one ", new Mapped<CohortIndicator>(pedsFemalesZeroTo1ind, periodMappings), "");
        dsd.addColumn("fps24", "Female peds  btw 2 and 4", new Mapped<CohortIndicator>(pedsFemales2To4ind, periodMappings), "");
        dsd.addColumn("fps514", "Female peds between 5 and 14", new Mapped<CohortIndicator>(pedsFemales5To14ind, periodMappings), "");

        //add columns for transfer out
        dsd.addColumn("mt14", "Males Below 15", new Mapped<CohortIndicator>(malesZeroTo14Tind, periodMappings), "");
        dsd.addColumn("mt15", "Males 15 or more", new Mapped<CohortIndicator>(malesAbove15Tind, periodMappings), "");
        dsd.addColumn("ft14", "Females Below 15", new Mapped<CohortIndicator>(femalesZeroTo14Tind, periodMappings), "");
        dsd.addColumn("ft15", "Females 15 or more", new Mapped<CohortIndicator>(femalesAbove15Tind, periodMappings), "");

        dsd.addColumn("mpt01", "Male Peds up to one year", new Mapped<CohortIndicator>(pedsMalesZeroTo1Tind, periodMappings), "");
        dsd.addColumn("mpt24", "Males peds between 2 and 4", new Mapped<CohortIndicator>(pedsmales2To4Tind, periodMappings), "");
        dsd.addColumn("mpt514", "Male Ped btw 5 and 14", new Mapped<CohortIndicator>(pedsmales5To14Tind, periodMappings), "");
        dsd.addColumn("fpt01", "Female peds at one ", new Mapped<CohortIndicator>(pedsFemalesZeroTo1Tind, periodMappings), "");
        dsd.addColumn("fpt24", "Female peds  btw 2 and 4", new Mapped<CohortIndicator>(pedsFemales2To4Tind, periodMappings), "");
        dsd.addColumn("fpt514", "Female peds between 5 and 14", new Mapped<CohortIndicator>(pedsFemales5To14Tind, periodMappings), "");

        //Add columns for dead
        dsd.addColumn("md14", "Males Below 15", new Mapped<CohortIndicator>(malesZeroTo14Dind, periodMappings), "");
        dsd.addColumn("md15", "Males 15 or more", new Mapped<CohortIndicator>(malesAbove15Dind, periodMappings), "");
        dsd.addColumn("fd14", "Females Below 15", new Mapped<CohortIndicator>(femalesZeroTo14Dind, periodMappings), "");
        dsd.addColumn("fd15", "Females 15 or more", new Mapped<CohortIndicator>(femalesAbove15Dind, periodMappings), "");

        dsd.addColumn("mpd01", "Male Peds up to one year", new Mapped<CohortIndicator>(pedsMalesZeroTo1Dind, periodMappings), "");
        dsd.addColumn("mpd24", "Males peds between 2 and 4", new Mapped<CohortIndicator>(pedsmales2To4Dind, periodMappings), "");
        dsd.addColumn("mpd514", "Male Ped btw 5 and 14", new Mapped<CohortIndicator>(pedsmales5To14Dind, periodMappings), "");
        dsd.addColumn("fpd01", "Female peds at one ", new Mapped<CohortIndicator>(pedsFemalesZeroTo1Dind, periodMappings), "");
        dsd.addColumn("fpd24", "Female peds  btw 2 and 4", new Mapped<CohortIndicator>(pedsFemales2To4Dind, periodMappings), "");
        dsd.addColumn("fpd514", "Female peds between 5 and 14", new Mapped<CohortIndicator>(pedsFemales5To14Dind, periodMappings), "");

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
		design.setName("ART Care Report Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/ARTCareFollowUpReportTemplate.xls");

		if (is == null)
			throw new APIException("Could not find report template.");

		try {
			resource.setContents(IOUtils.toByteArray(is));
		} catch (IOException ex) {
			throw new APIException("Could not create report design for ART Care Report.", ex);
		}

		IOUtils.closeQuietly(is);
		design.addResource(resource);

		return design;
	}
}