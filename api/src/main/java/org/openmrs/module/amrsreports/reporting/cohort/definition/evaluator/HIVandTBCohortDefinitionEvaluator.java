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
 * Evaluator for Dead Patients Cohort Definition
 */
@Handler(supports = {HIVandTBCohortDefinition.class})
public class HIVandTBCohortDefinitionEvaluator implements CohortDefinitionEvaluator {
    /**
     * in care = enrolled in care + transfer In - Transfer Out - Dead - LFTU
     */

    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

        HIVandTBCohortDefinition definition = (HIVandTBCohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

        Cohort hivAndTbCohort = new Cohort();

        EnrolledInCareCohortDefinition enrolled = new EnrolledInCareCohortDefinition();
        TBCohortDefinition tb = new TBCohortDefinition();

        //add params
        enrolled.addParameter(new Parameter("startDate", "Report Date", Date.class));
        enrolled.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        tb.addParameter(new Parameter("startDate", "Report Date", Date.class));
        tb.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));


		context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));



        Cohort enrolledPatients = Context.getService(CohortDefinitionService.class).evaluate(enrolled, context);
        Cohort tbPatients = Context.getService(CohortDefinitionService.class).evaluate(tb, context);

        Set<Integer> finalMembers = new HashSet<Integer>();
        Set<Integer> tbMembers = tbPatients.getMemberIds();
        Set<Integer> patientInCare = enrolledPatients.getMemberIds();

        for(Integer id: tbMembers){
            if(patientInCare.contains(id)){
                finalMembers.add(id);
            }
        }
        hivAndTbCohort.setMemberIds(finalMembers);

        return new EvaluatedCohort(hivAndTbCohort, cohortDefinition, context);
    }
}
