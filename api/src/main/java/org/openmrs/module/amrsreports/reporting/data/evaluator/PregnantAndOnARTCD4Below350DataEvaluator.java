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

import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CD4LessThan350CohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.OnARTCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.PregnantCohortDefinition;
import org.openmrs.module.amrsreports.reporting.data.PregnantAndOnARTCD4Below350DataDefinition;
import org.openmrs.module.amrsreports.reporting.data.PregnantAndOnARTDataDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;

import java.util.Date;
import java.util.Set;

/**
 * Evaluates an ObsForPersonDataDefinition to produce a PersonData
 */
@Handler(supports = PregnantAndOnARTCD4Below350DataDefinition.class, order = 50)
public class PregnantAndOnARTCD4Below350DataEvaluator implements PersonDataEvaluator {


	/**
	 * @should return the obs that match the passed definition configuration
	 * @see org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator#evaluate(org.openmrs.module.reporting.data.person.definition.PersonDataDefinition, org.openmrs.module.reporting.evaluation.EvaluationContext)
	 */
	public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context) throws EvaluationException {


		EvaluatedPersonData c = new EvaluatedPersonData(definition, context);

		if (context.getBaseCohort() != null && context.getBaseCohort().isEmpty()) {
			return c;
		}

        PregnantCohortDefinition pregnantCohort = new PregnantCohortDefinition();
        OnARTCohortDefinition onArt = new OnARTCohortDefinition();
        CD4LessThan350CohortDefinition cd4Below350 = new CD4LessThan350CohortDefinition();

        //add params
        pregnantCohort.addParameter(new Parameter("startDate", "Report Date", Date.class));
        pregnantCohort.addParameter(new Parameter("endDate", "End Date", Date.class));

        onArt.addParameter(new Parameter("startDate", "Report Date", Date.class));
        onArt.addParameter(new Parameter("endDate", "End Date", Date.class));

        cd4Below350.addParameter(new Parameter("startDate", "Report Date", Date.class));
        cd4Below350.addParameter(new Parameter("endDate", "End Date", Date.class));

        context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));

        Cohort pregnantPatients = Context.getService(CohortDefinitionService.class).evaluate(pregnantCohort, context);
        Set<Integer> pregnantPatientIds = pregnantPatients.getMemberIds();

        Cohort artPatients = Context.getService(CohortDefinitionService.class).evaluate(onArt, context);
        Set<Integer> patientsOnART = artPatients.getMemberIds();

        Cohort artPatientswithCD4Below350 = Context.getService(CohortDefinitionService.class).evaluate(cd4Below350, context);
        Set<Integer> artCD4Below350Patients = artPatientswithCD4Below350.getMemberIds();


        for (Integer memberId : context.getBaseCohort().getMemberIds()) {
            String isTrue =  "No";

            if(pregnantPatientIds.contains(memberId) && patientsOnART.contains(memberId) && artCD4Below350Patients.contains(memberId)){
                  isTrue = "Yes";
            }

            c.addData(memberId, isTrue);
        }

		return c;
	}

}
