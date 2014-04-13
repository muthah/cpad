package org.openmrs.module.amrsreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.module.amrsreports.MOHFacility;
import org.openmrs.module.amrsreports.cache.MohCacheUtils;
import org.openmrs.module.amrsreports.reporting.CommonCohortLibrary;
import org.openmrs.module.amrsreports.reporting.CommonICAPCohortLibrary;
import org.openmrs.module.amrsreports.reporting.CommonIndicatorLibrary;
import org.openmrs.module.amrsreports.reporting.ReportUtils;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CCCPatientCohortDefinition;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.BaseSQLCohortLibrary;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.artCareFollowup.ArtCareSQLCohortLibrary;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.palliativeCare.PalliativeCareSQLCohortLibrary;
import org.openmrs.module.amrsreports.rule.MohEvaluableNameConstants;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides mechanisms for rendering the MOH 361A Pre-ART Register
 */
public class HIVPalliativeCareProvider extends ReportProvider {

    private BaseSQLCohortLibrary baseSQLCohortLibrary = new BaseSQLCohortLibrary();
    private PalliativeCareSQLCohortLibrary sqlQueries = new PalliativeCareSQLCohortLibrary();

	public HIVPalliativeCareProvider() {
		this.name = "1.0 HIV Palliative Care";
		this.visible = true;
	}

	@Override
	public ReportDefinition getReportDefinition() {

		ReportDefinition report = new PeriodIndicatorReportDefinition();
		report.setName("1.0 HIV Palliative Care");

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


        //Cohorts for drugs

        CohortDefinition malesAgedBelow15OnMedCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14, sqlQueries.malesOnCotrimozaleBetweenDatesQry());
        CohortDefinition malesAbove15OnMedCohort = baseSQLCohortLibrary.compositionMinAgeCohort(15, sqlQueries.malesOnCotrimozaleBetweenDatesQry());
        CohortDefinition femalesBelow15onMedCohort = baseSQLCohortLibrary.compositionMaxAgeCohort(14, sqlQueries.femalesOnCotrimozaleBetweenDatesQry());
        CohortDefinition femalesAbove15onMedCohort = baseSQLCohortLibrary.compositionMinAgeCohort(15, sqlQueries.femalesOnCotrimozaleBetweenDatesQry());

        CohortDefinition pedsMalesZeroTo1onMedCohort = baseSQLCohortLibrary.compositionAgeCohort(0, 1, sqlQueries.malesOnCotrimozaleBetweenDatesQry());
        CohortDefinition pedsfemalesZeroTo1onMedCohort = baseSQLCohortLibrary.compositionAgeCohort(0, 1, sqlQueries.femalesOnCotrimozaleBetweenDatesQry());
        CohortDefinition pedsmales2To4onMedCohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.malesOnCotrimozaleBetweenDatesQry());
        CohortDefinition pedsfemales2To4onMedCohort = baseSQLCohortLibrary.compositionAgeCohort(2, 4, sqlQueries.femalesOnCotrimozaleBetweenDatesQry());
        CohortDefinition pedsmales5To14onMedCohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.malesOnCotrimozaleBetweenDatesQry());
        CohortDefinition pedsFemales5To14onMedCohort = baseSQLCohortLibrary.compositionAgeCohort(5, 14, sqlQueries.femalesOnCotrimozaleBetweenDatesQry());;

        CohortIndicator malesZeroTo14ind = CommonIndicatorLibrary.createCohortIndicator("malesZeroTo14CohortIndicator",ReportUtils.map(malesZeroTo14Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator malesAbove15ind = CommonIndicatorLibrary.createCohortIndicator("malesAbove15CohortIndicator", ReportUtils.map(malesAbove15Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesZeroTo14ind = CommonIndicatorLibrary.createCohortIndicator("femalesZeroTo14CohortIndicator", ReportUtils.map(femalesZeroTo14Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesAbove15ind = CommonIndicatorLibrary.createCohortIndicator("femalesAbove15CohortIndicator", ReportUtils.map(femalesAbove15Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        /**
         * Add indicators for peds
         */
        CohortIndicator pedsMalesZeroTo1ind = CommonIndicatorLibrary.createCohortIndicator("pedsMalesZeroTo1CohortIndicator", ReportUtils.map(pedsMalesZeroTo1Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemalesZeroTo1ind = CommonIndicatorLibrary.createCohortIndicator("pedsFemalesZeroTo1CohortIndicator", ReportUtils.map(pedsFemalesZeroTo1Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales2To4ind = CommonIndicatorLibrary.createCohortIndicator("pedsmales2To4CohortIndicator", ReportUtils.map(pedsmales2To4Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales2To4ind = CommonIndicatorLibrary.createCohortIndicator("pedsFemales2To4CohortIndicator", ReportUtils.map(pedsFemales2To4Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales5To14ind = CommonIndicatorLibrary.createCohortIndicator("pedsmales5To14CohortIndicator", ReportUtils.map(pedsmales5To14Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales5To14ind = CommonIndicatorLibrary.createCohortIndicator("pedsFemales5To14CohortIndicator", ReportUtils.map(pedsFemales5To14Cohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        /**
         * Define indicators for end date
         */

        CohortIndicator malesZeroTo14indend = CommonIndicatorLibrary.createCohortIndicator("malesZeroTo14CohortIndicatorEnd", ReportUtils.map(malesZeroTo14EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator malesAbove15indend = CommonIndicatorLibrary.createCohortIndicator("malesAbove15CohortIndicatorEnd", ReportUtils.map(malesAbove15EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesZeroTo14indend = CommonIndicatorLibrary.createCohortIndicator("femalesZeroTo14CohortIndicatorEnd", ReportUtils.map(femalesZeroTo14EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator femalesAbove15indend = CommonIndicatorLibrary.createCohortIndicator("femalesAbove15CohortIndicatorEnd", ReportUtils.map(femalesAbove15EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        /**
         * Add indicators for peds
         */
        CohortIndicator pedsMalesZeroTo1indend = CommonIndicatorLibrary.createCohortIndicator("pedsMalesZeroTo1CohortIndicatorEnd", ReportUtils.map(pedsMalesZeroTo1EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemalesZeroTo1indend = CommonIndicatorLibrary.createCohortIndicator("pedsFemalesZeroTo1CohortIndicatorEnd", ReportUtils.map(pedsFemalesZeroTo1EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales2To4indend = CommonIndicatorLibrary.createCohortIndicator("pedsmales2To4CohortIndicatorEnd", ReportUtils.map(pedsmales2To4EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales2To4indend = CommonIndicatorLibrary.createCohortIndicator("pedsFemales2To4CohortIndicatorEnd", ReportUtils.map(pedsFemales2To4EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales5To14indend = CommonIndicatorLibrary.createCohortIndicator("pedsmales5To14CohortIndicatorEnd", ReportUtils.map(pedsmales5To14EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales5To14indend = CommonIndicatorLibrary.createCohortIndicator("pedsFemales5To14CohortIndicatorEnd", ReportUtils.map(pedsFemales5To14EndCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator malesBelow15OnMedCohortIndi = CommonIndicatorLibrary.createCohortIndicator("malesBelow15OnMedCohortIndicator",ReportUtils.map(malesAgedBelow15OnMedCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator malesAbove15OnMedCohortIndi = CommonIndicatorLibrary.createCohortIndicator("malesAbove15OnMedCohortIndicator",ReportUtils.map(malesAbove15OnMedCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator femalesBelow15OnMedCohortIndi = CommonIndicatorLibrary.createCohortIndicator("femalesBelow15OnMedCohortIndicator",ReportUtils.map(femalesBelow15onMedCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator femalesAbove15OnMedCohortIndi = CommonIndicatorLibrary.createCohortIndicator("femalesAbove15OnMedCohortIndicator",ReportUtils.map(femalesAbove15onMedCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsMalesZeroTo1OnMedCohortIndi = CommonIndicatorLibrary.createCohortIndicator("malesZeroTo14CohortIndicator",ReportUtils.map(pedsMalesZeroTo1onMedCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemalesZeroTo1OnMedCohortIndi = CommonIndicatorLibrary.createCohortIndicator("pedsMalesZeroTo1OnMedCohortIndicator",ReportUtils.map(pedsfemalesZeroTo1onMedCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales2To4OnMedCohortIndi = CommonIndicatorLibrary.createCohortIndicator("pedsmales2To4OnMedCohortIndicator",ReportUtils.map(pedsmales2To4onMedCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales2To4OnMedCohortIndi = CommonIndicatorLibrary.createCohortIndicator("pedsFemales2To4OnMedCohortIndicator",ReportUtils.map(pedsfemales2To4onMedCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsmales5To14OnMedCohortIndi = CommonIndicatorLibrary.createCohortIndicator("pedsmales5To14CohortIndicator",ReportUtils.map(pedsmales5To14onMedCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator pedsFemales5To14OnMedCohortIndi = CommonIndicatorLibrary.createCohortIndicator("pedsFemales5To14CohortIndicator",ReportUtils.map(pedsFemales5To14onMedCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));



        Map<String, Object> periodMappings = new HashMap<String, Object>();
        periodMappings.put("startDate", "${startDate}");
        periodMappings.put("endDate", "${endDate}");
        periodMappings.put("locationList", "${locationList}");


        CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
        dsd.addParameter(ReportingConstants.START_DATE_PARAMETER);
        dsd.addParameter(ReportingConstants.END_DATE_PARAMETER);
        dsd.addParameter(facility);


        dsd.addColumn("malesBelow15", "Males Below 15", new Mapped<CohortIndicator>(malesZeroTo14ind, periodMappings), "");
        dsd.addColumn("malesAbove15", "Males 15 or more", new Mapped<CohortIndicator>(malesAbove15ind, periodMappings), "");
        dsd.addColumn("femalesBelow15", "Females Below 15", new Mapped<CohortIndicator>(femalesZeroTo14ind, periodMappings), "");
        dsd.addColumn("femalesAbove15", "Females 15 or more", new Mapped<CohortIndicator>(femalesAbove15ind, periodMappings), "");
        //Make second  column
        dsd.addColumn("NEWmalesBelow15", "Males Below 15", new Mapped<CohortIndicator>(malesZeroTo14indend, periodMappings), "");
        dsd.addColumn("NEWmalesAbove15", "Males 15 or more", new Mapped<CohortIndicator>(malesAbove15indend, periodMappings), "");
        dsd.addColumn("NEWfemalesBelow15", "Females Below 15", new Mapped<CohortIndicator>(femalesZeroTo14indend, periodMappings), "");
        dsd.addColumn("NEWfemalesAbove15", "Females 15 or more", new Mapped<CohortIndicator>(femalesAbove15indend, periodMappings), "");
        /**
         * Add columns for peds
         */
        dsd.addColumn("malesPedsAt1", "Male Peds up to one year", new Mapped<CohortIndicator>(pedsMalesZeroTo1ind, periodMappings), "");
        dsd.addColumn("malesPedsBtw2n4", "Males peds between 2 and 4", new Mapped<CohortIndicator>(pedsmales2To4ind, periodMappings), "");
        dsd.addColumn("malesPedsBtw5n14", "Male Ped btw 5 and 14", new Mapped<CohortIndicator>(pedsmales5To14ind, periodMappings), "");
        dsd.addColumn("femalesPedsAt1", "Female peds at one ", new Mapped<CohortIndicator>(pedsFemalesZeroTo1ind, periodMappings), "");
        dsd.addColumn("femalesPedsBtw2n4", "Female peds  btw 2 and 4", new Mapped<CohortIndicator>(pedsFemales2To4ind, periodMappings), "");
        dsd.addColumn("femalesPedsBtw5n14", "Female peds between 5 and 14", new Mapped<CohortIndicator>(pedsFemales5To14ind, periodMappings), "");

        /**
         * Fill second column for peds
         */
        dsd.addColumn("NEWmalesPedsAt1", "Male Peds up to one year", new Mapped<CohortIndicator>(pedsMalesZeroTo1indend, periodMappings), "");
        dsd.addColumn("NEWmalesPedsBtw2n4", "Males peds between 2 and 4", new Mapped<CohortIndicator>(pedsmales2To4indend, periodMappings), "");
        dsd.addColumn("NEWmalesPedsBtw5n14", "Male Ped btw 5 and 14", new Mapped<CohortIndicator>(pedsmales5To14indend, periodMappings), "");
        dsd.addColumn("NEWfemalesPedsAt1", "Female peds at one ", new Mapped<CohortIndicator>(pedsFemalesZeroTo1indend, periodMappings), "");
        dsd.addColumn("NEWfemalesPedsBtw2n4", "Female peds  btw 2 and 4", new Mapped<CohortIndicator>(pedsFemales2To4indend, periodMappings), "");
        dsd.addColumn("NEWfemalesPedsBtw5n14", "Female peds between 5 and 14", new Mapped<CohortIndicator>(pedsFemales5To14indend, periodMappings), "");


        //add columns for medication
        dsd.addColumn("malesBelow15onMedication", "Males below 15 on Medication", new Mapped<CohortIndicator>(malesBelow15OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("malesAbove15onMedication", "Males 15+ on Medication", new Mapped<CohortIndicator>(malesAbove15OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("femalesBelow15onMedication", "FeMales 15+ on Medication", new Mapped<CohortIndicator>(femalesBelow15OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("femalesAbove15onMedication", "FeMales below 15 on Medication", new Mapped<CohortIndicator>(femalesAbove15OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("malesPedsAt1onMedication", "peds below 2 on Medication", new Mapped<CohortIndicator>(pedsMalesZeroTo1OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("femalesPedsAt1onMedication", "Males 15+ on Medication", new Mapped<CohortIndicator>(pedsFemalesZeroTo1OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("malesPedsBtw2n4onMedication", "Males 15+ on Medication", new Mapped<CohortIndicator>(pedsmales2To4OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("femalesPedsBtw2n4onMedication", "Males 15+ on Medication", new Mapped<CohortIndicator>(pedsFemales2To4OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("malesPedsBtw5n14onMedication", "Males 15+ on Medication", new Mapped<CohortIndicator>(pedsmales5To14OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("femalesPedsBtw5n14onMedication", "Males 15+ on Medication", new Mapped<CohortIndicator>(pedsFemales5To14OnMedCohortIndi, periodMappings), "");

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
		design.setName("Hiv Palliative Care Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/Quarterly_Facility-BasedHIVCare-template.xls");

		if (is == null)
			throw new APIException("Could not find report template.");

		try {
			resource.setContents(IOUtils.toByteArray(is));
		} catch (IOException ex) {
			throw new APIException("Could not create report design for MOH 361B Register.", ex);
		}

		IOUtils.closeQuietly(is);
		design.addResource(resource);

		return design;
	}


}