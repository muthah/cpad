package org.openmrs.module.amrsreports.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CurrentlyInCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CurrentlyOnARTCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.StartedARTCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.StoppedARTCareCohortDefinition;
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
@Handler(supports = {CurrentlyOnARTCohortDefinition.class})
public class OnARTCohortDefinitionEvaluator implements CohortDefinitionEvaluator {
    /**
     * On ART  = Currently in care and (started art - stopped)
     */

    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

        CurrentlyOnARTCohortDefinition definition = (CurrentlyOnARTCohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

        Cohort currentlyInOnART = new Cohort();

        CurrentlyInCareCohortDefinition inCare = new CurrentlyInCareCohortDefinition();
        StartedARTCareCohortDefinition startedART = new StartedARTCareCohortDefinition();
        StoppedARTCareCohortDefinition stoppedART = new StoppedARTCareCohortDefinition();

        //add params
        inCare.addParameter(new Parameter("startDate", "Report Date", Date.class));
        inCare.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        startedART.addParameter(new Parameter("startDate", "Report Date", Date.class));
        startedART.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        stoppedART.addParameter(new Parameter("startDate", "Report Date", Date.class));
        stoppedART.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

		context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));

        Cohort inCarePatients = Context.getService(CohortDefinitionService.class).evaluate(inCare, context);
        Cohort onARTPatients = Context.getService(CohortDefinitionService.class).evaluate(startedART, context);
        Cohort stoppedARTPatients = Context.getService(CohortDefinitionService.class).evaluate(stoppedART, context);

        Set<Integer> finalMembers = new HashSet<Integer>();

        for(Integer id:onARTPatients.getMemberIds()){
            if(inCarePatients.getMemberIds().contains(id)){
                finalMembers.add(id);
            }
        }

        finalMembers.removeAll(stoppedARTPatients.getMemberIds());

        currentlyInOnART.setMemberIds(finalMembers);

        return new EvaluatedCohort(currentlyInOnART, cohortDefinition, context);
    }
}
