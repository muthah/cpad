package org.openmrs.module.amrsreports.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.cohort.definition.PatientsWithRecentEncAtStartCohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

/**
 * Evaluator for patients with recent encounters or have future appointment dates
 */
@Handler(supports = {PatientsWithRecentEncAtStartCohortDefinition.class})
public class PatientsWithRecentEncAt36CohortDefinitionEvaluator implements CohortDefinitionEvaluator {


    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

        PatientsWithRecentEncAtStartCohortDefinition definition = (PatientsWithRecentEncAtStartCohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

        context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));

        String sql =
                "select e.patient_id from encounter e   " +
                        "  group by e.patient_id   " +
                        "     having max(e.encounter_datetime) between date_add((:startDate),INTERVAL -93 DAY) and (:startDate)   " +
                        "  union   " +
                        "     (select o.person_id from obs o where concept_id = 5096 and value_datetime IS NOT NULL and value_datetime >= (:startDate))";



        SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition(sql);
        Cohort results = Context.getService(CohortDefinitionService.class).evaluate(sqlCohortDefinition, context);

        return new EvaluatedCohort(results, sqlCohortDefinition, context);
    }
}
