package org.openmrs.module.amrsreports.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.cohort.definition.ChildrenCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatientsCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TBCohortDefinition;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.BaseSQLCohortLibrary;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

/**
 * Evaluator for TB Patients Cohort Definition
 */
@Handler(supports = {ChildrenCohortDefinition.class})
public class ChildrenCohortDefinitionEvaluator implements CohortDefinitionEvaluator {


    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

        ChildrenCohortDefinition definition = (ChildrenCohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

        context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));
        context.addParameterValue("effectiveDate",context.getEvaluationDate());
        BaseSQLCohortLibrary library = new BaseSQLCohortLibrary();
        CohortDefinition cd = null;
        if(definition.getMinAge() != null && definition.getMaxAge() != null){
             cd = library.agedBetweenInMonths(definition.getMinAge(),definition.getMaxAge());
        }
        else if(definition.getMinAge() != null && definition.getMaxAge() == null){
             cd = library.agedMinInMonths(definition.getMinAge());
        }
        else if(definition.getMinAge() == null && definition.getMaxAge() != null){
             cd = library.agedMaxInMonths(definition.getMaxAge());
        }

        Cohort results = Context.getService(CohortDefinitionService.class).evaluate(cd, context);

        return new EvaluatedCohort(results, definition, context);
    }
}
