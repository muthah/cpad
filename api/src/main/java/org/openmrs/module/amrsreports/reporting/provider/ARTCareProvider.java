package org.openmrs.module.amrsreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.module.amrsreports.reporting.CommonCohortLibrary;
import org.openmrs.module.amrsreports.reporting.CommonICAPCohortLibrary;
import org.openmrs.module.amrsreports.reporting.CommonIndicatorLibrary;
import org.openmrs.module.amrsreports.reporting.ReportUtils;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CCCPatientCohortDefinition;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
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

/**
 * Provides mechanisms for rendering the MOH 361A Pre-ART Register
 */
public class ARTCareProvider extends ReportProvider {


    private CommonICAPCohortLibrary commonCohorts = new CommonICAPCohortLibrary();

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

        CohortDefinition malesZeroTo14Cohort = commonCohorts.malesAgedAtMostAtFacility(14);
        CohortDefinition malesAbove15Cohort = commonCohorts.malesAgedAtLeastAtFacility(15);
        CohortDefinition femalesZeroTo14Cohort = commonCohorts.femalesAgedAtMostAtFacility(14);
        CohortDefinition femalesAbove15Cohort = commonCohorts.femalesAgedAtLeastAtFacility(15);
        CohortDefinition pedsMalesZeroTo1Cohort = commonCohorts.malesAgedAtMostAtFacility(1);
        CohortDefinition pedsFemalesZeroTo1Cohort = commonCohorts.femalesAgedAtMostAtFacility(1);
        CohortDefinition pedsmales2To4Cohort = commonCohorts.malesAgedBetweenAtFacility(2,4);
        CohortDefinition pedsFemales2To4Cohort = commonCohorts.femalesAgedBetweenAtFacility(2,4);
        CohortDefinition pedsmales5To14Cohort = commonCohorts.malesAgedBetweenAtFacility(5,14);
        CohortDefinition pedsFemales5To14Cohort = commonCohorts.femalesAgedBetweenAtFacility(5,14);

        CohortDefinitionDimension compositionDimension = new CohortDefinitionDimension();
        compositionDimension.setName("compositionDimension");
        compositionDimension.addParameter(new Parameter("startDate", "Start Date", Date.class));
        compositionDimension.addParameter(new Parameter("endDate", "End Date", Date.class));
        compositionDimension.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        compositionDimension.addCohortDefinition("malesZeroTo14CohortDimension", ReportUtils.map(malesZeroTo14Cohort, "effectiveDate=${startDate},locationList=${locationList},onOrBefore=${startDate}"));
        compositionDimension.addCohortDefinition("malesAbove15CohortDimension",  ReportUtils.map(malesAbove15Cohort, "effectiveDate=${startDate},locationList=${locationList},onOrBefore=${startDate}"));
        compositionDimension.addCohortDefinition("femalesZeroTo14CohortDimension", ReportUtils.map(femalesZeroTo14Cohort, "effectiveDate=${startDate},locationList=${locationList},onOrBefore=${startDate}"));
        compositionDimension.addCohortDefinition("femalesAbove15CohortDimension", ReportUtils.map(femalesAbove15Cohort, "effectiveDate=${startDate},locationList=${locationList},onOrBefore=${startDate}"));

        //add cohort dimension for peds
        compositionDimension.addCohortDefinition("pedsMalesZeroTo1CohortDimension", ReportUtils.map(pedsMalesZeroTo1Cohort, "effectiveDate=${startDate},locationList=${locationList},onOrBefore=${startDate}"));
        compositionDimension.addCohortDefinition("pedsFemalesZeroTo1CohortDimension", ReportUtils.map(pedsFemalesZeroTo1Cohort, "effectiveDate=${startDate},locationList=${locationList},onOrBefore=${startDate}"));
        compositionDimension.addCohortDefinition("pedsmales2To4CohortDimension", ReportUtils.map(pedsmales2To4Cohort, "effectiveDate=${startDate},locationList=${locationList},onOrBefore=${startDate}"));
        compositionDimension.addCohortDefinition("pedsFemales2To4CohortDimension", ReportUtils.map(pedsFemales2To4Cohort, "effectiveDate=${startDate},locationList=${locationList},onOrBefore=${startDate}"));
        compositionDimension.addCohortDefinition("pedsmales5To14CohortDimension", ReportUtils.map(pedsmales5To14Cohort, "effectiveDate=${startDate},locationList=${locationList},onOrBefore=${startDate}"));
        compositionDimension.addCohortDefinition("pedsFemales5To14CohortDimension", ReportUtils.map(pedsFemales5To14Cohort, "effectiveDate=${startDate},locationList=${locationList},onOrBefore=${startDate}"));

        CohortIndicator malesZeroTo14ind = CommonIndicatorLibrary.createCohortIndicatorAtStart("malesZeroTo14CohortIndicator", malesZeroTo14Cohort);

        CohortIndicator malesAbove15ind = CommonIndicatorLibrary.createCohortIndicatorAtStart("malesAbove15CohortIndicator", malesAbove15Cohort);

        CohortIndicator femalesZeroTo14ind = CommonIndicatorLibrary.createCohortIndicatorAtStart("femalesZeroTo14CohortIndicator", femalesZeroTo14Cohort);

        CohortIndicator femalesAbove15ind = CommonIndicatorLibrary.createCohortIndicatorAtStart("femalesAbove15CohortIndicator", femalesAbove15Cohort);

        /**
         * Add indicators for peds
         */
        CohortIndicator pedsMalesZeroTo1ind = CommonIndicatorLibrary.createCohortIndicatorAtStart("pedsMalesZeroTo1CohortIndicator", pedsMalesZeroTo1Cohort);
        CohortIndicator pedsFemalesZeroTo1ind = CommonIndicatorLibrary.createCohortIndicatorAtStart("pedsFemalesZeroTo1CohortIndicator", pedsFemalesZeroTo1Cohort);
        CohortIndicator pedsmales2To4ind = CommonIndicatorLibrary.createCohortIndicatorAtStart("pedsmales2To4CohortIndicator", pedsmales2To4Cohort);
        CohortIndicator pedsFemales2To4ind = CommonIndicatorLibrary.createCohortIndicatorAtStart("pedsFemales2To4CohortIndicator", pedsFemales2To4Cohort);
        CohortIndicator pedsmales5To14ind = CommonIndicatorLibrary.createCohortIndicatorAtStart("pedsmales5To14CohortIndicator", pedsmales5To14Cohort);
        CohortIndicator pedsFemales5To14ind = CommonIndicatorLibrary.createCohortIndicatorAtStart("pedsFemales5To14CohortIndicator", pedsFemales5To14Cohort);

        /**
         * Define indicators for end date
         */

        CohortIndicator malesZeroTo14indend = CommonIndicatorLibrary.createCohortIndicatorAtEnd("malesZeroTo14CohortIndicatorEnd", malesZeroTo14Cohort);

        CohortIndicator malesAbove15indend = CommonIndicatorLibrary.createCohortIndicatorAtEnd("malesAbove15CohortIndicatorEnd", malesAbove15Cohort);

        CohortIndicator femalesZeroTo14indend = CommonIndicatorLibrary.createCohortIndicatorAtEnd("femalesZeroTo14CohortIndicatorEnd", femalesZeroTo14Cohort);

        CohortIndicator femalesAbove15indend = CommonIndicatorLibrary.createCohortIndicatorAtEnd("femalesAbove15CohortIndicatorEnd", femalesAbove15Cohort);

        /**
         * Add indicators for peds
         */
        CohortIndicator pedsMalesZeroTo1indend = CommonIndicatorLibrary.createCohortIndicatorAtEnd("pedsMalesZeroTo1CohortIndicatorEnd", pedsMalesZeroTo1Cohort);
        CohortIndicator pedsFemalesZeroTo1indend = CommonIndicatorLibrary.createCohortIndicatorAtEnd("pedsFemalesZeroTo1CohortIndicatorEnd", pedsFemalesZeroTo1Cohort);
        CohortIndicator pedsmales2To4indend = CommonIndicatorLibrary.createCohortIndicatorAtEnd("pedsmales2To4CohortIndicatorEnd", pedsmales2To4Cohort);
        CohortIndicator pedsFemales2To4indend = CommonIndicatorLibrary.createCohortIndicatorAtEnd("pedsFemales2To4CohortIndicatorEnd", pedsFemales2To4Cohort);
        CohortIndicator pedsmales5To14indend = CommonIndicatorLibrary.createCohortIndicatorAtEnd("pedsmales5To14CohortIndicatorEnd", pedsmales5To14Cohort);
        CohortIndicator pedsFemales5To14indend = CommonIndicatorLibrary.createCohortIndicatorAtEnd("pedsFemales5To14CohortIndicatorEnd", pedsFemales5To14Cohort);


        Map<String, Object> dimensionMappings = new HashMap<String, Object>();
        dimensionMappings.put("startDate", "${startDate}");
        dimensionMappings.put("locationList", "${locationList}");

        Map<String, Object> periodMappings = new HashMap<String, Object>();
        periodMappings.put("startDate", "${startDate}");
        periodMappings.put("endDate", "${endDate}");
        periodMappings.put("locationList", "${locationList}");

        Map<String, Object> secondColumnMappings = new HashMap<String, Object>();
        periodMappings.put("startDate", "${endDate}");
        periodMappings.put("endDate", "${endDate}");
        periodMappings.put("locationList", "${locationList}");

        CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
        dsd.addParameter(ReportingConstants.START_DATE_PARAMETER);
        dsd.addParameter(ReportingConstants.END_DATE_PARAMETER);
        dsd.addParameter(facility);

        dsd.addDimension("compositionDimension", new Mapped<CohortDefinitionDimension>(compositionDimension,dimensionMappings));
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
        dsd.addColumn("L14", "Males Below 15", new Mapped<CohortIndicator>(malesZeroTo14indend, periodMappings), "");
        dsd.addColumn("L15", "Males 15 or more", new Mapped<CohortIndicator>(malesAbove15indend, periodMappings), "");
        dsd.addColumn("L16", "Females Below 15", new Mapped<CohortIndicator>(femalesZeroTo14indend, periodMappings), "");
        dsd.addColumn("L17", "Females 15 or more", new Mapped<CohortIndicator>(femalesAbove15indend, periodMappings), "");

        dsd.addColumn("L27", "Male Peds up to one year", new Mapped<CohortIndicator>(pedsMalesZeroTo1indend, periodMappings), "");
        dsd.addColumn("L28", "Males peds between 2 and 4", new Mapped<CohortIndicator>(pedsmales2To4indend, periodMappings), "");
        dsd.addColumn("L29", "Male Ped btw 5 and 14", new Mapped<CohortIndicator>(pedsmales5To14indend, periodMappings), "");
        dsd.addColumn("L30", "Female peds at one ", new Mapped<CohortIndicator>(pedsFemalesZeroTo1indend, periodMappings), "");
        dsd.addColumn("L31", "Female peds  btw 2 and 4", new Mapped<CohortIndicator>(pedsFemales2To4indend, periodMappings), "");
        dsd.addColumn("L32", "Female peds between 5 and 14", new Mapped<CohortIndicator>(pedsFemales5To14indend, periodMappings), "");


        dsd.addColumn("N14", "Males Below 15", new Mapped<CohortIndicator>(malesZeroTo14indend, periodMappings), "");
        dsd.addColumn("N15", "Males 15 or more", new Mapped<CohortIndicator>(malesAbove15indend, periodMappings), "");
        dsd.addColumn("N16", "Females Below 15", new Mapped<CohortIndicator>(femalesZeroTo14indend, periodMappings), "");
        dsd.addColumn("N17", "Females 15 or more", new Mapped<CohortIndicator>(femalesAbove15indend, periodMappings), "");

        dsd.addColumn("N27", "Male Peds up to one year", new Mapped<CohortIndicator>(pedsMalesZeroTo1indend, periodMappings), "");
        dsd.addColumn("N28", "Males peds between 2 and 4", new Mapped<CohortIndicator>(pedsmales2To4indend, periodMappings), "");
        dsd.addColumn("N29", "Male Ped btw 5 and 14", new Mapped<CohortIndicator>(pedsmales5To14indend, periodMappings), "");
        dsd.addColumn("N30", "Female peds at one ", new Mapped<CohortIndicator>(pedsFemalesZeroTo1indend, periodMappings), "");
        dsd.addColumn("N31", "Female peds  btw 2 and 4", new Mapped<CohortIndicator>(pedsFemales2To4indend, periodMappings), "");
        dsd.addColumn("N32", "Female peds between 5 and 14", new Mapped<CohortIndicator>(pedsFemales5To14indend, periodMappings), "");




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

    public static CohortIndicator createCohortIndicator(String description, Mapped<CohortDefinition> mappedCohort) {
        CohortIndicator ind = new CohortIndicator(description);
        ind.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ind.addParameter(new Parameter("endDate", "End Date", Date.class));
        ind.setType(CohortIndicator.IndicatorType.COUNT);
        ind.setCohortDefinition(mappedCohort);
        return ind;
    }
}