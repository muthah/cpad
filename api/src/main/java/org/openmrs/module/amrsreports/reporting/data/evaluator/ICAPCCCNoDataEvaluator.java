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
import org.openmrs.module.amrsreports.reporting.data.ICAPCCCNoDataDefinition;
import org.openmrs.module.amrsreports.service.MohCoreService;
import org.openmrs.module.reporting.common.ListMap;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * evaluator for CCC Number
 */
@Handler(supports = ICAPCCCNoDataDefinition.class, order = 50)
public class ICAPCCCNoDataEvaluator implements PersonDataEvaluator {

	@Override
	public EvaluatedPersonData evaluate(final PersonDataDefinition definition, final EvaluationContext context) throws EvaluationException {
		EvaluatedPersonData data = new EvaluatedPersonData(definition, context);

		if (context.getBaseCohort().isEmpty())
			return data;

		String hql = "select patient.id, identifier" +
				"	from PatientIdentifier" +
				" 	where voided = false" +
				"   	and patient.patientId in (:patientIds)" +
				"   	and identifierType.patientIdentifierTypeId = 2 ";

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("patientIds", context.getBaseCohort());

		ListMap<Integer, String> mappedStartDates = makeCCCNoMapFromHQL(hql, m);

		for (Integer memberId : context.getBaseCohort().getMemberIds()) {

			Set<String> startDates = findInMap(mappedStartDates, memberId);
			data.addData(memberId, startDates);
		}

		return data;
	}

	/**
	 * replaces reportDate and personIds with data from private variables before generating a date map
	 */
	private ListMap<Integer, String> makeCCCNoMapFromHQL(final String query, final Map<String, Object> substitutions) {
		MohCoreService mcs = Context.getService(MohCoreService.class);
		List<Object> queryResult = mcs.executeScrollingHqlQuery(query, substitutions);

		ListMap<Integer, String> CCCNoListMap = new ListMap<Integer, String>();
		for (Object o : queryResult) {
			Object[] parts = (Object[]) o;
			if (parts.length == 2) {
				Integer pId = (Integer) parts[0];
				String cccNo = parts[1] !=null?(String) parts[1]:null;
                CCCNoListMap.putInList(pId, cccNo);
			}
		}

		return CCCNoListMap;
	}

    protected Set<String> findInMap(final ListMap<Integer, String> map, final Integer key) {
        Set<String> dateSet = new TreeSet<String>();
        if (map.containsKey(key))
            dateSet.addAll(map.get(key));
        return dateSet;
    }
}
