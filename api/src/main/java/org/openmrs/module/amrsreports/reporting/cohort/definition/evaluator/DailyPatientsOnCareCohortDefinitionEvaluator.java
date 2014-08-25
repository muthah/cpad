package org.openmrs.module.amrsreports.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.cohort.definition.*;
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
@Handler(supports = {DailyPatientsOnCareCohortDefinition.class})
public class DailyPatientsOnCareCohortDefinitionEvaluator implements CohortDefinitionEvaluator {


    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

        DailyPatientsOnCareCohortDefinition definition = (DailyPatientsOnCareCohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

        Cohort onCare = new Cohort();

        OnARTCohortDefinition onART = new OnARTCohortDefinition();
        PatientsOnADayCohortDefinition all = new PatientsOnADayCohortDefinition();

        //add params
        all.addParameter(new Parameter("startDate", "Report Date", Date.class));
        all.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        onART.addParameter(new Parameter("startDate", "Report Date", Date.class));
        onART.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));

        Cohort allPatients = Context.getService(CohortDefinitionService.class).evaluate(all, context);
        Cohort onArtPatients = Context.getService(CohortDefinitionService.class).evaluate(onART, context);

        Set<Integer> finalMembers = new HashSet<Integer>();

        for(Integer id:allPatients.getMemberIds()){
            if(!onArtPatients.getMemberIds().contains(id)){
                finalMembers.add(id);
            }
        }

        onCare.setMemberIds(finalMembers);

        return new EvaluatedCohort(onCare, cohortDefinition, context);
    }
}
