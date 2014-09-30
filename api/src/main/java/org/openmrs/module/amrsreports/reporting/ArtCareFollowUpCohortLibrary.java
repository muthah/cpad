/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.amrsreports.reporting;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.api.PatientSetService;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsreports.cache.MetadataUtils;
import org.openmrs.module.amrsreports.cache.MohCacheUtils;
import org.openmrs.module.amrsreports.rule.MohEvaluableNameConstants;
import org.openmrs.module.reporting.cohort.definition.CodedObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.common.SetComparator;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Library of ART care follow up related cohort definitions
 */
@Component
public class ArtCareFollowUpCohortLibrary {


	private CommonICAPCohortLibrary commonCohorts = new CommonICAPCohortLibrary();

    /**
     * Patients who have stopped from care
     * @return the cohort definition
     */
    public CohortDefinition stoppedARTCohort() {
        Concept artStopDate = Context.getConceptService().getConcept(160739);
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("Stopped ART Care");
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.setQuestion(artStopDate);
        return cd;

    }

    /**
     * Patients who have transferred out from care
     * @return the cohort definition
     */
    public CohortDefinition transferOutCohort() {
        Concept transferOut = Context.getConceptService().getConcept(160649);
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("Transfered out of Care");
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.setQuestion(transferOut);
        return cd;

    }

    /**
     * Patients who have transferred out from care
     * @return the cohort definition
     */
    public CohortDefinition deceasedCohort() {
        Concept dateOfDeath = Context.getConceptService().getConcept(1543);
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("Transfered out of Care");
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.setQuestion(dateOfDeath);
        return cd;

    }

    //====================================================
    /**
     * Males above a given limit of age at a facility
     */
    public CohortDefinition malesAboveAgeWithConcepts(int minAge,Concept question, Concept... answers){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("HasObs",ReportUtils.map(commonCohorts.hasObs(question, answers),"onOrBefore=${onOrBefore},onOrAfter=${effectiveDate}"));
        cd.addSearch("malesAtFacility",ReportUtils.map(commonCohorts.malesAgedAtLeastAtFacility(minAge),"effectiveDate=${onOrBefore},locationList=${locationList},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("HasObs AND malesAtFacility");
        return cd;
    }

    /**
     * Males Below a given limit of age at a facility
     */
    public CohortDefinition malesBelowAgeWithConcepts(int maxAge,Concept question, Concept... answers){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("HasObs",ReportUtils.map(commonCohorts.hasObs(question, answers),"onOrBefore=${onOrBefore},onOrAfter=${effectiveDate}"));
        cd.addSearch("malesAtFacility",ReportUtils.map(commonCohorts.malesAgedAtMostAtFacility(maxAge),"effectiveDate=${effectiveDate},locationList=${locationList},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("HasObs AND malesAtFacility");
        return cd;
    }

    /**
     * Males with a given range of age at a facility
     */
    public CohortDefinition malesBetweenAgeWithConcepts(int minAge,int maxAge,Concept question, Concept... answers){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("HasObs",ReportUtils.map(commonCohorts.hasObs(question, answers),"onOrBefore=${onOrBefore},onOrAfter=${effectiveDate}"));
        cd.addSearch("malesAtFacility",ReportUtils.map(commonCohorts.malesAgedBetweenAtFacility(minAge, maxAge),"effectiveDate=${effectiveDate},locationList=${locationList},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("HasObs AND malesAtFacility");
        return cd;
    }

    //---------------------------------------------------------------------
    /**
     * Females with a minimum age limit at a facility
     */
    public CohortDefinition femalesAboveAgeWithConcepts(int minAge,Concept question, Concept... answers){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("HasObs",ReportUtils.map(commonCohorts.hasObs(question, answers),"onOrBefore=${onOrBefore},onOrAfter=${effectiveDate}"));
        cd.addSearch("femalesAtFacility",ReportUtils.map(commonCohorts.femalesAgedAtLeastAtFacility(minAge),"effectiveDate=${effectiveDate},locationList=${locationList},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("HasObs AND femalesAtFacility");
        return cd;
    }

    /**
     * Females below a given age limit at a facility
     */
    public CohortDefinition femalesBelowAgeWithConcepts(int maxAge,Concept question, Concept... answers){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("HasObs",ReportUtils.map(commonCohorts.hasObs(question, answers),"onOrBefore=${onOrBefore},onOrAfter=${effectiveDate}"));
        cd.addSearch("femalesAtFacility",ReportUtils.map(commonCohorts.femalesAgedAtMostAtFacility(maxAge),"effectiveDate=${effectiveDate},locationList=${locationList},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("HasObs AND femalesAtFacility");
        return cd;
    }

    /**
     * Females of a given range of age at a facility
     */
    public CohortDefinition femalesBetweenAgeWithConcepts(int minAge,int maxAge,Concept question, Concept... answers){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("HasObs",ReportUtils.map(commonCohorts.hasObs(question, answers),"onOrBefore=${onOrBefore},onOrAfter=${effectiveDate}"));
        cd.addSearch("femalesAtFacility",ReportUtils.map(commonCohorts.femalesAgedBetweenAtFacility(minAge, maxAge),"effectiveDate=${effectiveDate},locationList=${locationList},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("HasObs AND femalesAtFacility");
        return cd;
    }





    //====================================================
    /**
     * Patients who have died
     * @return the cohort definition
     */
    public CohortDefinition causeOfDeath() {

        Concept concept = MohCacheUtils.getConcept(MohEvaluableNameConstants.CAUSE_FOR_DEATH);
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("Cause of Death Cohort Definition");
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.setQuestion(concept);
        return cd;
    }

    /**
     * Patients who have died
     * @return the cohort definition
     */
    public CohortDefinition deathReportedBy() {

        Concept concept = MohCacheUtils.getConcept(MohEvaluableNameConstants.DEATH_REPORTED_BY);
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("Cause of Death Cohort Definition");
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.setQuestion(concept);
        return cd;
    }

    /**
     * Patients who have died
     * @return the cohort definition
     */
    public CohortDefinition dateOfDeath() {

        Concept concept = MohCacheUtils.getConcept(MohEvaluableNameConstants.DATE_OF_DEATH);
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("Date of Death Cohort Definition");
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.setQuestion(concept);
        return cd;
    }

    /**
     * Patients who have died
     * @return the cohort definition
     */
    public CohortDefinition tbTreatmentOutcome() {

        Concept concept = MohCacheUtils.getConcept(MohEvaluableNameConstants.OUTCOME_AT_END_OF_TUBERCULOSIS_TREATMENT);
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("Date of Death Cohort Definition");
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.addValue(MohCacheUtils.getConcept(MohEvaluableNameConstants.DEAD));
        cd.setQuestion(concept);
        return cd;
    }
    /**
     * Patients who have died
     * @return the cohort definition
     */
    public CohortDefinition reasonForMissedVisit() {

        Concept concept = MohCacheUtils.getConcept(MohEvaluableNameConstants.REASON_FOR_MISSED_VISIT);
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("Date of Death Cohort Definition");
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.addValue(MohCacheUtils.getConcept(MohEvaluableNameConstants.DEAD));
        cd.setQuestion(concept);
        return cd;
    }

    /**
     * Patients who have died
     * @return the cohort definition
     */
    public CohortDefinition reasonExitedCare() {

        Concept concept = MohCacheUtils.getConcept(MohEvaluableNameConstants.REASON_EXITED_CARE);
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("Date of Death Cohort Definition");
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.addValue(MohCacheUtils.getConcept(MohEvaluableNameConstants.PATIENT_DIED));
        cd.setQuestion(concept);
        return cd;
    }

    /**
     * composition cohort for dead from death reporting
     * @return the cohort definition
     */
    public CohortDefinition deathReporting() {

        CompositionCohortDefinition ccd = new CompositionCohortDefinition();
        ccd.setName("Death Reporting Composition Cohort");
        ccd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        ccd.addSearch("causeOfDeath", ReportUtils.map(causeOfDeath(), "onOrBefore=${onOrBefore}"));
        ccd.addSearch("deathReportedBy", ReportUtils.map(deathReportedBy(), "onOrBefore=${onOrBefore}"));
        ccd.addSearch("dateOfDeath", ReportUtils.map(dateOfDeath(), "onOrBefore=${onOrBefore}"));
        ccd.setCompositionString("OR");
        return ccd;
    }

    public CohortDefinition dead(){
        CompositionCohortDefinition ccd = new CompositionCohortDefinition();
        ccd.setName("Dead Patients Cohort Definition");
        ccd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        ccd.addSearch("deathReporting",ReportUtils.map(deathReporting(),"onOrBefore=${onOrBefore}"));
        ccd.addSearch("reasonExitedCare",ReportUtils.map(reasonExitedCare(),"onOrBefore=${onOrBefore}"));
        ccd.addSearch("reasonForMissedVisit",ReportUtils.map(reasonForMissedVisit(),"onOrBefore=${onOrBefore}"));
        ccd.addSearch("tbTreatmentOutcome",ReportUtils.map(tbTreatmentOutcome(),"onOrBefore=${onOrBefore}"));
        ccd.setCompositionString("OR");
        return ccd;
    }


    public CohortDefinition transferCareToOtherCentre() {
        CodedObsCohortDefinition transferCareToOtherCentre = new CodedObsCohortDefinition();
        transferCareToOtherCentre.setName("Transfer Care to other center");
        transferCareToOtherCentre.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        transferCareToOtherCentre.setTimeModifier(PatientSetService.TimeModifier.ANY);
        transferCareToOtherCentre.setQuestion(MohCacheUtils.getConcept(MohEvaluableNameConstants.TRANSFER_CARE_TO_OTHER_CENTER));
        return transferCareToOtherCentre;
    }

    public CohortDefinition exitCareDueToTO() {
        CodedObsCohortDefinition reasonExitedCare = new CodedObsCohortDefinition();
        reasonExitedCare.setName("Reason exited care");
        reasonExitedCare.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        reasonExitedCare.setTimeModifier(PatientSetService.TimeModifier.ANY);
        reasonExitedCare.setQuestion(MohCacheUtils.getConcept(MohEvaluableNameConstants.REASON_EXITED_CARE));
        reasonExitedCare.addValue(MohCacheUtils.getConcept(MohEvaluableNameConstants.PATIENT_TRANSFERRED_OUT));
        return reasonExitedCare;
    }

    public CohortDefinition tbTreatmentDefaulter() {
        CodedObsCohortDefinition tbTreatmentOutcome = new CodedObsCohortDefinition();
        tbTreatmentOutcome.setName("Outcome of TB Treatment");
        tbTreatmentOutcome.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        tbTreatmentOutcome.setTimeModifier(PatientSetService.TimeModifier.ANY);
        tbTreatmentOutcome.setQuestion(MohCacheUtils.getConcept(MohEvaluableNameConstants.OUTCOME_AT_END_OF_TUBERCULOSIS_TREATMENT));
        tbTreatmentOutcome.addValue(MohCacheUtils.getConcept(MohEvaluableNameConstants.PATIENT_DEFAULTED));
        return tbTreatmentOutcome;
    }

    public CohortDefinition noPlanToComeToClinic(){
        CodedObsCohortDefinition planTocomeToClinic = new CodedObsCohortDefinition();
        planTocomeToClinic.setName("Plan to return to clinic");
        planTocomeToClinic.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        planTocomeToClinic.setTimeModifier(PatientSetService.TimeModifier.ANY);
        planTocomeToClinic.setQuestion(MohCacheUtils.getConcept(MohEvaluableNameConstants.PLAN_TO_RETURN_TO_CLINIC));
        planTocomeToClinic.addValue(MohCacheUtils.getConcept(MohEvaluableNameConstants.NO));
        return planTocomeToClinic;
    }
    /**
     * Patients who have transferred out to other clinics
     * @return the cohort definition
     * ampath concepts are 1285, 1596-1594, 6206-1595, 1579-1066
     */
    public CohortDefinition transferOut() {

        CompositionCohortDefinition ccd = new CompositionCohortDefinition();
        ccd.setName("Transfer In Cohort Definition");
        ccd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        ccd.addSearch("ToToOtherCenter",ReportUtils.map(transferCareToOtherCentre(),"onOrBefore=${onOrBefore}"));
        ccd.addSearch("exitCareDueToTO",ReportUtils.map(exitCareDueToTO(),"onOrBefore=${onOrBefore}"));
        ccd.addSearch("tbTreatmentDefaulter",ReportUtils.map(tbTreatmentDefaulter(),"onOrBefore=${onOrBefore}"));
        ccd.addSearch("noPlanToComeToClinic",ReportUtils.map(noPlanToComeToClinic(),"onOrBefore=${onOrBefore}"));
        ccd.setCompositionString("OR");
        return ccd;

    }

	/**
	 * Patients referred from the given entry point onto the HIV program
	 * @param entryPoints the entry point concepts
	 * @return the cohort definition
	 */
	public CohortDefinition referredFrom(Concept... entryPoints) {
		EncounterType hivEnrollEncType = MetadataUtils.getEncounterType(MohEvaluableNameConstants.ADMITTED_TO_HOSPITAL/*Metadata.EncounterType.HIV_ENROLLMENT*/);
		Concept methodOfEnrollment = MohCacheUtils.getConcept(MohEvaluableNameConstants.ADMITTED_TO_HOSPITAL);//Dictionary.getConcept(Dictionary.METHOD_OF_ENROLLMENT);

		CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
		cd.setName("entered care between dates");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
		cd.setQuestion(methodOfEnrollment);
		cd.setValueList(Arrays.asList(entryPoints));
		cd.setOperator(SetComparator.IN);
		cd.setEncounterTypeList(Collections.singletonList(hivEnrollEncType));
		return cd;

	}

	/**
	 * Patients referred from the given entry point onto the HIV program
	 * @param entryPoints the entry point concepts
	 * @return the cohort definition
	 */
	public CohortDefinition referredNotFrom(Concept... entryPoints) {
		EncounterType hivEnrollEncType = MetadataUtils.getEncounterType(MohEvaluableNameConstants.ADMITTED_TO_HOSPITAL/*Metadata.EncounterType.HIV_ENROLLMENT*/);
		Concept methodOfEnrollment = MohCacheUtils.getConcept(MohEvaluableNameConstants.ADMITTED_TO_HOSPITAL);//Dictionary.getConcept(Dictionary.METHOD_OF_ENROLLMENT);

		CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
		cd.setName("entered care between dates");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
		cd.setQuestion(methodOfEnrollment);
		cd.setValueList(Arrays.asList(entryPoints));
		cd.setOperator(SetComparator.NOT_IN);
		cd.setEncounterTypeList(Collections.singletonList(hivEnrollEncType));
		return cd;
	}

	/**
	 * Patients who were enrolled in HIV care (including transfers) between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	public CohortDefinition enrolled() {
		return commonCohorts.enrolled(MetadataUtils.getProgram(MohEvaluableNameConstants.ADMITTED_TO_HOSPITAL/*Metadata.Program.HIV*/));
	}
}