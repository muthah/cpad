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
import org.openmrs.module.amrsreports.model.RegimenObject;
import org.openmrs.module.amrsreports.reporting.data.CurrentRegimenDataDefinition;
import org.openmrs.module.amrsreports.service.MohCoreService;
import org.openmrs.module.reporting.common.ListMap;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

import java.util.*;


/**
 */
@Handler(supports = CurrentRegimenDataDefinition.class, order = 50)
public class CurrentRegimenDataDataEvaluator implements PersonDataEvaluator {

	/**
	 * @param definition
	 * @param context
	 * @return
	 * @throws org.openmrs.module.reporting.evaluation.EvaluationException
	 * @should return value_datetime for TUBERCULOSIS DRUG TREATMENT START DATE
	 * @should return obs_datetime when drug TUBERCULOSIS TREATMENT PLAN is START DRUGS
	 */
	@Override
	public EvaluatedPersonData evaluate(final PersonDataDefinition definition, final EvaluationContext context) throws EvaluationException {
		EvaluatedPersonData data = new EvaluatedPersonData(definition, context);

		if (context.getBaseCohort().isEmpty())
			return data;

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("personIds", context.getBaseCohort());
        m.put("startDate", context.getParameterValue("startDate"));
        m.put("endDate", context.getParameterValue("endDate"));

        /*context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));*/
        
        String sql = "SELECT o.patient_id, rg.name, rg.regimen_type, max(o.date_created)    " +
                "     from orders o    " +
                "     inner join drug_order do using(order_id)    " +
                "     inner join drug d on do.drug_inventory_id = d.drug_id    " +
                "     inner join cpad_regimen_drug rd using(drug_id)    " +
                "     inner join cpad_regimen rg using(regimen_id) "     +
                "     where o.date_created between (:startDate) and (:endDate)"+
                "     and o.patient_id in (:personIds) " +
                "     and o.voided = 0 " +
                "     group by o.patient_id having max(o.date_created)";

		ListMap<Integer, RegimenObject> currentRegimen = makeRegimensMapFromSQL(sql, m);



		for (Integer memberId : context.getBaseCohort().getMemberIds()) {

			data.addData(memberId, safeFind(currentRegimen, memberId));
		}

		return data;
	}

	protected RegimenObject safeFind(final ListMap<Integer, RegimenObject> map, final Integer key) {
		RegimenObject regimen = new RegimenObject();
		if (map.size() > 0 && map.containsKey(key))
			return (RegimenObject) map.get(key);

		return regimen;
	}

	/**
	 * executes sql query and generates a ListMap<Integer, Date>
	 */
	protected ListMap<Integer, RegimenObject> makeRegimensMapFromSQL(String sql, Map<String, Object> substitutions) {
		List<Object> data = Context.getService(MohCoreService.class).executeSqlQuery(sql, substitutions);
		return makeRegimensMap(data);
	}

	/**
	 * generates a map of integers to lists of dates, assuming this is the kind of response expected from the SQL
	 */
	protected ListMap<Integer, RegimenObject> makeRegimensMap(List<Object> data) {
		ListMap<Integer, RegimenObject> regimensListMap = new ListMap<Integer, RegimenObject>();
		for (Object o : data) {
			Object[] parts = (Object[]) o;
			if (parts.length == 4) {

				Integer pId = (Integer) parts[0];
				String regimenName = (String) parts[1];
                String regimenType = (String) parts[2];
				if (pId != null && regimenName != null) {
                    RegimenObject regimen = new RegimenObject();
                    regimen.setRegimenName(regimenName);
                    regimen.setRegimenType(regimenType);
					regimensListMap.putInList(pId, regimen);
				}
			}
		}

		return regimensListMap;
	}

}
