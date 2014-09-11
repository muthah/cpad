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
import org.openmrs.Program;
import org.openmrs.api.PatientSetService;
import org.openmrs.module.reporting.cohort.definition.*;
import org.openmrs.module.reporting.common.DurationUnit;
import org.openmrs.module.reporting.common.RangeComparator;
import org.openmrs.module.reporting.common.SetComparator;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;


/**
 * Library of common cohort definitions
 */
@Component
public class PatientMgtCohortLibrary {

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
	public CohortDefinition agedAtMostInYears(int maxAge) {
		AgeCohortDefinition cd = new AgeCohortDefinition();
		cd.setName("aged at most");
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.setMaxAge(maxAge);
		return cd;
	}

    public CohortDefinition agedAtMostInMonths(int maxAge) {
        AgeCohortDefinition cd = new AgeCohortDefinition();
        cd.setName("aged at most");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.setMaxAge(maxAge);
        cd.setMaxAgeUnit(DurationUnit.MONTHS);
        return cd;
    }

    /**
     * Patients who are between x and y years old on ${effectiveDate}
     * @return the cohort definition
     */
    public CohortDefinition ageRangeInYears(int minAge,int maxAge) {
        AgeCohortDefinition cd = new AgeCohortDefinition();
        cd.setName("aged Between");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.setMaxAge(maxAge);
        cd.setMinAge(minAge);
        return cd;
    }

    public CohortDefinition ageRangeInMonths(int minAge,int maxAge) {
        AgeCohortDefinition cd = new AgeCohortDefinition();
        cd.setName("aged Between");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.setMaxAge(maxAge);
        cd.setMinAge(minAge);
        cd.setMinAgeUnit(DurationUnit.MONTHS);
        cd.setMaxAgeUnit(DurationUnit.MONTHS);
        return cd;
    }

	/**
	 * Patients who are at least minAge years old on ${effectiveDate}
	 * @return the cohort definition
	 */
	public CohortDefinition agedAtLeastInYears(int minAge) {
		AgeCohortDefinition cd = new AgeCohortDefinition();
		cd.setName("aged at least");
		cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
		cd.setMinAge(minAge);
		return cd;
	}

    public CohortDefinition agedAtLeastInMonths(int minAge) {
        AgeCohortDefinition cd = new AgeCohortDefinition();
        cd.setName("aged at least");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.setMinAgeUnit(DurationUnit.MONTHS);
        cd.setMinAge(minAge);
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

    public CohortDefinition hasNumericObs(Concept question, Double value1,Double value2) {

        NumericObsCohortDefinition cd = new NumericObsCohortDefinition();
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.setQuestion(question);
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));

        cd.setOperator1(RangeComparator.LESS_THAN);
        cd.setValue1(value1);

        return cd;
    }


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
}