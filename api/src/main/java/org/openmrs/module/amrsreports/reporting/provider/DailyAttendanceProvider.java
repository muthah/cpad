package org.openmrs.module.amrsreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.module.amrsreports.reporting.*;
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
public class DailyAttendanceProvider extends ReportProvider {

    private MOH731IndicatorLibrary indicatorLibrary = new MOH731IndicatorLibrary();
    private DailySummariesCohortLibrary cohortLibrary = new DailySummariesCohortLibrary();

	public DailyAttendanceProvider() {
		this.name = "Daily Attendance Summaries";
		this.visible = true;
	}

	@Override
	public ReportDefinition getReportDefinition() {

        ReportDefinition report = new PeriodIndicatorReportDefinition();
        report.setName("Daily Attendance Summaries");

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


        dsd.addColumn("M-01", "Males Below 15", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("New Males below 15",cohortLibrary.malesAgedAtMostXNewEnrollments(14)), periodMappings), "");
        dsd.addColumn("M-02", "Males 15 or more", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Males 15 and above",cohortLibrary.malesAgedAtLeastXNewEnrollments(15)), periodMappings), "");
        dsd.addColumn("F-01", "Females Below 15", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Females below 15",cohortLibrary.femalesAgedAtMostXNewEnrollments(14)), periodMappings), "");
        dsd.addColumn("F-02", "Females 15 or more", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Males 15 and above",cohortLibrary.femalesAgedAtLeastXNewEnrollments(15)), periodMappings), "");

        dsd.addColumn("M-03", "Males Below 15", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("New Males below 15",cohortLibrary.malesAgedAtMostXRevisits(14)), periodMappings), "");
        dsd.addColumn("M-04", "Males 15 or more", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Males 15 and above",cohortLibrary.malesAgedAtLeastXRevisits(15)), periodMappings), "");
        dsd.addColumn("F-03", "Females Below 15", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Females below 15",cohortLibrary.femalesAgedAtMostXRevisits(14)), periodMappings), "");
        dsd.addColumn("F-04", "Females 15 or more", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Males 15 and above",cohortLibrary.femalesAgedAtLeastXRevisits(15)), periodMappings), "");


        dsd.addColumn("M-05", "Males Below 15", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("New Males below 15",cohortLibrary.patientsAgedAtMostXOnCare(14)), periodMappings), "");
        dsd.addColumn("M-06", "Males 15 or more", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Males 15 and above",cohortLibrary.patientsAgedAtLeastXOnCare(15)), periodMappings), "");
        dsd.addColumn("F-05", "Females Below 15", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Females below 15",cohortLibrary.patientsAgedAtMostXOnART(14)), periodMappings), "");
        dsd.addColumn("F-06", "Females 15 or more", new Mapped<CohortIndicator>(indicatorLibrary.cohortIndicatorCount("Males 15 and above",cohortLibrary.patientsAgedAtLeastXOnART(15)), periodMappings), "");


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
		design.setName("Daily Attendance Summaries Report Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/DailyAttendanceReportTemplate.xls");

		if (is == null)
			throw new APIException("Could not find report template.");

		try {
			resource.setContents(IOUtils.toByteArray(is));
		} catch (IOException ex) {
			throw new APIException("Could not create report design for Daily Attendance Summaries.", ex);
		}

		IOUtils.closeQuietly(is);
		design.addResource(resource);

		return design;
	}

}