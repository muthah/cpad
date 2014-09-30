package org.openmrs.module.amrsreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.module.amrsreports.reporting.ICAPCommonIndicatorLibrary;
import org.openmrs.module.amrsreports.reporting.ReportUtils;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CCCPatientCohortDefinition;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.ArtCare.ArtCare20SQLCohortLibrary;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.BaseSQLCohortLibrary;
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
 * Provider for adolescent on art and care
 */
public class AdolescentOnCareAndARTCareProvider extends ReportProvider {

    private BaseSQLCohortLibrary baseSQLCohortLibrary = new BaseSQLCohortLibrary();
    private PalliativeCareSQLCohortLibrary sqlQueries = new PalliativeCareSQLCohortLibrary();
    private ArtCare20SQLCohortLibrary artCareSqlQueries = new ArtCare20SQLCohortLibrary();

	public AdolescentOnCareAndARTCareProvider() {
		this.name = "Adolescent on ART and Care";
		this.visible = true;
	}

	@Override
	public ReportDefinition getReportDefinition() {

        ReportDefinition report = new PeriodIndicatorReportDefinition();
        report.setName("Adolescent on Care and ART Care");

        // set up parameters
        Parameter facility = new Parameter();
        facility.setName("locationList");
        facility.setType(Location.class);

        //define general cohorts

        CohortDefinition cummulativeMalesCohort = baseSQLCohortLibrary.compositionAgeCohort(10, 24, sqlQueries.malesEnrolledAtStartQry());
        CohortDefinition cummulativeFemalesCohort = baseSQLCohortLibrary.compositionAgeCohort(10, 24, sqlQueries.femalesEnrolledAtStartQry());

        CohortDefinition malesEnrolledCohort = baseSQLCohortLibrary.compositionAgeCohort(10, 24, sqlQueries.malesEnrolledBetweenDatesQry());
        CohortDefinition femalesEnrolledCohort = baseSQLCohortLibrary.compositionAgeCohort(10, 24, sqlQueries.femalesEnrolledBetweenDatesQry());


        //define columns for art care
        CohortDefinition cummulativeMalesARTCohort = baseSQLCohortLibrary.compositionAgeCohort(10, 24, artCareSqlQueries.malesEnrolledAtStartQry());
        CohortDefinition cummulativeFemalesARTCohort = baseSQLCohortLibrary.compositionAgeCohort(10, 24, artCareSqlQueries.femalesEnrolledAtStartQry());

        CohortDefinition malesEnrolledARTCohort = baseSQLCohortLibrary.compositionAgeCohort(10, 24, artCareSqlQueries.malesEnrolledBetweenDatesQry());
        CohortDefinition femalesEnrolledARTCohort = baseSQLCohortLibrary.compositionAgeCohort(10, 24, artCareSqlQueries.femalesEnrolledBetweenDatesQry());


        CohortDefinition malesTransfersCohort = baseSQLCohortLibrary.compositionAgeCohort(10, 24, artCareSqlQueries.malesTIBetweenDatesQry());
        CohortDefinition femalesTransfersCohort = baseSQLCohortLibrary.compositionAgeCohort(10, 24, artCareSqlQueries.femalesTIBetweenDatesQry());;


        //Indicators for Care
        CohortIndicator cummulativeMalesCohortind = ICAPCommonIndicatorLibrary.createCohortIndicator("cummulativeMalesCohortIndicator", ReportUtils.map(cummulativeMalesCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator cummulativeFemalesCohortind = ICAPCommonIndicatorLibrary.createCohortIndicator("cummulativeFemalesCohortIndicator", ReportUtils.map(cummulativeFemalesCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator malesEnrolledCohortInd = ICAPCommonIndicatorLibrary.createCohortIndicator("malesEnrolledCohortIndicator", ReportUtils.map(malesEnrolledCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator femalesEnrolledCohortInd = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesEnrolledCohortIndicator", ReportUtils.map(femalesEnrolledCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        //indicators for ART
        CohortIndicator malesTransfersCohortInd = ICAPCommonIndicatorLibrary.createCohortIndicator("transferMalesCohortIndicator", ReportUtils.map(malesTransfersCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator femalesTransfersCohortInd = ICAPCommonIndicatorLibrary.createCohortIndicator("transferFemalesCohortIndicator", ReportUtils.map(femalesTransfersCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator cummulativeMalesARTCohortind = ICAPCommonIndicatorLibrary.createCohortIndicator("cummulativeMalesARTCohortIndicator", ReportUtils.map(cummulativeMalesARTCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator cummulativeFemalesARTCohortind = ICAPCommonIndicatorLibrary.createCohortIndicator("cummulativeFemalesARTCohortIndicator", ReportUtils.map(cummulativeFemalesARTCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));

        CohortIndicator malesEnrolledARTCohortInd = ICAPCommonIndicatorLibrary.createCohortIndicator("malesEnrolledARTCohortIndicator", ReportUtils.map(malesEnrolledARTCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        CohortIndicator femalesEnrolledARTCohortInd = ICAPCommonIndicatorLibrary.createCohortIndicator("femalesEnrolledARTCohortIndicator", ReportUtils.map(femalesEnrolledARTCohort,"startDate=${startDate},locationList=${locationList},endDate=${endDate}"));



        Map<String, Object> periodMappings = new HashMap<String, Object>();
        periodMappings.put("startDate", "${startDate}");
        periodMappings.put("endDate", "${endDate}");
        periodMappings.put("locationList", "${locationList}");


        CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
        dsd.addParameter(ReportingConstants.START_DATE_PARAMETER);
        dsd.addParameter(ReportingConstants.END_DATE_PARAMETER);
        dsd.addParameter(facility);

        dsd.addColumn("E15", "Males 15 or more", new Mapped<CohortIndicator>(cummulativeMalesCohortind, periodMappings), "");
        dsd.addColumn("E16", "Females Below 15", new Mapped<CohortIndicator>(cummulativeFemalesCohortind, periodMappings), "");
        //Make second  column

        dsd.addColumn("I15", "Males 15 or more", new Mapped<CohortIndicator>(malesEnrolledCohortInd, periodMappings), "");
        dsd.addColumn("I16", "Females Below 15", new Mapped<CohortIndicator>(femalesEnrolledCohortInd, periodMappings), "");
        /**
         * Add columns for peds
         */
        dsd.addColumn("E27", "Cummulative Males at start ", new Mapped<CohortIndicator>(cummulativeMalesARTCohortind, periodMappings), "");
        dsd.addColumn("E28", "Cummulative Females at start", new Mapped<CohortIndicator>(cummulativeFemalesARTCohortind, periodMappings), "");

        /**
         * new enrollment
         */
        dsd.addColumn("I27", "Males enrolled during the quarter", new Mapped<CohortIndicator>(malesEnrolledARTCohortInd, periodMappings), "");
        dsd.addColumn("I28", "Females enrolled during the quarter", new Mapped<CohortIndicator>(femalesEnrolledARTCohortInd, periodMappings), "");

        //transfers


        dsd.addColumn("N27", "Male TI", new Mapped<CohortIndicator>(malesTransfersCohortInd, periodMappings), "");
        dsd.addColumn("N28", "Female TI", new Mapped<CohortIndicator>(femalesTransfersCohortInd, periodMappings), "");



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
		design.setName("Adolescent on Care and ART Care Report Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/AdolescentOnARTAndCareReportTemplate.xls");

		if (is == null)
			throw new APIException("Could not find report template for adolescent on art and on care.");

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