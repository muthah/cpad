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

import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.BaseSQLCohortLibrary;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.tbScreening.TBScreeningSQLCohortLibrary;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.springframework.stereotype.Component;

/**
 * Library of ART related cohort definitions
 */
@Component
public class TBScreeningCohortLibrary {


	private TBScreeningSQLCohortLibrary commonSQLLib = new TBScreeningSQLCohortLibrary();
    private BaseSQLCohortLibrary baseSQLCohortLibrary = new BaseSQLCohortLibrary();

	/*
	* Number of new Patients enrolled into HIV care
	*
	* */

    public CohortDefinition malePatientsEnrolledInCareBetweenDatesQry(Integer minAge, Integer maxAge){

        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.malePatientsEnrolledInCareBetweenDatesQry());
    }

    public CohortDefinition femalePatientsEnrolledInCareBetweenDatesQry(Integer minAge, Integer maxAge){

        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.femalePatientsEnrolledInCareBetweenDatesQry());
    }


    /*
    * TB Screening
    * */
    public CohortDefinition malePatientsWhoHadTBScreeningQry(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.malePatientsWhoHadTBScreeningQry());
    }

    public CohortDefinition femalePatientsWhoHadTBScreeningQry(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.femalePatientsWhoHadTBScreeningQry());
    }


    /*
   * TB Screening with positive results
   * */
    public CohortDefinition malePatientsWhoHadTBScreeningWithPositiveResults(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.malePatientsWhoHadTBScreeningWithPositiveResults());
    }

    public CohortDefinition femalePatientsWhoHadTBScreeningWithPositiveResults(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.femalePatientsWhoHadTBScreeningWithPositiveResults());
    }

    /*
 * TB Screening with positive results and started on TB Drugs
 * */
    public CohortDefinition malePatientsWithPositiveResultsStartedOnTBDrugs(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.malePatientsWithPositiveResultsStartedOnTBDrugs());
    }

    public CohortDefinition femalePatientsWithPositiveResultsStartedOnTBDrugs(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.femalePatientsWithPositiveResultsStartedOnTBDrugs());
    }

    /*
* TB Screening with positive results and started on TB Drugs
* */
    public CohortDefinition malePatientsOnTBDrugsRegardlessOfResults(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.malePatientsOnTBDrugsRegardlessOfResults());
    }

    public CohortDefinition femalePatientsOnTBDrugsRegardlessOfResults(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.femalePatientsOnTBDrugsRegardlessOfResults());
    }

    /*
* Patients already On TB treatment at enrollment to HIV Care
* */
    public CohortDefinition malePatientsOnTBTreatmentAtEnrollmentToHIVCare(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.malePatientsOnTBTreatmentAtEnrollmentToHIVCare());
    }

    public CohortDefinition femalePatientsOnTBTreatmentAtEnrollmentToHIVCare(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.femalePatientsOnTBTreatmentAtEnrollmentToHIVCare());
    }


    /*
* Patients enrolled in HIV care before reporting quarter but were diagnosed
* and started on treatment during the quarter
* regardless of screening status
* */
    public CohortDefinition malePatientsEnrolledB4ButStartedTduringQuarter(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.malePatientsEnrolledB4ButStartedTduringQuarter());
    }

    public CohortDefinition femalePatientsEnrolledB4ButStartedTDuringQuarter(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.femalePatientsEnrolledB4ButStartedTDuringQuarter());
    }

    /*
* Patients newly enrolled in HIV care, diagnosed with TB a
* and started on treatment during the quarter
* and also put on Cotrimoxazole
* */
    public CohortDefinition malePatientsEnrolledTBCotrimoxazole(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.malePatientsEnrolledTBCotrimoxazole());
    }

    public CohortDefinition femalePatientsEnrolledTBCotrimoxazole(Integer minAge, Integer maxAge){
        return baseSQLCohortLibrary.compositionAgeCohort(minAge,maxAge,commonSQLLib.femalePatientsEnrolledTBCotrimoxazole());
    }
}