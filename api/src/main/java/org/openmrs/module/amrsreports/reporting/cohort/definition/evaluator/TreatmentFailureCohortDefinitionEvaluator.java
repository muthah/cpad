package org.openmrs.module.amrsreports.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.model.CD4Details;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TreatmentFailureCohortDefinition;
import org.openmrs.module.amrsreports.reporting.data.DateARTStartedDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.TreatmentCD4CountsDataDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.service.PersonDataService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Evaluator for treatmentFailureCohortDefinition
 * It evaluates art start date together with TreatmentFailureCD4CountsDataDefinition
 */
@Handler(supports = {TreatmentFailureCohortDefinition.class})
public class TreatmentFailureCohortDefinitionEvaluator implements CohortDefinitionEvaluator {

    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

		context.addParameterValue("startDate", context.getParameterValue("startDate"));
		context.addParameterValue("endDate", context.getParameterValue("endDate"));
		context.addParameterValue("minCd4", context.getParameterValue("minCd4"));

		TreatmentFailureCohortDefinition def = (TreatmentFailureCohortDefinition) cohortDefinition;
		DateARTStartedDataDefinition artStartDate = new DateARTStartedDataDefinition();
		EvaluatedPersonData artStartDateData = Context.getService(PersonDataService.class).evaluate(artStartDate, context);
		Map<Integer, Object> startDates = artStartDateData.getData();

		TreatmentCD4CountsDataDefinition cdCount = new TreatmentCD4CountsDataDefinition();
		Map<Integer, Object> cd4Data = Context.getService(PersonDataService.class).evaluate(cdCount, context).getData();

		Integer monthsAfterInitiation = (Integer) context.getParameterValue("monthsAfter");
		Double minCD4 = (Double) context.getParameterValue("minCD4");

		Cohort cohort = new Cohort();

		for( Integer ptId:startDates.keySet()){
			Date startDate = (Date) startDates.get(ptId);
			Date endDate = calEffectiveDate(startDate, monthsAfterInitiation);
			Set<CD4Details> ptData =(Set<CD4Details>) cd4Data.get(ptId);
			boolean isEligible = eligible(startDate, endDate, minCD4, ptData );
			if (isEligible)
				cohort.addMember(ptId);

		}

        return new EvaluatedCohort(cohort, def, context);
    }

	private Date calEffectiveDate(Date date, Integer dateToAdd){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, dateToAdd);
		return cal.getTime();
	}

	private boolean eligible(Date lowerLimit, Date upperLimit, Double minCd4Count, Set<CD4Details> patientCD4){

		Calendar lowerLimitCal = Calendar.getInstance();
		lowerLimitCal.setTime(lowerLimit);

		Calendar upperLimitCal = Calendar.getInstance();
		upperLimitCal.setTime(upperLimit);

		for (CD4Details data: patientCD4){
			Date obsDate = data.getObs_datetime();
			Calendar obsCal = Calendar.getInstance();
			obsCal.setTime(obsDate);

			if (obsCal.before(upperLimitCal) && obsCal.after(lowerLimitCal)) {
				Double val = data.getValue_numeric();
				if (val != null && val < minCd4Count){
					return true;
				}
			}
		}
		return false;
	}
}
