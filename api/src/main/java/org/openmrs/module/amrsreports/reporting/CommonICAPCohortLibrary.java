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
import org.openmrs.Program;
import org.openmrs.api.PatientSetService;
import org.openmrs.module.reporting.cohort.definition.AgeCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CodedObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.EncounterCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.GenderCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.ProgramEnrollmentCohortDefinition;
import org.openmrs.module.reporting.common.SetComparator;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;


/**
 * Library of common cohort definitions
 */
@Component
public class CommonICAPCohortLibrary {

	/**
	 * Patients who are female
	 * @return the cohort definition
	 */
	public CohortDefinition females() {
		GenderCohortDefinition cd = new GenderCohortDefinition();
		cd.setName("females");
		cd.setFemaleIncluded(true);
		return cd;
	}

	/**
	 * Patients who are male
	 * @return the cohort definition
	 */
	public CohortDefinition males() {
		GenderCohortDefinition cd = new GenderCohortDefinition();
		cd.setName("males");
		cd.setMaleIncluded(true);
		return cd;
	}

	/**
	 * Patients who at most maxAge years old on ${effectiveDate}
	 * @return the cohort definition
	 */
	public CohortDefinition agedAtMost(int maxAge) {
		AgeCohortDefinition cd = new AgeCohortDefinition();
		cd.setName("aged at most");
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.setMaxAge(maxAge);
		return cd;
	}

    /**
     * Patients who are between x and y years old on ${effectiveDate}
     * @return the cohort definition
     */
    public CohortDefinition agedBetween(int minAge,int maxAge) {
        AgeCohortDefinition cd = new AgeCohortDefinition();
        cd.setName("aged Between");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.setMaxAge(maxAge);
        cd.setMinAge(minAge);
        return cd;
    }

	/**
	 * Patients who are at least minAge years old on ${effectiveDate}
	 * @return the cohort definition
	 */
	public CohortDefinition agedAtLeast(int minAge) {
		AgeCohortDefinition cd = new AgeCohortDefinition();
		cd.setName("aged at least");
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.setMinAge(minAge);
		return cd;
	}

    /**
     * patients who had encounters at a given facility
     */
    public CohortDefinition hasFacilityEncounters() {

        EncounterCohortDefinition cd = new EncounterCohortDefinition();
        cd.setName("Has Encounters at a facility between dates");
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));

        return cd;
    }

    /**
     * ------------------------------------------------------------------------------
     *               Composition Cohort of age and Facility
     * ------------------------------------------------------------------------------
     */

    /**
     * Patients aged at least some age at a facility between dates
     */
    public CohortDefinition agedAtLeastAtFacility(int minAge){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Patients of at least some age at facility");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addSearch("agedAtLeast", ReportUtils.map(agedAtLeast(minAge), "effectiveDate=${effectiveDate}"));
        cd.addSearch("atLocation",ReportUtils.map(hasFacilityEncounters(), "locationList=${locationList},onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}"));
        cd.setCompositionString("agedAtLeast AND atLocation");
        return cd;
    }

    /**
     * Patients aged at most some age at a facility between dates
     */
    public CohortDefinition agedAtMostAtFacility(int maxAge){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Patients of at most some age at facility");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addSearch("agedAtMost", ReportUtils.map(agedAtMost(maxAge), "effectiveDate=${effectiveDate}"));
        cd.addSearch("atLocation",ReportUtils.map(hasFacilityEncounters(), "locationList=${locationList},onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}"));
        cd.setCompositionString("agedAtMost AND atLocation");
        return cd;
    }

    /**
     * Patients at a given range of age at facility between range of dates
     */
    public CohortDefinition agedBetweenAtFacility(int minAge, int maxAge){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Patients of at most some age at facility");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addSearch("rangeOfAge", ReportUtils.map(agedBetween(minAge,maxAge), "effectiveDate=${effectiveDate}"));
        cd.addSearch("atLocation",ReportUtils.map(hasFacilityEncounters(), "locationList=${locationList},onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}"));
        cd.setCompositionString("rangeOfAge AND atLocation");
        return cd;
    }


    /**
     * ------------------------------------------------------------------------------
     *               Composition Cohort of gender and ageAndFacility
     * ------------------------------------------------------------------------------
     */

    /**
     * Males aged at least some age at a facility between dates
     */
    public CohortDefinition malesAgedAtLeastAtFacility(int minAge){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Males of at least some age at facility");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addSearch("agedAtLeastAtFacility", ReportUtils.map(agedAtLeastAtFacility(minAge), "effectiveDate=${effectiveDate},locationList=${locationList},onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}"));
        cd.addSearch("males",ReportUtils.map(males()));
        cd.setCompositionString("agedAtLeastAtFacility AND males");
        return cd;
    }

    /**
     * Females aged at least some age at a facility between dates
     */
    public CohortDefinition femalesAgedAtLeastAtFacility(int minAge){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Females of at least some age at facility");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addSearch("agedAtLeastAtFacility", ReportUtils.map(agedAtLeastAtFacility(minAge), "effectiveDate=${effectiveDate},locationList=${locationList},onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}"));
        cd.addSearch("females",ReportUtils.map(females()));
        cd.setCompositionString("agedAtLeastAtFacility AND females");
        return cd;
    }

    /**
     * Males aged at most some age at a facility between dates
     */
    public CohortDefinition malesAgedAtMostAtFacility(int maxAge){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Males of at most some age at facility");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addSearch("agedAtMostAtFacility", ReportUtils.map(agedAtMostAtFacility(maxAge), "effectiveDate=${effectiveDate},locationList=${locationList},onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}"));
        cd.addSearch("males",ReportUtils.map(males()));
        cd.setCompositionString("agedAtMostAtFacility AND males");
        return cd;
    }

    /**
     * Females aged at most some age at a facility between dates
     */
    public CohortDefinition femalesAgedAtMostAtFacility(int maxAge){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Females of at least some age at facility");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addSearch("agedAtMostAtFacility", ReportUtils.map(agedAtMostAtFacility(maxAge), "effectiveDate=${effectiveDate},locationList=${locationList},onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}"));
        cd.addSearch("females",ReportUtils.map(females()));
        cd.setCompositionString("agedAtMostAtFacility AND females");
        return cd;
    }

    /**
     * Males of age range  at a facility between dates
     */
    public CohortDefinition malesAgedBetweenAtFacility(int minAge,int maxAge){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Males of age range at facility");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addSearch("agedBetweenAtFacility", ReportUtils.map(agedBetweenAtFacility(minAge,maxAge), "effectiveDate=${effectiveDate},locationList=${locationList},onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}"));
        cd.addSearch("males",ReportUtils.map(males()));
        cd.setCompositionString("agedBetweenAtFacility AND males");
        return cd;
    }

    /**
     * Females aged at most some age at a facility between dates
     */
    public CohortDefinition femalesAgedBetweenAtFacility(int minAge,int maxAge){
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Females of at least some age at facility");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addSearch("agedBetweenAtFacility", ReportUtils.map(agedBetweenAtFacility(minAge,maxAge), "effectiveDate=${effectiveDate},locationList=${locationList},onOrBefore=${onOrBefore},onOrAfter=${onOrAfter}"));
        cd.addSearch("females",ReportUtils.map(females()));
        cd.setCompositionString("agedBetweenAtFacility AND females");
        return cd;
    }

	/**
	 * Patients who have an encounter between ${onOrAfter} and ${onOrBefore}
	 * @param types the encounter types
	 * @return the cohort definition
	 */
	public CohortDefinition hasEncounter(EncounterType... types) {
		EncounterCohortDefinition cd = new EncounterCohortDefinition();
		cd.setName("has encounter between dates");
		cd.setTimeQualifier(TimeQualifier.ANY);
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		if (types.length > 0) {
			cd.setEncounterTypeList(Arrays.asList(types));
		}
		return cd;
	}

	/**
	 * Patients who have an obs between ${onOrAfter} and ${onOrBefore}
	 * @param question the question concept
	 * @param answers the answers to include
	 * @return the cohort definition
	 */
	public CohortDefinition hasObs(Concept question, Concept... answers) {
		CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
		cd.setName("has obs between dates");
		cd.setQuestion(question);
		cd.setOperator(SetComparator.IN);
		cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		if (answers.length > 0) {
			cd.setValueList(Arrays.asList(answers));
		}
		return cd;
	}

	/**
	 * Patients who transferred in between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	/*public CohortDefinition transferredIn() {
		Concept transferInDate = Dictionary.getConcept(Dictionary.TRANSFER_IN_DATE);

		DateObsValueBetweenCohortDefinition cd = new DateObsValueBetweenCohortDefinition();
		cd.setName("transferred in between dates");
		cd.setQuestion(transferInDate);
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		return cd;
	}*/

	/**
	 * Patients who transferred in between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	/*public CohortDefinition transferredOut() {
		Concept reasonForDiscontinue = Dictionary.getConcept(Dictionary.REASON_FOR_PROGRAM_DISCONTINUATION);
		Concept transferredOut = Dictionary.getConcept(Dictionary.TRANSFERRED_OUT);

		CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.setName("transferred out between dates");
		cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
		cd.setQuestion(reasonForDiscontinue);
		cd.setOperator(SetComparator.IN);
		cd.setValueList(Collections.singletonList(transferredOut));
		return cd;
	}
*/
	/**
	 * Patients who were enrolled on the given programs between ${enrolledOnOrAfter} and ${enrolledOnOrBefore}
	 * @param programs the programs
	 * @return the cohort definition
	 */
	public CohortDefinition enrolled(Program... programs) {
		ProgramEnrollmentCohortDefinition cd = new ProgramEnrollmentCohortDefinition();
		cd.setName("enrolled in program between dates");
		cd.addParameter(new Parameter("enrolledOnOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("enrolledOnOrBefore", "Before Date", Date.class));
		if (programs.length > 0) {
			cd.setPrograms(Arrays.asList(programs));
		}
		return cd;
	}

	/**
	 * Patients who were enrolled on the given programs (excluding transfers) between ${fromDate} and ${toDate}
	 * @param programs the programs
	 * @return the cohort definition
	 */
	//public CohortDefinition enrolledExcludingTransfers(Program... programs) {
	/*	CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("enrolled excluding transfers in program between dates");
		cd.addParameter(new Parameter("onOrAfter", "From Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "To Date", Date.class));
		cd.addSearch("enrolled", ReportUtils.map(enrolled(programs), "enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
		cd.addSearch("transferIn", ReportUtils.map(transferredIn(), "onOrBefore=${onOrBefore}"));
		cd.setCompositionString("enrolled AND NOT transferIn");
		return cd;
	}*/

	/**
	 * Patients who are pregnant on ${onDate}
	 * @return the cohort definition
	 */
	/*public CohortDefinition pregnant() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new OnAlternateFirstLineArtCalculation());
		cd.setName("pregnant on date");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		return cd;
	}*/

	/**
	 * Patients who are in the specified program on ${onDate}
	 * @param program the program
	 * @return
	 */
	/*public CohortDefinition inProgram(Program program) {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new InProgramCalculation());
		cd.setName("in " + program.getName() + " on date");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		cd.addCalculationParameter("program", program);
		return cd;
	}*/

	/**
	 * Patients who are on the specified medication on ${onDate}
	 * @param concepts the drug concepts
	 * @return the cohort definition
	 */
	/*public CohortDefinition onMedication(Concept... concepts) {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new OnMedicationCalculation());
		cd.setName("taking drug on date");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		cd.addCalculationParameter("drugs", new HashSet<Concept>(Arrays.asList(concepts)));
		return cd;
	}*/
}