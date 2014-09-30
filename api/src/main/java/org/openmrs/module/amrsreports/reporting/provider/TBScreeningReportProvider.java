package org.openmrs.module.amrsreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.module.amrsreports.reporting.TBScreeningIndicatorLibrary;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CCCPatientCohortDefinition;
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
public class TBScreeningReportProvider extends ReportProvider {

    private TBScreeningIndicatorLibrary baseIndicator = new TBScreeningIndicatorLibrary();

	public TBScreeningReportProvider() {
		this.name = "TB Screening Report";
		this.visible = true;
	}

	@Override
	public ReportDefinition getReportDefinition() {

        ReportDefinition report = new PeriodIndicatorReportDefinition();
        report.setName("TB Screening Report");

        // set up parameters
        Parameter facility = new Parameter();
        facility.setName("locationList");
        facility.setType(Location.class);


        Map<String, Object> periodMappings = new HashMap<String, Object>();
        periodMappings.put("startDate", "${startDate}");
        periodMappings.put("endDate", "${endDate}");
        periodMappings.put("locationList", "${locationList}");


        CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
        dsd.addParameter(ReportingConstants.START_DATE_PARAMETER);
        dsd.addParameter(ReportingConstants.END_DATE_PARAMETER);
        dsd.addParameter(facility);

        dsd.addColumn("11M", "Males 0-1 enrolled in care", new Mapped<CohortIndicator>(baseIndicator.malesZeroToOneEnrolledInCare(), periodMappings), "");
        dsd.addColumn("21M", "Males 0-1 screened for TB", new Mapped<CohortIndicator>(baseIndicator.malesZeroToOneWhoHadTBScreening(), periodMappings), "");
        dsd.addColumn("31M", "Males 0-1 proportion screened for TB", new Mapped<CohortIndicator>(baseIndicator.malesZeroToOneWhoHadTBScreening(), periodMappings), "");
        dsd.addColumn("41M", "Males 0-1 with positive TB results", new Mapped<CohortIndicator>(baseIndicator.malesZeroToOneWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("51M", "Males 0-1 proportion with positive TB results", new Mapped<CohortIndicator>(baseIndicator.malesZeroToOneWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("61M", "Males 0-1 with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.malesZeroToOneWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("71M", "Males 0-1 proportion with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.malesZeroToOneWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("81M", "Males 0-1 started on TB treatment regardless of Screening results", new Mapped<CohortIndicator>(baseIndicator.malesZeroToOneOnTBDrugsRegardlessOfResults(), periodMappings), "");
        dsd.addColumn("91M", "Males 0-1 already TB Treatment enrolled into care", new Mapped<CohortIndicator>(baseIndicator.malesZeroToOneOnTBTreatmentAtEnrollmentToHIVCare(), periodMappings), "");
        dsd.addColumn("101M", "Males 0-1 who started treatment during the quarter", new Mapped<CohortIndicator>(baseIndicator.malesZeroToOneEnrolledB4ButStartedTduringQuarter(), periodMappings), "");
        dsd.addColumn("111M", "Males 0-1 on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.malesZeroToOneEnrolledTBCotrimoxazole(), periodMappings), "");
        dsd.addColumn("121M", "Males 0-1 proportion on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.malesZeroToOneEnrolledTBCotrimoxazole(), periodMappings), "");



        dsd.addColumn("14M", "Males 2-4 enrolled in care", new Mapped<CohortIndicator>(baseIndicator.malesTwoToFourEnrolledInCare(), periodMappings), "");
        dsd.addColumn("24M", "Males 2-4 screened for TB", new Mapped<CohortIndicator>(baseIndicator.malesTwoToFourWhoHadTBScreening(), periodMappings), "");
        dsd.addColumn("34M", "Males 2-4 proportion screened for TB", new Mapped<CohortIndicator>(baseIndicator.malesTwoToFourWhoHadTBScreening(), periodMappings), "");
        dsd.addColumn("44M", "Males 2-4 with positive TB results", new Mapped<CohortIndicator>(baseIndicator.malesTwoToFourWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("54M", "Males 2-4 proportion with positive TB results", new Mapped<CohortIndicator>(baseIndicator.malesTwoToFourWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("64M", "Males 2-4 with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.malesTwoToFourWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("74M", "Males 2-4 proportion with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.malesTwoToFourWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("84M", "Males 2-4 started on TB treatment regardless of Screening results", new Mapped<CohortIndicator>(baseIndicator.malesTwoToFourOnTBDrugsRegardlessOfResults(), periodMappings), "");
        dsd.addColumn("94M", "Males 2-4 already TB Treatment enrolled into care", new Mapped<CohortIndicator>(baseIndicator.malesTwoToFourOnTBTreatmentAtEnrollmentToHIVCare(), periodMappings), "");
        dsd.addColumn("104M", "Males 2-4 who started treatment during the quarter", new Mapped<CohortIndicator>(baseIndicator.malesTwoToFourEnrolledB4ButStartedTduringQuarter(), periodMappings), "");
        dsd.addColumn("114M", "Males 2-4 on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.malesTwoToFourEnrolledTBCotrimoxazole(), periodMappings), "");
        dsd.addColumn("124M", "Males 2-4 proportion on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.malesTwoToFourEnrolledTBCotrimoxazole(), periodMappings), "");


        dsd.addColumn("1144M", "Males 5-14 enrolled in care", new Mapped<CohortIndicator>(baseIndicator.malesFiveToFourteenEnrolledInCare(), periodMappings), "");
        dsd.addColumn("2144M", "Males 5-14 screened for TB", new Mapped<CohortIndicator>(baseIndicator.malesFiveToFourteenEnrolledInCare(), periodMappings), "");
        dsd.addColumn("3144M", "Males 5-14 proportion screened for TB", new Mapped<CohortIndicator>(baseIndicator.malesFiveToFourteenWhoHadTBScreening(), periodMappings), "");
        dsd.addColumn("4144M", "Males 5-14 with positive TB results", new Mapped<CohortIndicator>(baseIndicator.malesFiveToFourteenWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("5144M", "Males 5-14 proportion with positive TB results", new Mapped<CohortIndicator>(baseIndicator.malesFiveToFourteenWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("6144M", "Males 5-14 with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.malesFiveToFourteenWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("7144M", "Males 5-14 proportion with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.malesFiveToFourteenWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("8144M", "Males 5-14 started on TB treatment regardless of Screening results", new Mapped<CohortIndicator>(baseIndicator.malesFiveToFourteenOnTBDrugsRegardlessOfResults(), periodMappings), "");
        dsd.addColumn("9144M", "Males 5-14 already TB Treatment enrolled into care", new Mapped<CohortIndicator>(baseIndicator.malesFiveToFourteenOnTBTreatmentAtEnrollmentToHIVCare(), periodMappings), "");
        dsd.addColumn("10144M", "Males 5-14 who started treatment during the quarter", new Mapped<CohortIndicator>(baseIndicator.malesFiveToFourteenEnrolledB4ButStartedTduringQuarter(), periodMappings), "");
        dsd.addColumn("11144M", "Males 5-14 on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.malesFiveToFourteenEnrolledTBCotrimoxazole(), periodMappings), "");
        dsd.addColumn("12144M", "Males 5-14 proportion on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.malesFiveToFourteenEnrolledTBCotrimoxazole(), periodMappings), "");


        dsd.addColumn("1154M", "Males 15+ enrolled in care", new Mapped<CohortIndicator>(baseIndicator.males15AndAboveEnrolledInCare(), periodMappings), "");
        dsd.addColumn("2154M", "Males 15+ screened for TB", new Mapped<CohortIndicator>(baseIndicator.males15AndAboveEnrolledInCare(), periodMappings), "");
        dsd.addColumn("3154M", "Males 15+ proportion screened for TB", new Mapped<CohortIndicator>(baseIndicator.males15AndAboveWhoHadTBScreening(), periodMappings), "");
        dsd.addColumn("4154M", "Males 15+ with positive TB results", new Mapped<CohortIndicator>(baseIndicator.males15AndAboveWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("5154M", "Males 15+ proportion with positive TB results", new Mapped<CohortIndicator>(baseIndicator.males15AndAboveWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("6154M", "Males 15+ with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.males15AndAboveWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("7154M", "Males 15+ proportion with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.males15AndAboveWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("8154M", "Males 15+ started on TB treatment regardless of Screening results", new Mapped<CohortIndicator>(baseIndicator.males15AndAboveOnTBDrugsRegardlessOfResults(), periodMappings), "");
        dsd.addColumn("9154M", "Males 15+ already TB Treatment enrolled into care", new Mapped<CohortIndicator>(baseIndicator.males15AndAboveOnTBTreatmentAtEnrollmentToHIVCare(), periodMappings), "");
        dsd.addColumn("10154M", "Males 15+ who started treatment during the quarter", new Mapped<CohortIndicator>(baseIndicator.males15AndAboveEnrolledB4ButStartedTduringQuarter(), periodMappings), "");
        dsd.addColumn("11154M", "Males 15+ on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.males15AndAboveEnrolledTBCotrimoxazole(), periodMappings), "");
        dsd.addColumn("12154M", "Males 15+ proportion on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.males15AndAboveEnrolledTBCotrimoxazole(), periodMappings), "");



        /**
         * Add columns for females
         */

        dsd.addColumn("11F", "Females 0-1 enrolled in care", new Mapped<CohortIndicator>(baseIndicator.femalesZeroToOneEnrolledInCare(), periodMappings), "");
        dsd.addColumn("21F", "Females 0-1 screened for TB", new Mapped<CohortIndicator>(baseIndicator.femalesZeroToOneWhoHadTBScreening(), periodMappings), "");
        dsd.addColumn("31F", "Females 0-1 proportion screened for TB", new Mapped<CohortIndicator>(baseIndicator.femalesZeroToOneWhoHadTBScreening(), periodMappings), "");
        dsd.addColumn("41F", "Females 0-1 with positive TB results", new Mapped<CohortIndicator>(baseIndicator.femalesZeroToOneWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("51F", "Females 0-1 proportion with positive TB results", new Mapped<CohortIndicator>(baseIndicator.femalesZeroToOneWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("61F", "Females 0-1 with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.femalesZeroToOneWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("71F", "Females 0-1 proportion with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.femalesZeroToOneWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("81F", "Females 0-1 started on TB treatment regardless of Screening results", new Mapped<CohortIndicator>(baseIndicator.femalesZeroToOneOnTBDrugsRegardlessOfResults(), periodMappings), "");
        dsd.addColumn("91F", "Females 0-1 already TB Treatment enrolled into care", new Mapped<CohortIndicator>(baseIndicator.femalesZeroToOneOnTBTreatmentAtEnrollmentToHIVCare(), periodMappings), "");
        dsd.addColumn("101F", "Females 0-1 who started treatment during the quarter", new Mapped<CohortIndicator>(baseIndicator.femalesZeroToOneEnrolledB4ButStartedTDuringQuarter(), periodMappings), "");
        dsd.addColumn("111F", "Females 0-1 on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.femalesZeroToOneEnrolledTBCotrimoxazole(), periodMappings), "");
        dsd.addColumn("121F", "Females 0-1 proportion on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.femalesZeroToOneEnrolledTBCotrimoxazole(), periodMappings), "");



        dsd.addColumn("14F", "Females 2-4 enrolled in care", new Mapped<CohortIndicator>(baseIndicator.femalesTwoToFourEnrolledInCare(), periodMappings), "");
        dsd.addColumn("24F", "Females 2-4 screened for TB", new Mapped<CohortIndicator>(baseIndicator.femalesTwoToFourWhoHadTBScreening(), periodMappings), "");
        dsd.addColumn("34F", "Females 2-4 proportion screened for TB", new Mapped<CohortIndicator>(baseIndicator.femalesTwoToFourWhoHadTBScreening(), periodMappings), "");
        dsd.addColumn("44F", "Females 2-4 with positive TB results", new Mapped<CohortIndicator>(baseIndicator.femalesTwoToFourWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("54F", "Females 2-4 proportion with positive TB results", new Mapped<CohortIndicator>(baseIndicator.femalesTwoToFourWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("64F", "Females 2-4 with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.femalesTwoToFourWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("74F", "Females 2-4 proportion with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.femalesTwoToFourWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("84F", "Females 2-4 started on TB treatment regardless of Screening results", new Mapped<CohortIndicator>(baseIndicator.femalesTwoToFourOnTBDrugsRegardlessOfResults(), periodMappings), "");
        dsd.addColumn("94F", "Females 2-4 already TB Treatment enrolled into care", new Mapped<CohortIndicator>(baseIndicator.femalesTwoToFourOnTBTreatmentAtEnrollmentToHIVCare(), periodMappings), "");
        dsd.addColumn("104F", "Females 2-4 who started treatment during the quarter", new Mapped<CohortIndicator>(baseIndicator.femalesTwoToFourEnrolledB4ButStartedTDuringQuarter(), periodMappings), "");
        dsd.addColumn("114F", "Females 2-4 on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.femalesTwoToFourEnrolledTBCotrimoxazole(), periodMappings), "");
        dsd.addColumn("124F", "Females 2-4 proportion on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.femalesTwoToFourEnrolledTBCotrimoxazole(), periodMappings), "");


        dsd.addColumn("1144F", "Females 5-14 enrolled in care", new Mapped<CohortIndicator>(baseIndicator.femalesFiveToFourteenEnrolledInCare(), periodMappings), "");
        dsd.addColumn("2144F", "Females 5-14 screened for TB", new Mapped<CohortIndicator>(baseIndicator.femalesFiveToFourteenEnrolledInCare(), periodMappings), "");
        dsd.addColumn("3144F", "Females 5-14 proportion screened for TB", new Mapped<CohortIndicator>(baseIndicator.femalesFiveToFourteenWhoHadTBScreening(), periodMappings), "");
        dsd.addColumn("4144F", "Females 5-14 with positive TB results", new Mapped<CohortIndicator>(baseIndicator.femalesFiveToFourteenWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("5144F", "Females 5-14 proportion with positive TB results", new Mapped<CohortIndicator>(baseIndicator.femalesFiveToFourteenWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("6144F", "Females 5-14 with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.femalesFiveToFourteenWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("7144F", "Females 5-14 proportion with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.femalesFiveToFourteenWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("8144F", "Females 5-14 started on TB treatment regardless of Screening results", new Mapped<CohortIndicator>(baseIndicator.femalesFiveToFourteenOnTBDrugsRegardlessOfResults(), periodMappings), "");
        dsd.addColumn("9144F", "Females 5-14 already TB Treatment enrolled into care", new Mapped<CohortIndicator>(baseIndicator.femalesFiveToFourteenOnTBTreatmentAtEnrollmentToHIVCare(), periodMappings), "");
        dsd.addColumn("10144F", "Females 5-14 who started treatment during the quarter", new Mapped<CohortIndicator>(baseIndicator.femalesFiveToFourteenEnrolledB4ButStartedTDuringQuarter(), periodMappings), "");
        dsd.addColumn("11144F", "Females 5-14 on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.femalesFiveToFourteenEnrolledTBCotrimoxazole(), periodMappings), "");
        dsd.addColumn("12144F", "Females 5-14 proportion on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.femalesFiveToFourteenEnrolledTBCotrimoxazole(), periodMappings), "");


        dsd.addColumn("1154F", "Females 15+ enrolled in care", new Mapped<CohortIndicator>(baseIndicator.females15AndAboveEnrolledInCare(), periodMappings), "");
        dsd.addColumn("2154F", "Females 15+ screened for TB", new Mapped<CohortIndicator>(baseIndicator.females15AndAboveEnrolledInCare(), periodMappings), "");
        dsd.addColumn("3154F", "Females 15+ proportion screened for TB", new Mapped<CohortIndicator>(baseIndicator.females15AndAboveWhoHadTBScreening(), periodMappings), "");
        dsd.addColumn("4154F", "Females 15+ with positive TB results", new Mapped<CohortIndicator>(baseIndicator.females15AndAboveWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("5154F", "Females 15+ proportion with positive TB results", new Mapped<CohortIndicator>(baseIndicator.females15AndAboveWhoHadTBScreeningWithPositiveResults(), periodMappings), "");
        dsd.addColumn("6154F", "Females 15+ with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.females15AndAboveWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("7154F", "Females 15+ proportion with positive results and started on TB Drugs", new Mapped<CohortIndicator>(baseIndicator.females15AndAboveWithPositiveResultsStartedOnTBDrugs(), periodMappings), "");
        dsd.addColumn("8154F", "Females 15+ started on TB treatment regardless of Screening results", new Mapped<CohortIndicator>(baseIndicator.females15AndAboveOnTBDrugsRegardlessOfResults(), periodMappings), "");
        dsd.addColumn("9154F", "Females 15+ already TB Treatment enrolled into care", new Mapped<CohortIndicator>(baseIndicator.females15AndAboveOnTBTreatmentAtEnrollmentToHIVCare(), periodMappings), "");
        dsd.addColumn("10154F", "Females 15+ who started treatment during the quarter", new Mapped<CohortIndicator>(baseIndicator.females15AndAboveEnrolledB4ButStartedTDuringQuarter(), periodMappings), "");
        dsd.addColumn("11154F", "Females 15+ on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.females15AndAboveEnrolledTBCotrimoxazole(), periodMappings), "");
        dsd.addColumn("12154F", "Females 15+ proportion on cotrimoxazole", new Mapped<CohortIndicator>(baseIndicator.females15AndAboveEnrolledTBCotrimoxazole(), periodMappings), "");



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
		design.setName("TB Screening Report Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/TBScreeningReportTemplate.xls");

		if (is == null)
			throw new APIException("Could not find report template for TB Screening.");

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