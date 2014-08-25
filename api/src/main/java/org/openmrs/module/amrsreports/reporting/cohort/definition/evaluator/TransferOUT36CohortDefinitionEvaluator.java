package org.openmrs.module.amrsreports.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferOUT24CohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

/**
 * Evaluator for Dead Patients Cohort Definition
 */
@Handler(supports = {TransferOUT24CohortDefinition.class})
public class TransferOUT36CohortDefinitionEvaluator implements CohortDefinitionEvaluator {


    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

        TransferOUT24CohortDefinition definition = (TransferOUT24CohortDefinition) cohortDefinition;

        if (definition == null)
            return null;
        /**
         * startDate = startDate + 12 months
         * endDate =
         */

		context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));

        String sql =
                "select o.person_id from person p   " +
                        "    inner join obs o using(person_id)   " +
                        "    where o.concept_id=160649   " +
                        "    and value_datetime > date_add((:startDate),INTERVAL 12 MONTH) and value_datetime <= date_add((:startDate),INTERVAL 24 MONTH)";

        SqlCohortDefinition sqlCohortDefinition = new SqlCohortDefinition(sql);
        Cohort results = Context.getService(CohortDefinitionService.class).evaluate(sqlCohortDefinition, context);

        return new EvaluatedCohort(results, sqlCohortDefinition, context);
    }
}
