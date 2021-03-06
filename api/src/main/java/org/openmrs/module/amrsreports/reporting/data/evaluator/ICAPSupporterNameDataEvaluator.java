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
import org.openmrs.module.amrsreports.reporting.data.ICAPSupporterNameDataDefinition;
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
@Handler(supports = ICAPSupporterNameDataDefinition.class, order = 50)
public class ICAPSupporterNameDataEvaluator implements PersonDataEvaluator {


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



        String sql = "select person_id, value_text " +
                " 	from obs" +
                " 	where" +
                "		person_id in (:patientIds)" +
                "   	and concept_id = 160750" +
                "		and voided = 0" +
                "       group by person_id "+
                "       having max(obs_datetime) ";


        if (context.getBaseCohort() != null) {
			m.put("patientIds", context.getBaseCohort());
		}


        ListMap<Integer, String> mappedTBConcepts = makeNameMapFromSQL(sql, m);

        for (Integer memberId : context.getBaseCohort().getMemberIds()) {
            String supporterName= safeFind(mappedTBConcepts, memberId);

            c.addData(memberId, supporterName);
        }

		return c;
	}

    protected ListMap<Integer, String> makeNameMapFromSQL(String sql, Map<String, Object> substitutions) {
        List<Object> data = Context.getService(MohCoreService.class).executeSqlQuery(sql, substitutions);

        return makeNamesMap(data);
    }

    protected ListMap<Integer, String> makeNamesMap(List<Object> data) {
        ListMap<Integer, String> dateListMap = new ListMap<Integer, String>();
        for (Object o : data) {
            Object[] parts = (Object[]) o;
            if (parts.length == 2) {
                Integer pId = (Integer) parts[0];
                String pName = (String) parts[1];
                dateListMap.putInList(pId, pName);
            }
        }

        return dateListMap;
    }

    protected String safeFind(final ListMap<Integer, String> map, final Integer key) {
        String pName = null;
        if (map.containsKey(key))
            pName = map.get(key).get(0);

        return pName;
    }
}
