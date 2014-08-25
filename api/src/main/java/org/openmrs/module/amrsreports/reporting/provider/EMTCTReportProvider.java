package org.openmrs.module.amrsreports.reporting.provider;

import org.apache.commons.io.IOUtils;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.module.amrsreports.reporting.converter.*;
import org.openmrs.module.amrsreports.reporting.data.*;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
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
import java.util.Properties;

/**
 * Provides mechanisms for rendering the Eligible and not on ARV
 */
public class EMTCTReportProvider extends ReportProvider {

	public EMTCTReportProvider() {
		this.name = "EMTCT Report";
		this.visible = true;
        this.isIndicator= false;
	}

	@Override
	public ReportDefinition getReportDefinition() {

		String nullString = null;
		ReportDefinition report = new PeriodIndicatorReportDefinition();
		report.setName("EMTCT");

        report.addParameter(new Parameter("startDate", "Report Date", Date.class));
        report.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        report.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        Map<String, Object> periodMappings = new HashMap<String, Object>();
        periodMappings.put("startDate", "${startDate}");
        periodMappings.put("endDate", "${endDate}");
        periodMappings.put("locationList", "${locationList}");

		PatientDataSetDefinition dsd = new PatientDataSetDefinition();
		dsd.setName("EMTCT");

        dsd.addParameter(new Parameter("startDate", "Report Date", Date.class));
        dsd.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        dsd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

		dsd.addSortCriteria("id", SortCriteria.SortDirection.ASC);
        dsd.addColumn("id", new ICAPCCCNoDataDefinition(), nullString);
		dsd.addColumn("name", new PreferredNameDataDefinition(), nullString, new ObjectFormatter());

		AgeAtEvaluationDateDataDefinition add = new AgeAtEvaluationDateDataDefinition();
		dsd.addColumn("age", add, nullString, new DecimalAgeConverter(0));
        dsd.addColumn("status", new ICAPPregnancyStatusDataDefinition(), nullString, new ICAPPregnancyStatusConverter());
        dsd.addColumn("regimenName",new CurrentRegimenDataDefinition(),nullString,new RegimenConverter(true));
        dsd.addColumn("regimenType",new CurrentRegimenDataDefinition(),nullString,new RegimenConverter(false));

        PatientOnARTDataDefinition art = new PatientOnARTDataDefinition();
        art.addParameter(new Parameter("startDate", "Report Date", Date.class));
        art.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        art.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        dsd.addColumn("patientOn", art,periodMappings, new OnARTOrCareConverter());

        PregnancyStatusDataDefinition status = new PregnancyStatusDataDefinition();
        status.addParameter(new Parameter("startDate", "Report Date", Date.class));
        status.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        status.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        dsd.addColumn("pregnant", status,periodMappings);

        PregnantAndOnARTDataDefinition pregnantAndOnART = new PregnantAndOnARTDataDefinition();
        pregnantAndOnART.addParameter(new Parameter("startDate", "Report Date", Date.class));
        pregnantAndOnART.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        pregnantAndOnART.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        dsd.addColumn("pregnantOnART", pregnantAndOnART,periodMappings);

        PregnantAndOnARTDataDefinition pregnantAndOnARTCD4Below350 = new PregnantAndOnARTDataDefinition();
        pregnantAndOnARTCD4Below350.addParameter(new Parameter("startDate", "Report Date", Date.class));
        pregnantAndOnARTCD4Below350.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        pregnantAndOnARTCD4Below350.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        dsd.addColumn("pregnantARTCD4Below350", pregnantAndOnARTCD4Below350,periodMappings);

        PregnantNotOnARTCD4Below350DataDefinition pregnantNotOnARTCD4Below350 = new PregnantNotOnARTCD4Below350DataDefinition();
        pregnantNotOnARTCD4Below350.addParameter(new Parameter("startDate", "Report Date", Date.class));
        pregnantNotOnARTCD4Below350.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        pregnantNotOnARTCD4Below350.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        dsd.addColumn("pregnantNotOnARTCD4Below350", pregnantNotOnARTCD4Below350,periodMappings);

		report.addDataSetDefinition(dsd,periodMappings);

		return report;
	}

	@Override
	public CohortDefinition getCohortDefinition() {
        String sql ="select   o.person_id  " +
                "  from obs o  " +
                "  inner join person p  " +
                "  on p.person_id=o.person_id   " +
                "    where o.voided = 0  " +
                "    and gender='F' " +
                "    and o.concept_id = 5272" +
                "    and p.voided=0   " +
                "    and o.obs_datetime between (:startDate) and (:endDate)  " +
                "    and o.location_id in ( :locationList ) ";

        CohortDefinition generalCOhort = new SqlCohortDefinition(sql);
        generalCOhort.setName("EMTCT REPORT");

        generalCOhort.addParameter(new Parameter("startDate", "Report Date", Date.class));
        generalCOhort.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        generalCOhort.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        return generalCOhort;
	}

	@Override
	public ReportDesign getReportDesign() {
		ReportDesign design = new ReportDesign();
		design.setName("EMTCT Report Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		Properties props = new Properties();
		props.put("repeatingSections", "sheet:1,row:7,dataset:EMTCT");

		design.setProperties(props);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/EMTCTReportTemplate.xls");

		if (is == null)
			throw new APIException("Could not find report template for EMTCT patients");

		try {
			resource.setContents(IOUtils.toByteArray(is));
		} catch (IOException ex) {
			throw new APIException("Could not create report design for EMTCT patients", ex);
		}

		IOUtils.closeQuietly(is);
		design.addResource(resource);

		return design;
	}
}