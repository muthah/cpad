package org.openmrs.module.amrsreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.module.amrsreports.reporting.ICAPCommonIndicatorLibrary;
import org.openmrs.module.amrsreports.reporting.ReportUtils;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CCCPatientCohortDefinition;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.ArtCare.ArtCare20SQLCohortLibrary;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.BaseSQLCohortLibrary;
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
public class ARTCareProvider extends ReportProvider {

    private BaseSQLCohortLibrary baseSQLCohortLibrary = new BaseSQLCohortLibrary();
    private ArtCare20SQLCohortLibrary sqlQueries = new ArtCare20SQLCohortLibrary();

	public ARTCareProvider() {
		this.name = "2.0 ART Care";
		this.visible = true;
	}

	@Override
	public ReportDefinition getReportDefinition() {

        ReportDefinition report = new PeriodIndicatorReportDefinition();
        report.setName("2.0 ART Care");

        // set up parameters
        Parameter facility = new Parameter();
        facility.setName("locationList");
        facility.setType(Location.class);

        //define general cohorts

        CohortDefinition malesZeroTo14Cohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14, sqlQueries.malesEnrolledAtStartQry());
        CohortDefinition malesAbove15Cohort = baseSQLCohortLibrary.compositionMinAgeCohort(15, sqlQueries.malesEnrolledAtStartQry());
        CohortDefinition femalesZeroTo14Cohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14, sqlQueries.femalesEnrolledAtStartQry());
        CohortDefinition femalesAbove15Cohort = baseSQLCohortLibrary.compositionMinAgeCohort(15, sqlQueries.femalesEnrolledAtStartQry());
        CohortDefinition pedsMalesZeroTo1Cohort = baseSQLCohortLibrary.compositionAgeCohort(0, 1, sqlQueries.malesEnrolledAtStartQry());
        CohortDefinition pedsFemalesZeroTo1Cohort = baseSQLCohortLibrary.compositionAgeCohort(0, 1, sqlQueries.femalesEnrolledAtStartQry());
        CohortDefinition pedsmales2To4Cohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.malesEnrolledAtStartQry());
        CohortDefinition pedsFemales2To4Cohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.femalesEnrolledAtStartQry());
        CohortDefinition pedsmales5To14Cohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.malesEnrolledAtStartQry());
        CohortDefinition pedsFemales5To14Cohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.femalesEnrolledAtStartQry());

        CohortDefinition malesZeroTo14EndCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14, sqlQueries.malesEnrolledBetweenDatesQry());
        CohortDefinition malesAbove15EndCohort = baseSQLCohortLibrary.compositionMinAgeCohort(15, sqlQueries.malesEnrolledBetweenDatesQry());
        CohortDefinition femalesZeroTo14EndCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14, sqlQueries.femalesEnrolledBetweenDatesQry());
        CohortDefinition femalesAbove15EndCohort = baseSQLCohortLibrary.compositionMinAgeCohort(15, sqlQueries.femalesEnrolledBetweenDatesQry());
        CohortDefinition pedsMalesZeroTo1EndCohort = baseSQLCohortLibrary.compositionAgeCohort(0, 1, sqlQueries.malesEnrolledBetweenDatesQry());
        CohortDefinition pedsFemalesZeroTo1EndCohort = baseSQLCohortLibrary.compositionAgeCohort(0, 1, sqlQueries.femalesEnrolledBetweenDatesQry());
        CohortDefinition pedsmales2To4EndCohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.malesEnrolledBetweenDatesQry());
        CohortDefinition pedsFemales2To4EndCohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.femalesEnrolledBetweenDatesQry());
        CohortDefinition pedsmales5To14EndCohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.malesEnrolledBetweenDatesQry());
        CohortDefinition pedsFemales5To14EndCohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.femalesEnrolledBetweenDatesQry());


        //Cohort Definition for Transfer-in
        CohortDefinition malesAgedBelow15TransfersCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14, sqlQueries.malesTIBetweenDatesQry());
        CohortDefinition malesAbove15TransfersCohort = baseSQLCohortLibrary.compositionMinAgeCohort(15, sqlQueries.malesTIBetweenDatesQry());
        CohortDefinition femalesBelow15TransfersCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14, sqlQueries.femalesTIBetweenDatesQry());
        CohortDefinition femalesAbove15TransfersCohort = baseSQLCohortLibrary.compositionMinAgeCohort(15, sqlQueries.femalesTIBetweenDatesQry());

        CohortDefinition pedsMalesZeroTo1TransfersCohort = baseSQLCohortLibrary.compositionAgeCohort(0, 1, sqlQueries.malesTIBetweenDatesQry());
        CohortDefinition pedsfemalesZeroTo1TransfersCohort = baseSQLCohortLibrary.compositionAgeCohort(0, 1, sqlQueries.femalesTIBetweenDatesQry());
        CohortDefinition pedsmales2To4TransfersCohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.malesTIBetweenDatesQry());
        CohortDefinition pedsfemales2To4TransfersCohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.femalesTIBetweenDatesQry());
        CohortDefinition pedsmales5To14TransfersCohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.malesTIBetweenDatesQry());
        CohortDefinition pedsFemales5To14TransfersCohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.femalesTIBetweenDatesQry());;



        CohortIndicator malesZeroTo14ind = ICAPCommonIndicatorLibrary.createCohortIndicator("malesZeroTo14CohortIndicator",ReportUtils.map(malesZeroTo14Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator malesAbove15ind = ICAPCommonIndicatorLibrary.createCohortIndicator("malesAbove15CohortIndicator", ReportUtils.map(malesAbove15Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesZeroTo14ind = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesZeroTo14CohortIndicator", ReportUtils.map(femalesZeroTo14Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesAbove15ind = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesAbove15CohortIndicator", ReportUtils.map(femalesAbove15Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        /**
         * Add indicators for peds
         */
        CohortIndicator pedsMalesZeroTo1ind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsMalesZeroTo1CohortIndicator", ReportUtils.map(pedsMalesZeroTo1Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemalesZeroTo1ind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemalesZeroTo1CohortIndicator", ReportUtils.map(pedsFemalesZeroTo1Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales2To4ind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsmales2To4CohortIndicator", ReportUtils.map(pedsmales2To4Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales2To4ind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemales2To4CohortIndicator", ReportUtils.map(pedsFemales2To4Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales5To14ind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsmales5To14CohortIndicator", ReportUtils.map(pedsmales5To14Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales5To14ind = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemales5To14CohortIndicator", ReportUtils.map(pedsFemales5To14Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        /**
         * Define indicators for end date
         */

        CohortIndicator malesZeroTo14indend = ICAPCommonIndicatorLibrary.createCohortIndicator("malesZeroTo14CohortIndicatorEnd", ReportUtils.map(malesZeroTo14EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator malesAbove15indend = ICAPCommonIndicatorLibrary.createCohortIndicator("malesAbove15CohortIndicatorEnd", ReportUtils.map(malesAbove15EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesZeroTo14indend = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesZeroTo14CohortIndicatorEnd", ReportUtils.map(femalesZeroTo14EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesAbove15indend = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesAbove15CohortIndicatorEnd", ReportUtils.map(femalesAbove15EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        /**
         * Add indicators for peds
         */
        CohortIndicator pedsMalesZeroTo1indend = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsMalesZeroTo1CohortIndicatorEnd", ReportUtils.map(pedsMalesZeroTo1EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemalesZeroTo1indend = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemalesZeroTo1CohortIndicatorEnd", ReportUtils.map(pedsFemalesZeroTo1EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales2To4indend = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsmales2To4CohortIndicatorEnd", ReportUtils.map(pedsmales2To4EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales2To4indend = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemales2To4CohortIndicatorEnd", ReportUtils.map(pedsFemales2To4EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales5To14indend = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsmales5To14CohortIndicatorEnd", ReportUtils.map(pedsmales5To14EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales5To14indend = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemales5To14CohortIndicatorEnd", ReportUtils.map(pedsFemales5To14EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));


        CohortIndicator malesBelow15TransfersIndi = ICAPCommonIndicatorLibrary.createCohortIndicator("malesBelow15TransfersCohortIndicator",ReportUtils.map(malesAgedBelow15TransfersCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator malesAbove15TransfersCohortIndi = ICAPCommonIndicatorLibrary.createCohortIndicator("malesAbove15TransfersCohortIndicator",ReportUtils.map(malesAbove15TransfersCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator femalesBelow15TransfersCohortIndi = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesBelow15TransfersCohortIndicator",ReportUtils.map(femalesBelow15TransfersCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator femalesAbove15TransfersCohortIndi = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesAbove15TransfersCohortIndicator",ReportUtils.map(femalesAbove15TransfersCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsMalesZeroTo1TransfersCohortIndi = ICAPCommonIndicatorLibrary.createCohortIndicator("malesZeroTo14TransfersCohortIndicator",ReportUtils.map(pedsMalesZeroTo1TransfersCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemalesZeroTo1TransfersCohortIndi = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsMalesZeroTo1TransfersCohortIndicator",ReportUtils.map(pedsfemalesZeroTo1TransfersCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales2To4TransfersCohortIndi = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsmales2To4TransfersCohortIndicator",ReportUtils.map(pedsmales2To4TransfersCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales2To4TransfersCohortIndi = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemales2To4TransfersCohortIndicator",ReportUtils.map(pedsfemales2To4TransfersCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales5To14TransfersCohortIndi = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsmales5To14TransfersCohortIndicator",ReportUtils.map(pedsmales5To14TransfersCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales5To14TransfersCohortIndi = ICAPCommonIndicatorLibrary.createCohortIndicator("pedsFemales5To14TransfersCohortIndicator",ReportUtils.map(pedsFemales5To14TransfersCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));



        Map<String, Object> periodMappings = new HashMap<String, Object>();
        periodMappings.put("startDate", "${startDate}");
        periodMappings.put("endDate", "${endDate}");
        periodMappings.put("locationList", "${locationList}");


        CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
        dsd.addParameter(ReportingConstants.START_DATE_PARAMETER);
        dsd.addParameter(ReportingConstants.END_DATE_PARAMETER);
        dsd.addParameter(facility);

        dsd.addColumn("E14", "Males Below 15", new Mapped<CohortIndicator>(malesZeroTo14ind, periodMappings), "");
        dsd.addColumn("E15", "Males 15 or more", new Mapped<CohortIndicator>(malesAbove15ind, periodMappings), "");
        dsd.addColumn("E16", "Females Below 15", new Mapped<CohortIndicator>(femalesZeroTo14ind, periodMappings), "");
        dsd.addColumn("E17", "Females 15 or more", new Mapped<CohortIndicator>(femalesAbove15ind, periodMappings), "");
        //Make second  column
        dsd.addColumn("I14", "Males Below 15", new Mapped<CohortIndicator>(malesZeroTo14indend, periodMappings), "");
        dsd.addColumn("I15", "Males 15 or more", new Mapped<CohortIndicator>(malesAbove15indend, periodMappings), "");
        dsd.addColumn("I16", "Females Below 15", new Mapped<CohortIndicator>(femalesZeroTo14indend, periodMappings), "");
        dsd.addColumn("I17", "Females 15 or more", new Mapped<CohortIndicator>(femalesAbove15indend, periodMappings), "");
        /**
         * Add columns for peds
         */
        dsd.addColumn("E27", "Male Peds up to one year", new Mapped<CohortIndicator>(pedsMalesZeroTo1ind, periodMappings), "");
        dsd.addColumn("E28", "Males peds between 2 and 4", new Mapped<CohortIndicator>(pedsmales2To4ind, periodMappings), "");
        dsd.addColumn("E29", "Male Ped btw 5 and 14", new Mapped<CohortIndicator>(pedsmales5To14ind, periodMappings), "");
        dsd.addColumn("E30", "Female peds at one ", new Mapped<CohortIndicator>(pedsFemalesZeroTo1ind, periodMappings), "");
        dsd.addColumn("E31", "Female peds  btw 2 and 4", new Mapped<CohortIndicator>(pedsFemales2To4ind, periodMappings), "");
        dsd.addColumn("E32", "Female peds between 5 and 14", new Mapped<CohortIndicator>(pedsFemales5To14ind, periodMappings), "");

        /**
         * Fill second column for peds
         */
        dsd.addColumn("I27", "Male Peds up to one year", new Mapped<CohortIndicator>(pedsMalesZeroTo1indend, periodMappings), "");
        dsd.addColumn("I28", "Males peds between 2 and 4", new Mapped<CohortIndicator>(pedsmales2To4indend, periodMappings), "");
        dsd.addColumn("I29", "Male Ped btw 5 and 14", new Mapped<CohortIndicator>(pedsmales5To14indend, periodMappings), "");
        dsd.addColumn("I30", "Female peds at one ", new Mapped<CohortIndicator>(pedsFemalesZeroTo1indend, periodMappings), "");
        dsd.addColumn("I31", "Female peds  btw 2 and 4", new Mapped<CohortIndicator>(pedsFemales2To4indend, periodMappings), "");
        dsd.addColumn("I32", "Female peds between 5 and 14", new Mapped<CohortIndicator>(pedsFemales5To14indend, periodMappings), "");

        //sample for the other two cols

        dsd.addColumn("N14", "Males Below 15", new Mapped<CohortIndicator>(malesBelow15TransfersIndi, periodMappings), "");
        dsd.addColumn("N15", "Males 15 or more", new Mapped<CohortIndicator>(malesAbove15TransfersCohortIndi, periodMappings), "");
        dsd.addColumn("N16", "Females Below 15", new Mapped<CohortIndicator>(femalesBelow15TransfersCohortIndi, periodMappings), "");
        dsd.addColumn("N17", "Females 15 or more", new Mapped<CohortIndicator>(femalesAbove15TransfersCohortIndi, periodMappings), "");

        dsd.addColumn("N27", "Male Peds up to one year", new Mapped<CohortIndicator>(pedsMalesZeroTo1TransfersCohortIndi, periodMappings), "");
        dsd.addColumn("N28", "Males peds between 2 and 4", new Mapped<CohortIndicator>(pedsmales2To4TransfersCohortIndi, periodMappings), "");
        dsd.addColumn("N29", "Male Ped btw 5 and 14", new Mapped<CohortIndicator>(pedsmales5To14TransfersCohortIndi, periodMappings), "");
        dsd.addColumn("N30", "Female peds at one ", new Mapped<CohortIndicator>(pedsFemalesZeroTo1TransfersCohortIndi, periodMappings), "");
        dsd.addColumn("N31", "Female peds  btw 2 and 4", new Mapped<CohortIndicator>(pedsFemales2To4TransfersCohortIndi, periodMappings), "");
        dsd.addColumn("N32", "Female peds between 5 and 14", new Mapped<CohortIndicator>(pedsFemales5To14TransfersCohortIndi, periodMappings), "");



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
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/ARTCare20ReportTemplate.xls");

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