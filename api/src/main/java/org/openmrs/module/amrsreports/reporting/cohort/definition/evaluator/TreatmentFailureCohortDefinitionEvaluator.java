package org.openmrs.module.amrsreports.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.model.CD4Details;
import org.openmrs.module.amrsreports.reporting.cohort.definition.EnrolledInCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TreatmentFailureCohortDefinition;
import org.openmrs.module.amrsreports.reporting.data.DateARTStartedDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.ICAPArvStartDateDataDefinition;
import org.openmrs.module.amrsreports.reporting.data.TreatmentCD4CountsDataDefinition;
import org.openmrs.module.amrsreports.service.MohCoreService;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.common.ListMap;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.service.PersonDataService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Evaluator for treatmentFailureCohortDefinition
 * It evaluates art start date together with TreatmentFailureCD4CountsDataDefinition
 */
@Handler(supports = {TreatmentFailureCohortDefinition.class})
public class TreatmentFailureCohortDefinitionEvaluator implements CohortDefinitionEvaluator {

    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

		TreatmentFailureCohortDefinition def = (TreatmentFailureCohortDefinition) cohortDefinition;

		//get cd4 counts
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("startDate", context.getParameterValue("startDate"));
		m.put("endDate", context.getParameterValue("endDate"));
		m.put("minCd4", context.getParameterValue("minCd4"));

		String sql = "select person_id, obs_datetime, value_numeric" +
				" 	from obs " +
				" 	where " +
				"		concept_id=5497 and value_numeric < :minCd4 " +
				"   	and obs_datetime between (:startDate) and (:endDate) " +
				"		and voided = 0";

		ListMap<Integer, CD4Details> cd4DetailsListMap = makeResultsMapFromSQL(sql, m);
		Set<Integer> cdpatients = cd4DetailsListMap.keySet();
		context.setBaseCohort(new Cohort(cdpatients));

		ICAPArvStartDateDataDefinition artStartDate = new ICAPArvStartDateDataDefinition();
		EvaluatedPersonData artStartDateData = Context.getService(PersonDataService.class).evaluate(artStartDate, context);
		Map<Integer, Object> startDates = artStartDateData.getData();

		Integer monthsAfterInitiation = (Integer) context.getParameterValue("monthsAfter");
		Double minCD4 = (Double) context.getParameterValue("minCd4");
		Cohort finalCohort = new Cohort();
		Cohort cohort = new Cohort();
		cohort.setMemberIds(cd4DetailsListMap.keySet());

		if (!cdpatients.isEmpty()){

			for( Integer ptId:cdpatients){
				Date startDate = (Date) startDates.get(ptId);
				Date endDate = calEffectiveDate(startDate, monthsAfterInitiation);
				Set<CD4Details> ptData = safeFind(cd4DetailsListMap, ptId);
				boolean isEligible = eligible(startDate, endDate, minCD4, ptData );
				if (isEligible)
					finalCohort.addMember(ptId);

			}
		}


        return new EvaluatedCohort(finalCohort, def, context);
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

	/**
	 * executes sql query and generates a ListMap<Integer, Date>
	 */
	protected ListMap<Integer, CD4Details> makeResultsMapFromSQL(String sql, Map<String, Object> substitutions) {
		List<Object> data = Context.getService(MohCoreService.class).executeSqlQuery(sql, substitutions);
		return makeResultsMap(data);
	}

	/**
	 * generates a map of integers to lists of dates, assuming this is the kind of response expected from the SQL
	 */
	protected ListMap<Integer, CD4Details> makeResultsMap(List<Object> data) {
		ListMap<Integer, CD4Details> dateListMap = new ListMap<Integer, CD4Details>();
		for (Object o : data) {
			Object[] parts = (Object[]) o;
			if (parts.length == 3) {
				Integer pId = (Integer) parts[0];
				Date date = (Date) parts[1];
				Double val = (Double) parts[2];
				if (pId != null && date != null) {
					CD4Details details = new CD4Details(val, date);
					dateListMap.putInList(pId, details);
				}
			}
		}

		return dateListMap;
	}

	protected Set<CD4Details> safeFind(final ListMap<Integer, CD4Details> map, final Integer key) {
		Set<CD4Details> dateSet = new TreeSet<CD4Details>();
		if (map.size() > 0 && map.containsKey(key))
			dateSet.addAll(map.get(key));
		return dateSet;
	}
}
