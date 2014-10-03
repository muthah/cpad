package org.openmrs.module.amrsreports.reporting.data.evaluator;

import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.model.CD4Details;
import org.openmrs.module.amrsreports.reporting.cohort.definition.EnrolledInCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TBCohortDefinition;
import org.openmrs.module.amrsreports.reporting.data.TreatmentCD4CountsDataDefinition;
import org.openmrs.module.amrsreports.service.MohCoreService;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.common.ListMap;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Returns the age of each person in the cohort, according to the evaluation date from the context
 */
@Handler(supports = TreatmentCD4CountsDataDefinition.class, order = 50)
public class TreatmentCD4CountsDataEvaluator implements PersonDataEvaluator {
	/**
	 * @see org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator#evaluate(org.openmrs.module.reporting.data.person.definition.PersonDataDefinition, org.openmrs.module.reporting.evaluation.EvaluationContext)
	 */
	@Override
	public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context) throws EvaluationException {


		EvaluatedPersonData ret = new EvaluatedPersonData(definition, context);

		//evaluate enrolled in care
		EnrolledInCareCohortDefinition enrolled = new EnrolledInCareCohortDefinition();

		//add params
		enrolled.addParameter(new Parameter("startDate", "Report Date", Date.class));
		enrolled.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

		context.addParameterValue("startDate", context.getParameterValue("startDate"));
		context.addParameterValue("endDate", context.getParameterValue("endDate"));
		Cohort enrolledPatients = Context.getService(CohortDefinitionService.class).evaluate(enrolled, context);

		//
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

		for(Integer id: enrolledPatients.getMemberIds()){
			ret.addData(id,safeFind(cd4DetailsListMap,id));
		}

		return ret;
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