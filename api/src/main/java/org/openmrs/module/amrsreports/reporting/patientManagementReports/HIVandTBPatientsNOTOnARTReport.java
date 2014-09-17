package org.openmrs.module.amrsreports.reporting.patientManagementReports;

import org.apache.commons.io.IOUtils;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.PatientMgtCohortLibrary;
import org.openmrs.module.amrsreports.reporting.ReportUtils;
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatientsCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.EnrolledInCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferINCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferOUTCohortDefinition;
import org.openmrs.module.amrsreports.reporting.converter.DecimalAgeConverter;
import org.openmrs.module.amrsreports.reporting.data.AgeAtEvaluationDateDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPCCCNoDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPCD4CountDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPMaritalStatusDataDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
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
import java.util.Properties;

/**
 * provides CD4 reports for adults and adolescents
 */
public class HIVandTBPatientsNOTOnARTReport {

    private Integer minAge;
    private Integer maxAge;
    private Double value1;
    private Double value2;

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Double getValue1() {
        return value1;
    }

    public void setValue1(Double value1) {
        this.value1 = value1;
    }

    public Double getValue2() {
        return value2;
    }

    public void setValue2(Double value2) {
        this.value2 = value2;
    }


    public ReportDefinition getReportDefinition() {

		String nullString = null;
		ReportDefinition report = new PeriodIndicatorReportDefinition();
		report.setName("adultsCD4Report");

		PatientDataSetDefinition dsd = new PatientDataSetDefinition();
		dsd.setName("adultsCD4Report");

        dsd.addSortCriteria("id", SortCriteria.SortDirection.ASC);
        dsd.addColumn("id", new ICAPCCCNoDataDefinition(), nullString);
		dsd.addColumn("name", new PreferredNameDataDefinition(), nullString, new ObjectFormatter());
		dsd.addColumn("sex", new GenderDataDefinition(), nullString);

		AgeAtEvaluationDateDataDefinition add = new AgeAtEvaluationDateDataDefinition();
		dsd.addColumn("age", add, nullString, new DecimalAgeConverter(0));
        /*dsd.addColumn("cd4", new ICAPCD4CountDataDefinition(), nullString);
        dsd.addColumn("maritalStatus", new ICAPMaritalStatusDataDefinition(), nullString);
*/
		report.addDataSetDefinition(dsd,null);

		return report;
	}


	public CohortDefinition getCohortDefinition() {
        PatientMgtCohortLibrary library = new PatientMgtCohortLibrary();
        CohortDefinition cohortDefinition = library.ageRangeInYears(this.getMinAge(), this.getMaxAge());
        cohortDefinition.setName("Adults Cohort");
        cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date",Date.class));

        EnrolledInCareCohortDefinition enrolledInHIV = new EnrolledInCareCohortDefinition();
        enrolledInHIV.addParameter(new Parameter("startDate", "Report Date", Date.class));
        enrolledInHIV.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        CohortDefinition tbPatients = library.hasObs(Context.getConceptService().getConcept(307),Context.getConceptService().getConcept(703));
        tbPatients.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        tbPatients.addParameter(new Parameter("onOrAfter", "After Date", Date.class));

        CompositionCohortDefinition ccd = new CompositionCohortDefinition();
        ccd.addParameter(new Parameter("endDate", "Before Date", Date.class));
        ccd.addParameter(new Parameter("startDate", "After Date", Date.class));
        ccd.addParameter(new Parameter("effectiveDate", "Effective Date",Date.class));
        ccd.setName("Composition cohort for children with a given CD4 count");
        ccd.addSearch("adultsCohort", ReportUtils.map(cohortDefinition, "effectiveDate=${effectiveDate}"));
        ccd.addSearch("tbPatients", ReportUtils.map(tbPatients, "onOrAfter=${startDate},onOrBefore=${endDate}"));
        ccd.addSearch("hivPatients", ReportUtils.<CohortDefinition>map(enrolledInHIV, "startDate=${startDate},endDate=${endDate}"));
        ccd.setCompositionString("adultsCohort AND tbPatients AND hivPatients");

        return ccd;
	}


	public ReportDesign getReportDesign() {
		ReportDesign design = new ReportDesign();
		design.setName("Booked Report Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		Properties props = new Properties();
		props.put("repeatingSections", "sheet:1,row:6,dataset:adultsCD4Report");

		design.setProperties(props);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/AdultCD4ReportTemplate.xls");

		if (is == null)
			throw new APIException("Could not find report template for Booked Report.");

		try {
			resource.setContents(IOUtils.toByteArray(is));
		} catch (IOException ex) {
			throw new APIException("Could not create report design for Booked Report", ex);
		}

		IOUtils.closeQuietly(is);
		design.addResource(resource);

		return design;
	}
}