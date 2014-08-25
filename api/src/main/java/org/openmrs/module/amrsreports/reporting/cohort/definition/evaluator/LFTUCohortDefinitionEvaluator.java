package org.openmrs.module.amrsreports.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CurrentlyInCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatientsCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.EnrolledInCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferINCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferOUTCohortDefinition;
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
 * Evaluator for Dead Patients Cohort Definition
 */
@Handler(supports = {CurrentlyInCareCohortDefinition.class})
public class LFTUCohortDefinitionEvaluator implements CohortDefinitionEvaluator {
    /**
     * in care = enrolled in care + transfer In - Transfer Out - Dead - LFTU
     */

    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

        CurrentlyInCareCohortDefinition definition = (CurrentlyInCareCohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

        Cohort currentlyInCare = new Cohort();

        EnrolledInCareCohortDefinition enrolled = new EnrolledInCareCohortDefinition();
        TransferINCohortDefinition ti = new TransferINCohortDefinition();
        TransferOUTCohortDefinition to = new TransferOUTCohortDefinition();
        DeadPatientsCohortDefinition dead = new DeadPatientsCohortDefinition();

        //add params
        enrolled.addParameter(new Parameter("startDate", "Report Date", Date.class));
        enrolled.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        ti.addParameter(new Parameter("startDate", "Report Date", Date.class));
        ti.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        to.addParameter(new Parameter("startDate", "Report Date", Date.class));
        to.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        dead.addParameter(new Parameter("startDate", "Report Date", Date.class));
        dead.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));


		context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));



        Cohort enrolledPatients = Context.getService(CohortDefinitionService.class).evaluate(enrolled, context);
        Cohort tiPatients = Context.getService(CohortDefinitionService.class).evaluate(ti, context);
        Cohort toPatients = Context.getService(CohortDefinitionService.class).evaluate(to, context);
        Cohort deadPatients = Context.getService(CohortDefinitionService.class).evaluate(dead, context);

        Set<Integer> finalMembers = new HashSet<Integer>();
        finalMembers.addAll(enrolledPatients.getMemberIds());
        finalMembers.addAll(tiPatients.getMemberIds());
        finalMembers.removeAll(toPatients.getMemberIds());
        finalMembers.removeAll(deadPatients.getMemberIds());

        currentlyInCare.setMemberIds(finalMembers);

        return new EvaluatedCohort(currentlyInCare, cohortDefinition, context);
    }
}
