package org.openmrs.module.amrsreports.reporting.data.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.AmrsReportsConstants;
import org.openmrs.module.amrsreports.MOHFacility;
import org.openmrs.module.amrsreports.reporting.data.FirstEncounterAtFacilityDataDefinition;
import org.openmrs.module.amrsreports.service.MohCoreService;
import org.openmrs.module.reporting.common.ListMap;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.dataset.query.service.DataSetQueryService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Handler for last encounter at facility
 */
@Handler(supports = FirstEncounterAtFacilityDataDefinition.class, order = 50)
public class FirstEncounterAtFacilityDataEvaluator extends DrugStartStopDataEvaluator {

	private final Log log = LogFactory.getLog(getClass());

    @Override
    public EvaluatedPersonData evaluate(final PersonDataDefinition definition, final EvaluationContext context) throws EvaluationException {
        EvaluatedPersonData data = new EvaluatedPersonData(definition, context);

        if (context.getBaseCohort().isEmpty())
            return data;

        String hql = "select patient.patientId, min(nullif(encounterDatetime,'0000-00-00 00:00:00')) " +
                "	from Encounter" +
                " 	where voided = false " +
                "   	and patient.patientId in (:patientIds)" +
                "   	GROUP BY patient.patientId " ;

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("patientIds", context.getBaseCohort());

        ListMap<Integer, Date> mappedStartDates = makeDatesMapFromHQL(hql, m);

        for (Integer memberId : context.getBaseCohort().getMemberIds()) {

            Set<Date> startDates = safeFind(mappedStartDates, memberId);
            data.addData(memberId, startDates);
        }

        return data;
    }

    /**
     * replaces reportDate and personIds with data from private variables before generating a date map
     */
    private ListMap<Integer, Date> makeDatesMapFromHQL(final String query, final Map<String, Object> substitutions) {
        MohCoreService mcs = Context.getService(MohCoreService.class);
        List<Object> queryResult = mcs.executeScrollingHqlQuery(query, substitutions);

        ListMap<Integer, Date> dateListMap = new ListMap<Integer, Date>();
        for (Object o : queryResult) {
            Object[] parts = (Object[]) o;
            if (parts.length == 2) {
                Integer pId = (Integer) parts[0];
                Date date = null;
                try{

                    date = (Date) parts[1];
                    dateListMap.putInList(pId, date);
                } catch (Exception e){

                    dateListMap.putInList(pId, date);
                }

            }
        }

        return dateListMap;
    }
}
