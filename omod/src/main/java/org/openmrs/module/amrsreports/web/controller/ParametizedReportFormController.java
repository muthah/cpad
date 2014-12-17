package org.openmrs.module.amrsreports.web.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.QueuedReport;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.AdultCD4DropBelowPreTreatmentReport;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.AdultCD4DropPercentageReport;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.AdultsNotOnARTCD4Report;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.AdultsTreatmentFailurePersistenceReport;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.ChildrenCD4MNotInHARRTReport;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.ChildrenCD4YNotInHAARTReport;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.ChildrenNotInHAARTReport;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.HIVandTBPatientsNOTOnARTReport;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.LatestCD4CountReport;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.PedTFReportFour;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.PedTFReportOne;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.PedTFReportThree;
import org.openmrs.module.amrsreports.reporting.patientManagementReports.PedTFReportTwo;
import org.openmrs.module.amrsreports.service.QueuedReportService;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * controller for Run AMRS Reports page
 */
@Controller
@SessionAttributes("queuedReports")
public class ParametizedReportFormController {

	private final Log log = LogFactory.getLog(getClass());

	private static final String FORM_VIEW = "module/amrsreports/parametizedReportForm";

	/**
	 * adultsPersistence
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "adultsPersistence")
	public String generateAdultsPersistence(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		//Integer persistence = Integer.valueOf(request.getParameter("adultPersistencetimes"));

		if (StringUtils.isEmpty(request.getParameter("adultPersistencetimes"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for persistence");

			return "redirect:queuedParametizedReport.form";
		}

		if (StringUtils.isEmpty(request.getParameter("adt_maxcd4"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for max CD4!");

			return "redirect:queuedParametizedReport.form";
		}

		if (StringUtils.isEmpty(request.getParameter("adt_noOfMonths"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for no of months");
			return "redirect:queuedParametizedReport.form";
		}

		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}

		Double maxCd4 = Double.valueOf(request.getParameter("adt_maxcd4"));
		Integer no_of_months = Integer.valueOf(request.getParameter("adt_noOfMonths"));
		String effectiveDate = request.getParameter("evaluationDate");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = sdf.parse(effectiveDate);
		Date endDate = new Date();

		AdultsTreatmentFailurePersistenceReport queuedReport = new AdultsTreatmentFailurePersistenceReport();

		try{
			CohortDefinition cohortDefinition = queuedReport.getCohortDefinition();
			//cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
			//cohortDefinition.addParameter(new Parameter("endDate", "Before Date", Date.class));

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
			//e.getMessage();
			e.printStackTrace();
			throw new RuntimeException("There was a problem running this report!!!!" + e);
		}
		return "redirect:queuedParametizedReport.form";
	}


	/**
	 * adult cd4 drop below pre-treatment baseline
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "adultCd4DropBelowPreTreatmentReport")
	public String adultCd4DropBelowPreTreatmentBaseline(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		if (StringUtils.isEmpty(request.getParameter("adult_cd4_below_pretreatment_no_of_months"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for no of months");
			return "redirect:queuedParametizedReport.form";
		}

		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}

		Double maxCd4 = Double.valueOf(200);
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
			e.getMessage();
			//e.printStackTrace();
		}
		return "redirect:queuedParametizedReport.form";
	}

	/**
	 * adults whose cd4 count failed to surpass pre-treatment baseline
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "adultsCd4DropPercentage")
	public String adultPercCd4DropBelowPreTreatmentBaseline(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		if (StringUtils.isEmpty(request.getParameter("adult_perc_cd4drop_param"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for CD4 % Drop");
			return "redirect:queuedParametizedReport.form";
		}

		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}

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
			e.getMessage();
			//e.printStackTrace();
		}
		return "redirect:queuedParametizedReport.form";
	}

	//================================================= peds treatment failure reports ============================================================

	/**
	 * peds report one
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "ped_tf_rpt1")
	public String ped_tf_rpt1(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		if (StringUtils.isEmpty(request.getParameter("ped_tf_rpt1_min_age"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for min age in months");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("ped_tf_rpt1_max_age"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for max age in months");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("ped_tf_rpt1_max_cd4"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for max CD4");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("ped_tf_rpt1_months"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for no of months after initiation");
			return "redirect:queuedParametizedReport.form";
		}


		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}

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
			e.getMessage();
			//e.printStackTrace();
		}
		return "redirect:queuedParametizedReport.form";
	}

	/**
	 * peds report two: takes age params in years
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "ped_tf_rpt2")
	public String ped_tf_rpt2(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		if (StringUtils.isEmpty(request.getParameter("ped_tf_rpt2_min_age"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for min age");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("ped_tf_rpt2_max_cd4"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for max CD4");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("ped_tf_rpt2_months"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for months after initiation");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}

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
			e.getMessage();
			e.printStackTrace();
		}
		return "redirect:queuedParametizedReport.form";
	}


	/**
	 * peds report 3: report of peds who experienced a decline in cd4 count below pre-treatment baseline
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "ped_tf_rpt3")
	public String ped_tf_rpt3(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		if (StringUtils.isEmpty(request.getParameter("ped_tf_rpt3_months"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for months after initiation");
			return "redirect:queuedParametizedReport.form";
		}

		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}

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
			e.getMessage();
			e.printStackTrace();
		}
		return "redirect:queuedParametizedReport.form";
	}




	/**
	 * peds report 4: report of peds who experienced a decline in cd4 count below a given percentage and failed to regain beyond pre-treatment baseline
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "ped_tf_rpt4")
	public String ped_tf_rpt4(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		if (StringUtils.isEmpty(request.getParameter("ped_tf_rpt4_cd4_perc"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for CD4 drop level");
			return "redirect:queuedParametizedReport.form";
		}

		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}

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
			e.getMessage();
			e.printStackTrace();
		}
		return "redirect:queuedParametizedReport.form";
	}



	//================================================ end of peds treatment failure reports

	@RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "childrenNotInHaartReport")
	public String childrenNotInHaartIrrespectiveOfCD4Count(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		if (StringUtils.isEmpty(request.getParameter("childrenNotInHaartAgeInM"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for age");
			return "redirect:queuedParametizedReport.form";
		}

		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}
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
			e.getMessage();
			e.printStackTrace();
        }

		return "redirect:queuedParametizedReport.form";
	}

    @RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "childrenCD4MReport")
    public String childrenCD4NotInHAARTM(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		if (StringUtils.isEmpty(request.getParameter("childrenMinAgeM"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for min age");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("childrenMaxAgeM"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for max age");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("childrenMaxCD4M"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for max CD4");
			return "redirect:queuedParametizedReport.form";
		}

		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}

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
			e.getMessage();
			e.printStackTrace();
        }

		return "redirect:queuedParametizedReport.form";
    }

    @RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "childrenCD4YReport")
    public String childrenCD4NotInHAARTY(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		if (StringUtils.isEmpty(request.getParameter("childrenMinAgeY"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for min age");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("childrenMaxAgeY"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for max age");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("childrenMaxCD4Y"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for max CD4");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}
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
			e.getMessage();
			e.printStackTrace();
        }
		return "redirect:queuedParametizedReport.form";

    }

    @RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "childrenLatestCD4Report")
    public String childrenLatestCD4Report(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		if (StringUtils.isEmpty(request.getParameter("childrenLatestCD4"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for duration");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}
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
			e.getMessage();
			e.printStackTrace();
        }
		return "redirect:queuedParametizedReport.form";

    }

    @RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "adultsLatestCD4Report")
    public String adultsLatestCD4Report(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		if (StringUtils.isEmpty(request.getParameter("adultsLatestCD4"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for duration");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}
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
			e.getMessage();
			e.printStackTrace();
        }
		return "redirect:queuedParametizedReport.form";

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
			e.getMessage();
			e.printStackTrace();
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "module/amrsreports/queuedParametizedReport.form", params = "adultPatientsNotOnART")
    public String adultPatientsNotOnART(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response ) throws Exception {

		if (StringUtils.isEmpty(request.getParameter("adultsMaxCD4noPreg"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set value for max CD4");
			return "redirect:queuedParametizedReport.form";
		}
		if (StringUtils.isEmpty(request.getParameter("evaluationDate"))){
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "You did not set start date");
			return "redirect:queuedParametizedReport.form";
		}
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
			e.getMessage();
			e.printStackTrace();
        }
		return "redirect:queuedParametizedReport.form";
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
