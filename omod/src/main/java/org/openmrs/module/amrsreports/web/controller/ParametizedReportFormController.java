package org.openmrs.module.amrsreports.web.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.MOHFacility;
import org.openmrs.module.amrsreports.QueuedReport;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.*;
import org.openmrs.module.amrsreports.reporting.provider.ReportProvider;
import org.openmrs.module.amrsreports.service.QueuedReportService;
import org.openmrs.module.amrsreports.service.ReportProviderRegistrar;
import org.openmrs.module.amrsreports.service.UserFacilityService;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.renderer.ExcelTemplateRenderer;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.WebConstants;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * controller for Run AMRS Reports page
 */
@Controller
@SessionAttributes("queuedReports")
public class ParametizedReportFormController {

	private final Log log = LogFactory.getLog(getClass());

	private static final String FORM_VIEW = "module/amrsreports/parametizedReportForm";
	private static final String SUCCESS_VIEW = "redirect:queuedReport.list";

	/**
	 * adultsPersistence
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "adultsPersistence")
	public void generateAdultsPersistence(HttpServletRequest request, HttpServletResponse response ) throws Exception {

		Integer persistence = Integer.valueOf(request.getParameter("adultPersistencetimes"));
		Double maxCd4 = Double.valueOf(request.getParameter("adt_maxcd4"));
		Integer no_of_months = Integer.valueOf(request.getParameter("adt_noOfMonths"));
		String effectiveDate = request.getParameter("evaluationDate");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = sdf.parse(effectiveDate);
		Date endDate = new Date();

		AdultsTreatmentFailurePersistenceReport queuedReport = new AdultsTreatmentFailurePersistenceReport();

		try{
			CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
			cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));

			ReportDefinition reportDefinition = queuedReport.getReportDefinition();
			EvaluationContext evaluationContext = new EvaluationContext();
			evaluationContext.setEvaluationDate(endDate);
			evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
			evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
			evaluationContext.addParameterValue("minCd4", maxCd4);
			evaluationContext.addParameterValue("monthsAfter", no_of_months);

			// get the cohort
			CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
			Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
			evaluationContext.setBaseCohort(cohort);

			ReportData reportData = Context.getService(ReportDefinitionService.class)
					.evaluate(reportDefinition, evaluationContext);

			File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
			OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

			final ReportDesign design = queuedReport.getReportDesign();
			ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
				public ReportDesign getDesign(String argument) {
					return design;
				}
			};
			renderer.render(reportData, "reportManagement", stream);
			stream.close();

			response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
			response.setContentType("application/vnd.ms-excel");
			OutputStream excelFileDownload = response.getOutputStream();
			FileInputStream fileInputStream = new FileInputStream(xlsFile);

			IOUtils.copy(fileInputStream, excelFileDownload);
			fileInputStream.close();
			excelFileDownload.flush();
			excelFileDownload.close();
			xlsFile.delete();
		}  catch (Exception e){
			e.printStackTrace();

			throw new RuntimeException("There was a problem running this report!!!!");
		}
	}


	/**
	 * adult cd4 drop below pre-treatment baseline
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "adultCd4DropBelowPreTreatmentReport")
	public void adultCd4DropBelowPreTreatmentBaseline(HttpServletRequest request, HttpServletResponse response ) throws Exception {

		Integer persistence = Integer.valueOf(request.getParameter("adultPersistencetimes"));
		Double maxCd4 = Double.valueOf(request.getParameter("adt_maxcd4"));
		Integer no_of_months = Integer.valueOf(request.getParameter("adult_cd4_below_pretreatment_no_of_months"));
		String effectiveDate = request.getParameter("evaluationDate");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = sdf.parse(effectiveDate);
		Date endDate = new Date();

		AdultCD4DropBelowPreTreatmentReport queuedReport = new AdultCD4DropBelowPreTreatmentReport();

		try{
			CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
			cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));

			ReportDefinition reportDefinition = queuedReport.getReportDefinition();
			EvaluationContext evaluationContext = new EvaluationContext();
			evaluationContext.setEvaluationDate(endDate);
			evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
			evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
			evaluationContext.addParameterValue("minCd4", maxCd4);
			evaluationContext.addParameterValue("monthsAfter", no_of_months);

			// get the cohort
			CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
			Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
			evaluationContext.setBaseCohort(cohort);

			ReportData reportData = Context.getService(ReportDefinitionService.class)
					.evaluate(reportDefinition, evaluationContext);

			File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
			OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

			final ReportDesign design = queuedReport.getReportDesign();
			ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
				public ReportDesign getDesign(String argument) {
					return design;
				}
			};
			renderer.render(reportData, "reportManagement", stream);
			stream.close();

			response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
			response.setContentType("application/vnd.ms-excel");
			OutputStream excelFileDownload = response.getOutputStream();
			FileInputStream fileInputStream = new FileInputStream(xlsFile);

			IOUtils.copy(fileInputStream, excelFileDownload);
			fileInputStream.close();
			excelFileDownload.flush();
			excelFileDownload.close();
			xlsFile.delete();
		}  catch (Exception e){
			e.printStackTrace();

			throw new RuntimeException("There was a problem running this report!!!!");
		}

	}

	/**
	 * adults whose cd4 count failed to surpass pre-treatment baseline
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "adultsCd4DropPercentage")
	public void adultPercCd4DropBelowPreTreatmentBaseline(HttpServletRequest request, HttpServletResponse response ) throws Exception {

		Integer adult_perc_cd4_baseline = Integer.valueOf(request.getParameter("adult_perc_cd4drop_param"));
		String effectiveDate = request.getParameter("evaluationDate");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = sdf.parse(effectiveDate);
		Date endDate = new Date();

		AdultCD4DropPercentageReport queuedReport = new AdultCD4DropPercentageReport();


		try{
			CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
			cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));

			ReportDefinition reportDefinition = queuedReport.getReportDefinition();
			EvaluationContext evaluationContext = new EvaluationContext();
			evaluationContext.setEvaluationDate(endDate);
			evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
			evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
			evaluationContext.addParameterValue("minCd4", adult_perc_cd4_baseline);
			evaluationContext.addParameterValue("monthsAfter", 6);

			// get the cohort
			CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
			Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
			evaluationContext.setBaseCohort(cohort);

			ReportData reportData = Context.getService(ReportDefinitionService.class)
					.evaluate(reportDefinition, evaluationContext);

			File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
			OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

			final ReportDesign design = queuedReport.getReportDesign();
			ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
				public ReportDesign getDesign(String argument) {
					return design;
				}
			};
			renderer.render(reportData, "reportManagement", stream);
			stream.close();

			response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
			response.setContentType("application/vnd.ms-excel");
			OutputStream excelFileDownload = response.getOutputStream();
			FileInputStream fileInputStream = new FileInputStream(xlsFile);

			IOUtils.copy(fileInputStream, excelFileDownload);
			fileInputStream.close();
			excelFileDownload.flush();
			excelFileDownload.close();
			xlsFile.delete();
		}  catch (Exception e){
			e.printStackTrace();

			throw new RuntimeException("There was a problem running this report!!!!");
		}

	}

	//================================================= peds treatment failure reports ============================================================

	/**
	 * peds report one
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "ped_tf_rpt1")
	public void ped_tf_rpt1(HttpServletRequest request, HttpServletResponse response ) throws Exception {

		Integer ped_tf_rpt1_min_age = Integer.valueOf(request.getParameter("ped_tf_rpt1_min_age"));
		Integer ped_tf_rpt1_max_age = Integer.valueOf(request.getParameter("ped_tf_rpt1_max_age"));
		Double ped_tf_rpt1_max_cd4 = Double.valueOf(request.getParameter("ped_tf_rpt1_max_cd4"));
		Integer ped_tf_rpt1_months = Integer.valueOf(request.getParameter("ped_tf_rpt1_months"));


		String effectiveDate = request.getParameter("evaluationDate");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = sdf.parse(effectiveDate);
		Date endDate = new Date();

		PedTFReportOne queuedReport = new PedTFReportOne();
		queuedReport.setMaxAge(ped_tf_rpt1_max_age);
		queuedReport.setMinAge(ped_tf_rpt1_min_age);

		try{
			CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
			cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));

			ReportDefinition reportDefinition = queuedReport.getReportDefinition();
			EvaluationContext evaluationContext = new EvaluationContext();
			evaluationContext.setEvaluationDate(endDate);
			evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
			evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
			evaluationContext.addParameterValue("minCd4", ped_tf_rpt1_max_cd4);
			evaluationContext.addParameterValue("monthsAfter", ped_tf_rpt1_months);

			// get the cohort
			CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
			Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
			evaluationContext.setBaseCohort(cohort);

			ReportData reportData = Context.getService(ReportDefinitionService.class)
					.evaluate(reportDefinition, evaluationContext);

			File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
			OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

			final ReportDesign design = queuedReport.getReportDesign();
			ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
				public ReportDesign getDesign(String argument) {
					return design;
				}
			};
			renderer.render(reportData, "reportManagement", stream);
			stream.close();

			response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
			response.setContentType("application/vnd.ms-excel");
			OutputStream excelFileDownload = response.getOutputStream();
			FileInputStream fileInputStream = new FileInputStream(xlsFile);

			IOUtils.copy(fileInputStream, excelFileDownload);
			fileInputStream.close();
			excelFileDownload.flush();
			excelFileDownload.close();
			xlsFile.delete();
		}  catch (Exception e){
			e.printStackTrace();

			throw new RuntimeException("There was a problem running this report!!!!");
		}

	}

	/**
	 * peds report two: takes age params in years
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "ped_tf_rpt2")
	public void ped_tf_rpt2(HttpServletRequest request, HttpServletResponse response ) throws Exception {

		Integer ped_tf_rpt2_min_age = Integer.valueOf(request.getParameter("ped_tf_rpt2_min_age"));
		Double ped_tf_rpt2_max_cd4 = Double.valueOf(request.getParameter("ped_tf_rpt2_max_cd4"));
		Integer ped_tf_rpt2_months = Integer.valueOf(request.getParameter("ped_tf_rpt2_months"));
		String effectiveDate = request.getParameter("evaluationDate");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = sdf.parse(effectiveDate);
		Date endDate = new Date();

		PedTFReportTwo queuedReport = new PedTFReportTwo();
		queuedReport.setMinAge(ped_tf_rpt2_min_age);

		try{
			CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
			cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));

			ReportDefinition reportDefinition = queuedReport.getReportDefinition();
			EvaluationContext evaluationContext = new EvaluationContext();
			evaluationContext.setEvaluationDate(endDate);
			evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
			evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
			evaluationContext.addParameterValue("minCd4", ped_tf_rpt2_max_cd4);
			evaluationContext.addParameterValue("monthsAfter", ped_tf_rpt2_months);

			// get the cohort
			CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
			Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
			evaluationContext.setBaseCohort(cohort);

			ReportData reportData = Context.getService(ReportDefinitionService.class)
					.evaluate(reportDefinition, evaluationContext);

			File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
			OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

			final ReportDesign design = queuedReport.getReportDesign();
			ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
				public ReportDesign getDesign(String argument) {
					return design;
				}
			};
			renderer.render(reportData, "reportManagement", stream);
			stream.close();

			response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
			response.setContentType("application/vnd.ms-excel");
			OutputStream excelFileDownload = response.getOutputStream();
			FileInputStream fileInputStream = new FileInputStream(xlsFile);

			IOUtils.copy(fileInputStream, excelFileDownload);
			fileInputStream.close();
			excelFileDownload.flush();
			excelFileDownload.close();
			xlsFile.delete();
		}  catch (Exception e){
			e.printStackTrace();

			throw new RuntimeException("There was a problem running this report!!!!");
		}

	}


	/**
	 * peds report 3: report of peds who experienced a decline in cd4 count below pre-treatment baseline
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "ped_tf_rpt3")
	public void ped_tf_rpt3(HttpServletRequest request, HttpServletResponse response ) throws Exception {

		Integer ped_tf_rpt3_months = Integer.valueOf(request.getParameter("ped_tf_rpt3_months"));
		String effectiveDate = request.getParameter("evaluationDate");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = sdf.parse(effectiveDate);
		Date endDate = new Date();

		PedTFReportThree queuedReport = new PedTFReportThree();


		try{
			CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
			cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));

			ReportDefinition reportDefinition = queuedReport.getReportDefinition();
			EvaluationContext evaluationContext = new EvaluationContext();
			evaluationContext.setEvaluationDate(endDate);
			evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
			evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
			evaluationContext.addParameterValue("minCd4", 200.0);
			evaluationContext.addParameterValue("monthsAfter", ped_tf_rpt3_months);

			// get the cohort
			CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
			Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
			evaluationContext.setBaseCohort(cohort);

			ReportData reportData = Context.getService(ReportDefinitionService.class)
					.evaluate(reportDefinition, evaluationContext);

			File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
			OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

			final ReportDesign design = queuedReport.getReportDesign();
			ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
				public ReportDesign getDesign(String argument) {
					return design;
				}
			};
			renderer.render(reportData, "reportManagement", stream);
			stream.close();

			response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
			response.setContentType("application/vnd.ms-excel");
			OutputStream excelFileDownload = response.getOutputStream();
			FileInputStream fileInputStream = new FileInputStream(xlsFile);

			IOUtils.copy(fileInputStream, excelFileDownload);
			fileInputStream.close();
			excelFileDownload.flush();
			excelFileDownload.close();
			xlsFile.delete();
		}  catch (Exception e){
			e.printStackTrace();

			throw new RuntimeException("There was a problem running this report!!!!");
		}

	}




	/**
	 * peds report 4: report of peds who experienced a decline in cd4 count below a given percentage and failed to regain beyond pre-treatment baseline
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "ped_tf_rpt4")
	public void ped_tf_rpt4(HttpServletRequest request, HttpServletResponse response ) throws Exception {

		Integer ped_tf_rpt4_cd4_perc = Integer.valueOf(request.getParameter("ped_tf_rpt4_cd4_perc"));
		String effectiveDate = request.getParameter("evaluationDate");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = sdf.parse(effectiveDate);
		Date endDate = new Date();

		PedTFReportFour queuedReport = new PedTFReportFour();


		try{
			CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
			cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));

			ReportDefinition reportDefinition = queuedReport.getReportDefinition();
			EvaluationContext evaluationContext = new EvaluationContext();
			evaluationContext.setEvaluationDate(endDate);
			evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
			evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
			evaluationContext.addParameterValue("minCd4", ped_tf_rpt4_cd4_perc);
			evaluationContext.addParameterValue("monthsAfter", 10);

			// get the cohort
			CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
			Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
			evaluationContext.setBaseCohort(cohort);

			ReportData reportData = Context.getService(ReportDefinitionService.class)
					.evaluate(reportDefinition, evaluationContext);

			File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
			OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

			final ReportDesign design = queuedReport.getReportDesign();
			ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
				public ReportDesign getDesign(String argument) {
					return design;
				}
			};
			renderer.render(reportData, "reportManagement", stream);
			stream.close();

			response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
			response.setContentType("application/vnd.ms-excel");
			OutputStream excelFileDownload = response.getOutputStream();
			FileInputStream fileInputStream = new FileInputStream(xlsFile);

			IOUtils.copy(fileInputStream, excelFileDownload);
			fileInputStream.close();
			excelFileDownload.flush();
			excelFileDownload.close();
			xlsFile.delete();
		}  catch (Exception e){
			e.printStackTrace();

			throw new RuntimeException("There was a problem running this report!!!!");
		}

	}



	//================================================ end of peds treatment failure reports

	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "childrenNotInHaartReport")
	public void childrenNotInHaartIrrespectiveOfCD4Count(HttpServletRequest request, HttpServletResponse response ) throws Exception {

        Integer maxAge = Integer.valueOf(request.getParameter("childrenNotInHaartAgeInM"));
        String effectiveDate = request.getParameter("evaluationDate");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = sdf.parse(effectiveDate);
        Date endDate = new Date();

        ChildrenNotInHAARTReport queuedReport = new ChildrenNotInHAARTReport();
        queuedReport.setMaxAge(maxAge);

        try{
            CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
            cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));

            ReportDefinition reportDefinition = queuedReport.getReportDefinition();
            EvaluationContext evaluationContext = new EvaluationContext();
            evaluationContext.setEvaluationDate(endDate);
            evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
            evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
            // get the cohort
            CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
            Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
            evaluationContext.setBaseCohort(cohort);

            ReportData reportData = Context.getService(ReportDefinitionService.class)
                    .evaluate(reportDefinition, evaluationContext);

            File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
            OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

            final ReportDesign design = queuedReport.getReportDesign();
            ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
                public ReportDesign getDesign(String argument) {
                    return design;
                }
            };
            renderer.render(reportData, "reportManagement", stream);
            stream.close();

            response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
            response.setContentType("application/vnd.ms-excel");
            OutputStream excelFileDownload = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(xlsFile);

            IOUtils.copy(fileInputStream, excelFileDownload);
            fileInputStream.close();
            excelFileDownload.flush();
            excelFileDownload.close();
            xlsFile.delete();
        }  catch (Exception e){
            e.printStackTrace();

            throw new RuntimeException("There was a problem running this report!!!!");
        }



        //================================

		//httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Please wait as the report is processed." + persistencetimes + " ==" + effectiveDate);

		//return SUCCESS_VIEW;
	}

    @RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "childrenCD4MReport")
    public void childrenCD4NotInHAARTM(HttpServletRequest request, HttpServletResponse response ) throws Exception {

        Integer minAge = Integer.valueOf(request.getParameter("childrenMinAgeM"));
        Integer maxAge = Integer.valueOf(request.getParameter("childrenMaxAgeM"));
        Double maxCd4Count = Double.valueOf(request.getParameter("childrenMaxCD4M"));
        String effectiveDate = request.getParameter("evaluationDate");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = sdf.parse(effectiveDate);
        Date endDate = new Date();

        ChildrenCD4MNotInHARRTReport queuedReport = new ChildrenCD4MNotInHARRTReport();
        queuedReport.setMaxAge(maxAge);
        queuedReport.setMinAge(minAge);
        queuedReport.setValue1(maxCd4Count);

        try{
            CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
            cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
            cohortDefinition.addParameter(new Parameter("endDate", "Before Date", Date.class));
            cohortDefinition.addParameter(new Parameter("startDate", "After Date", Date.class));

            ReportDefinition reportDefinition = queuedReport.getReportDefinition();
            EvaluationContext evaluationContext = new EvaluationContext();
            evaluationContext.setEvaluationDate(endDate);
            evaluationContext.addParameterValue("effectiveDate",endDate);
            evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
            evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
            // get the cohort
            CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
            Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
            evaluationContext.setBaseCohort(cohort);

            ReportData reportData = Context.getService(ReportDefinitionService.class)
                    .evaluate(reportDefinition, evaluationContext);

            File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
            OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

            final ReportDesign design = queuedReport.getReportDesign();
            ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
                public ReportDesign getDesign(String argument) {
                    return design;
                }
            };
            renderer.render(reportData, "reportManagement", stream);
            stream.close();

            response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
            response.setContentType("application/vnd.ms-excel");
            OutputStream excelFileDownload = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(xlsFile);

            IOUtils.copy(fileInputStream, excelFileDownload);
            fileInputStream.close();
            excelFileDownload.flush();
            excelFileDownload.close();
            xlsFile.delete();

        }  catch (Exception e){
            e.printStackTrace();

            throw new RuntimeException("There was a problem running this report!!!!");
        }


    }

    @RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "childrenCD4YReport")
    public void childrenCD4NotInHAARTY(HttpServletRequest request, HttpServletResponse response ) throws Exception {

        Integer minAge = Integer.valueOf(request.getParameter("childrenMinAgeY"));
        Integer maxAge = Integer.valueOf(request.getParameter("childrenMaxAgeY"));
        Double maxCd4Count = Double.valueOf(request.getParameter("childrenMaxCD4Y"));
        String effectiveDate = request.getParameter("evaluationDate");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = sdf.parse(effectiveDate);
        Date endDate = new Date();

        ChildrenCD4YNotInHAARTReport queuedReport = new ChildrenCD4YNotInHAARTReport();
        queuedReport.setMaxAge(maxAge);
        queuedReport.setMinAge(minAge);
        queuedReport.setValue1(maxCd4Count);

        try{
            CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
            cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
            cohortDefinition.addParameter(new Parameter("endDate", "Before Date", Date.class));
            cohortDefinition.addParameter(new Parameter("startDate", "After Date", Date.class));

            ReportDefinition reportDefinition = queuedReport.getReportDefinition();
            EvaluationContext evaluationContext = new EvaluationContext();
            evaluationContext.setEvaluationDate(endDate);
            evaluationContext.addParameterValue("effectiveDate",endDate);
            evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
            evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
            // get the cohort
            CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
            Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
            evaluationContext.setBaseCohort(cohort);

            ReportData reportData = Context.getService(ReportDefinitionService.class)
                    .evaluate(reportDefinition, evaluationContext);

            File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
            OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

            final ReportDesign design = queuedReport.getReportDesign();
            ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
                public ReportDesign getDesign(String argument) {
                    return design;
                }
            };
            renderer.render(reportData, "reportManagement", stream);
            stream.close();

            response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
            response.setContentType("application/vnd.ms-excel");
            OutputStream excelFileDownload = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(xlsFile);

            IOUtils.copy(fileInputStream, excelFileDownload);
            fileInputStream.close();
            excelFileDownload.flush();
            excelFileDownload.close();
            xlsFile.delete();
        }  catch (Exception e){
            e.printStackTrace();

            throw new RuntimeException("There was a problem running this report!!!!");
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "childrenLatestCD4Report")
    public void childrenLatestCD4Report( HttpServletRequest request, HttpServletResponse response ) throws Exception {

        Integer period = Integer.valueOf(request.getParameter("childrenLatestCD4"));
        String effectiveDate = request.getParameter("evaluationDate");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = sdf.parse(effectiveDate);

        Calendar cl = Calendar.getInstance();
        cl.add(Calendar.MONTH, -(period));

        Date endDate = cl.getTime();

        LatestCD4CountReport queuedReport = new LatestCD4CountReport();
        queuedReport.setMaxAge(14);
        queuedReport.setMinAge(0);

        try{
            CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
            cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
            cohortDefinition.addParameter(new Parameter("endDate", "Before Date", Date.class));
            cohortDefinition.addParameter(new Parameter("startDate", "After Date", Date.class));

            ReportDefinition reportDefinition = queuedReport.getReportDefinition();
            EvaluationContext evaluationContext = new EvaluationContext();
            evaluationContext.setEvaluationDate(endDate);
            evaluationContext.addParameterValue("effectiveDate",endDate);
            evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
            evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
            // get the cohort
            CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
            Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
            evaluationContext.setBaseCohort(cohort);

            ReportData reportData = Context.getService(ReportDefinitionService.class)
                    .evaluate(reportDefinition, evaluationContext);

            File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
            OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

            final ReportDesign design = queuedReport.getReportDesign();
            ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
                public ReportDesign getDesign(String argument) {
                    return design;
                }
            };
            renderer.render(reportData, "reportManagement", stream);
            stream.close();

            response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
            response.setContentType("application/vnd.ms-excel");
            OutputStream excelFileDownload = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(xlsFile);

            IOUtils.copy(fileInputStream, excelFileDownload);
            fileInputStream.close();
            excelFileDownload.flush();
            excelFileDownload.close();
            xlsFile.delete();
        }  catch (Exception e){
            e.printStackTrace();

            throw new RuntimeException("There was a problem running this report!!!!");
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "adultsLatestCD4Report")
    public void adultsLatestCD4Report( HttpServletRequest request, HttpServletResponse response ) throws Exception {

        Integer period = Integer.valueOf(request.getParameter("adultsLatestCD4"));
        String effectiveDate = request.getParameter("evaluationDate");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = sdf.parse(effectiveDate);

        Calendar cl = Calendar.getInstance();
        cl.add(Calendar.MONTH, -(period));

        Date endDate = cl.getTime();

        LatestCD4CountReport queuedReport = new LatestCD4CountReport();
        queuedReport.setMaxAge(200);
        queuedReport.setMinAge(15);

        try{
            CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
            cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
            cohortDefinition.addParameter(new Parameter("endDate", "Before Date", Date.class));
            cohortDefinition.addParameter(new Parameter("startDate", "After Date", Date.class));

            ReportDefinition reportDefinition = queuedReport.getReportDefinition();
            EvaluationContext evaluationContext = new EvaluationContext();
            evaluationContext.setEvaluationDate(endDate);
            evaluationContext.addParameterValue("effectiveDate",endDate);
            evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
            evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
            // get the cohort
            CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
            Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
            evaluationContext.setBaseCohort(cohort);

            ReportData reportData = Context.getService(ReportDefinitionService.class)
                    .evaluate(reportDefinition, evaluationContext);

            File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
            OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

            final ReportDesign design = queuedReport.getReportDesign();
            ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
                public ReportDesign getDesign(String argument) {
                    return design;
                }
            };
            renderer.render(reportData, "reportManagement", stream);
            stream.close();

            response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
            response.setContentType("application/vnd.ms-excel");
            OutputStream excelFileDownload = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(xlsFile);

            IOUtils.copy(fileInputStream, excelFileDownload);
            fileInputStream.close();
            excelFileDownload.flush();
            excelFileDownload.close();
            xlsFile.delete();
        }  catch (Exception e){
            e.printStackTrace();

            throw new RuntimeException("There was a problem running this report!!!!");
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "adultsTBHIVCoinfectedReport")
    public void adultHIVandTBPatients( HttpServletRequest request, HttpServletResponse response ) throws Exception {

        String effectiveDate = request.getParameter("evaluationDate");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = sdf.parse(effectiveDate);

        Date endDate = new Date();

        HIVandTBPatientsNOTOnARTReport queuedReport = new HIVandTBPatientsNOTOnARTReport();
        queuedReport.setMaxAge(200);
        queuedReport.setMinAge(15);

        try{
            CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
            cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
            cohortDefinition.addParameter(new Parameter("endDate", "Before Date", Date.class));
            cohortDefinition.addParameter(new Parameter("startDate", "After Date", Date.class));

            ReportDefinition reportDefinition = queuedReport.getReportDefinition();
            EvaluationContext evaluationContext = new EvaluationContext();
            evaluationContext.setEvaluationDate(endDate);
            evaluationContext.addParameterValue("effectiveDate",endDate);
            evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
            evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
            // get the cohort
            CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
            Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
            evaluationContext.setBaseCohort(cohort);

            ReportData reportData = Context.getService(ReportDefinitionService.class)
                    .evaluate(reportDefinition, evaluationContext);

            File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
            OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

            final ReportDesign design = queuedReport.getReportDesign();
            ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
                public ReportDesign getDesign(String argument) {
                    return design;
                }
            };
            renderer.render(reportData, "reportManagement", stream);
            stream.close();

            response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
            response.setContentType("application/vnd.ms-excel");
            OutputStream excelFileDownload = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(xlsFile);

            IOUtils.copy(fileInputStream, excelFileDownload);
            fileInputStream.close();
            excelFileDownload.flush();
            excelFileDownload.close();
            xlsFile.delete();
        }  catch (Exception e){
            e.printStackTrace();

            throw new RuntimeException("There was a problem running this report!!!!");
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "adultPatientsNotOnART")
    public void adultPatientsNotOnART( HttpServletRequest request, HttpServletResponse response ) throws Exception {

        String effectiveDate = request.getParameter("evaluationDate");
        Double maxCD4 = Double.valueOf(request.getParameter("adultsMaxCD4noPreg"));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = sdf.parse(effectiveDate);

        Date endDate = new Date();

        AdultsNotOnARTCD4Report queuedReport = new AdultsNotOnARTCD4Report();
        queuedReport.setMaxAge(200);
        queuedReport.setMinAge(15);
        queuedReport.setValue1(maxCD4);

        try{
            CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
            cohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
            cohortDefinition.addParameter(new Parameter("endDate", "Before Date", Date.class));
            cohortDefinition.addParameter(new Parameter("startDate", "After Date", Date.class));

            ReportDefinition reportDefinition = queuedReport.getReportDefinition();
            EvaluationContext evaluationContext = new EvaluationContext();
            evaluationContext.setEvaluationDate(endDate);
            evaluationContext.addParameterValue("effectiveDate",endDate);
            evaluationContext.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), startDate);
            evaluationContext.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), endDate);
            // get the cohort
            CohortDefinitionService cohortDefinitionService = Context.getService(CohortDefinitionService.class);
            Cohort cohort = cohortDefinitionService.evaluate(cohortDefinition, evaluationContext);
            evaluationContext.setBaseCohort(cohort);

            ReportData reportData = Context.getService(ReportDefinitionService.class)
                    .evaluate(reportDefinition, evaluationContext);

            File xlsFile = File.createTempFile("patient_mgt_rpt", ".xls");
            OutputStream stream = new BufferedOutputStream(new FileOutputStream(xlsFile));

            final ReportDesign design = queuedReport.getReportDesign();
            ExcelTemplateRenderer renderer = new ExcelTemplateRenderer() {
                public ReportDesign getDesign(String argument) {
                    return design;
                }
            };
            renderer.render(reportData, "reportManagement", stream);
            stream.close();

            response.setHeader("Content-disposition", "attachment; filename=" + "patientMgtReport" + ".xls");
            response.setContentType("application/vnd.ms-excel");
            OutputStream excelFileDownload = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(xlsFile);

            IOUtils.copy(fileInputStream, excelFileDownload);
            fileInputStream.close();
            excelFileDownload.flush();
            excelFileDownload.close();
            xlsFile.delete();
        }  catch (Exception e){
            e.printStackTrace();

            throw new RuntimeException("There was a problem running this report!!!!");
        }

    }



    /**
     *
     * methods for GET
     */
	@RequestMapping(method = RequestMethod.GET, value = "module/amrsreports/queuedParametizedReport.form")
	public String editQueuedReport(
			@RequestParam(value = "queuedReportId", required = false) Integer queuedReportId,
            @RequestParam(value = "status", required = false) String status,
			ModelMap modelMap
                                ) {

		QueuedReport queuedReport = null;
        String inlineInstruction ="";


		if (queuedReportId != null)
			queuedReport = Context.getService(QueuedReportService.class).getQueuedReport(queuedReportId);

		if (queuedReport == null) {
			queuedReport = new QueuedReport();
		}

        if (OpenmrsUtil.nullSafeEquals("ERROR", status)) {
            inlineInstruction = "Check the new scheduled date and submit when finished";
            queuedReport.setDateScheduled(new Date());/*
            queuedReport.setStatus(QueuedReport.STATUS_NEW);*/
        }

		modelMap.put("queuedReports", queuedReport);
        modelMap.put("inlineInstruction",inlineInstruction);

		Integer interval = queuedReport.getRepeatInterval();
		Integer repeatInterval;

		if (interval < 60) {
			modelMap.put("units", "seconds");
			repeatInterval = interval;
		} else if (interval < 3600) {
			modelMap.put("units", "minutes");
			repeatInterval = interval / 60;
		} else if (interval < 86400) {
			modelMap.put("units", "hours");
			repeatInterval = interval / 3600;
		} else {
			modelMap.put("units", "days");
			repeatInterval = interval / 86400;
		}

		modelMap.put("repeatInterval", repeatInterval.toString());

		return FORM_VIEW;
	}


}
