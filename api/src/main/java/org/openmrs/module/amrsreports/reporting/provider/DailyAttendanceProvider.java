package org.openmrs.module.amrsreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.module.amrsreports.reporting.AttendanceIndicatorLibrary;
import org.openmrs.module.amrsreports.reporting.PWPIndicatorLibrary;
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

    private AttendanceIndicatorLibrary baseIndicator= new AttendanceIndicatorLibrary();

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


        dsd.addColumn("M-01", "Males Below 15", new Mapped<CohortIndicator>(baseIndicator.malesBelow15NewEnrollment(), periodMappings), "");
        dsd.addColumn("M-02", "Males 15 or more", new Mapped<CohortIndicator>(baseIndicator.malesAbove15NewEnrollment(), periodMappings), "");
        dsd.addColumn("F-01", "Females Below 15", new Mapped<CohortIndicator>(baseIndicator.femalesBelow15NewEnrollment(), periodMappings), "");
        dsd.addColumn("F-02", "Females 15 or more", new Mapped<CohortIndicator>(baseIndicator.femalesAbove15NewEnrollment(), periodMappings), "");

        dsd.addColumn("M-03", "Males Below 15", new Mapped<CohortIndicator>(baseIndicator.malesBelow15WithRevisits(), periodMappings), "");
        dsd.addColumn("M-04", "Males 15 or more", new Mapped<CohortIndicator>(baseIndicator.malesBelow15WithRevisits(), periodMappings), "");
        dsd.addColumn("F-03", "Females Below 15", new Mapped<CohortIndicator>(baseIndicator.femalesBelow15WithRevisits(), periodMappings), "");
        dsd.addColumn("F-04", "Females 15 or more", new Mapped<CohortIndicator>(baseIndicator.femalesBelow15WithRevisits(), periodMappings), "");

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