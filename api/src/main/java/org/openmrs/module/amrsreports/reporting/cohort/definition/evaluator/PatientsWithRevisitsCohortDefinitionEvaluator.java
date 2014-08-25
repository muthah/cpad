package org.openmrs.module.amrsreports.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.cohort.definition.NewEnrollmentsCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.PatientsOnADayCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.PatientsWithRevisitsCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.RevisitsCohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Evaluator for patients with recent encounters or have future appointment dates
 */
@Handler(supports = {PatientsWithRevisitsCohortDefinition.class})
public class PatientsWithRevisitsCohortDefinitionEvaluator implements CohortDefinitionEvaluator {


    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

        PatientsWithRevisitsCohortDefinition definition = (PatientsWithRevisitsCohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

        Cohort newEnrollments = new Cohort();

        PatientsOnADayCohortDefinition allPatients = new PatientsOnADayCohortDefinition();
        RevisitsCohortDefinition revisits = new RevisitsCohortDefinition();

        //add params
        allPatients.addParameter(new Parameter("startDate", "Report Date", Date.class));
        allPatients.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        revisits.addParameter(new Parameter("startDate", "Report Date", Date.class));
        revisits.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));

        Cohort all = Context.getService(CohortDefinitionService.class).evaluate(allPatients, context);
        Cohort revisitPatients = Context.getService(CohortDefinitionService.class).evaluate(revisits, context);

        Set<Integer> finalMembers = new HashSet<Integer>();

        for(Integer id:all.getMemberIds()){
            if(revisitPatients.getMemberIds().contains(id)){
                finalMembers.add(id);
            }
        }

        newEnrollments.setMemberIds(finalMembers);

        return new EvaluatedCohort(newEnrollments, cohortDefinition, context);
    }
}
