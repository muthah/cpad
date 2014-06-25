/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.amrsreports.reporting.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.data.ICAPFirstWHOStageDataDefinition;
import org.openmrs.module.amrsreports.service.MohCoreService;
import org.openmrs.module.reporting.common.ListMap;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.dataset.query.service.DataSetQueryService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Evaluates an ObsForPersonDataDefinition to produce a PersonData
 */
@Handler(supports = ICAPFirstWHOStageDataDefinition.class, order = 50)
public class ICAPFirstWHOStageDataEvaluator implements PersonDataEvaluator {


	/**
	 * @should return the obs that match the passed definition configuration
	 * @see org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator#evaluate(org.openmrs.module.reporting.data.person.definition.PersonDataDefinition, org.openmrs.module.reporting.evaluation.EvaluationContext)
	 */
	public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context) throws EvaluationException {


		EvaluatedPersonData c = new EvaluatedPersonData(definition, context);

		if (context.getBaseCohort() != null && context.getBaseCohort().isEmpty()) {
			return c;
		}


		DataSetQueryService qs = Context.getService(DataSetQueryService.class);
		Map<String, Object> m = new HashMap<String, Object>();



        String sql = "select person_id, value_coded " +
                " 	from obs" +
                " 	where" +
                "		person_id in (:patientIds)" +
                "   	and concept_id=5356" +
                "		and value_coded is not null" +
                "		and voided = 0" +
                "       group by person_id "+
                "       having min(obs_datetime) ";


        if (context.getBaseCohort() != null) {
			m.put("patientIds", context.getBaseCohort());
		}


        ListMap<Integer, Integer> mappedFirstWHOStageConcepts = makeFirstWhoStageMapFromSQL(sql, m);

        for (Integer memberId : context.getBaseCohort().getMemberIds()) {
            Integer firstWhoStage = safeFind(mappedFirstWHOStageConcepts, memberId);

            c.addData(memberId, firstWhoStage);
        }

		return c;
	}

    protected ListMap<Integer, Integer> makeFirstWhoStageMapFromSQL(String sql, Map<String, Object> substitutions) {
        List<Object> data = Context.getService(MohCoreService.class).executeSqlQuery(sql, substitutions);

        return makeWhoStagesMap(data);
    }

    protected ListMap<Integer, Integer> makeWhoStagesMap(List<Object> data) {
        ListMap<Integer, Integer> dateListMap = new ListMap<Integer, Integer>();
        for (Object o : data) {
            Object[] parts = (Object[]) o;
            if (parts.length == 2) {
                Integer pId = (Integer) parts[0];
                Integer firstWHOStage = (Integer) parts[1];
                dateListMap.putInList(pId, firstWHOStage);
            }
        }

        return dateListMap;
    }

    protected Integer safeFind(final ListMap<Integer, Integer> map, final Integer key) {
        Integer statusCode = null;
        if (map.containsKey(key))
            statusCode = map.get(key).get(0);

        return statusCode;
    }
}
