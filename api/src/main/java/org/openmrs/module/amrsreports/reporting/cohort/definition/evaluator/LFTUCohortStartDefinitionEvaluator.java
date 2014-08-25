package org.openmrs.module.amrsreports.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatientsCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.EnrolledInCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.LFTUCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.PatientsWithRecentEncCohortDefinition;
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
@Handler(supports = {LFTUCohortDefinition.class})
public class LFTUCohortStartDefinitionEvaluator implements CohortDefinitionEvaluator {
    /**
     * LFTU = Enrolled + TI - (dead + To + recent visits + future appointments )
     * in care = enrolled in care + transfer In - Transfer Out - Dead - LFTU
     */

    private final Log log = LogFactory.getLog(this.getClass());

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

        LFTUCohortDefinition definition = (LFTUCohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

        Cohort lftu = new Cohort();

        EnrolledInCareCohortDefinition enrolled = new EnrolledInCareCohortDefinition();
        TransferINCohortDefinition ti = new TransferINCohortDefinition();
        TransferOUTCohortDefinition to = new TransferOUTCohortDefinition();
        DeadPatientsCohortDefinition dead = new DeadPatientsCohortDefinition();
        PatientsWithRecentEncCohortDefinition recentVisits = new PatientsWithRecentEncCohortDefinition();

        //add params
        enrolled.addParameter(new Parameter("startDate", "Report Date", Date.class));
        enrolled.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        ti.addParameter(new Parameter("startDate", "Report Date", Date.class));
        ti.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        to.addParameter(new Parameter("startDate", "Report Date", Date.class));
        to.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        dead.addParameter(new Parameter("startDate", "Report Date", Date.class));
        dead.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));

        recentVisits.addParameter(new Parameter("startDate", "Report Date", Date.class));
        recentVisits.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));


		context.addParameterValue("startDate", context.getParameterValue("startDate"));
        context.addParameterValue("endDate", context.getParameterValue("endDate"));



        Cohort enrolledPatients = Context.getService(CohortDefinitionService.class).evaluate(enrolled, context);
        Cohort tiPatients = Context.getService(CohortDefinitionService.class).evaluate(ti, context);
        Cohort toPatients = Context.getService(CohortDefinitionService.class).evaluate(to, context);
        Cohort deadPatients = Context.getService(CohortDefinitionService.class).evaluate(dead, context);
        Cohort recentPatients = Context.getService(CohortDefinitionService.class).evaluate(recentVisits, context);

        Set<Integer> enrolledP = enrolledPatients.getMemberIds();
        Set<Integer> tiP = tiPatients.getMemberIds();
        Set<Integer> toP = toPatients.getMemberIds();
        Set<Integer> deadP = deadPatients.getMemberIds();
        Set<Integer> recentP = recentPatients.getMemberIds();

        Set<Integer> enrolledNTi = new HashSet<Integer>(enrolledP);
        enrolledNTi.addAll(tiP);

        Set<Integer> originalCohort = enrolledNTi;

        enrolledNTi.removeAll(toP);
        enrolledNTi.removeAll(deadP);
        enrolledNTi.removeAll(recentP);

        for(Integer id: enrolledNTi){
            if(originalCohort.contains(id)){
               lftu.addMember(id);
            }
        }

        return new EvaluatedCohort(lftu, cohortDefinition, context);
    }
}
