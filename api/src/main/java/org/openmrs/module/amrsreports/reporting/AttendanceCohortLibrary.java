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
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.attendanceSummaries.AttendanceSummariesSQLCohortLibrary;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.preventionWithPositive.PreventionWithPositiveSQLCohortLibrary;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.springframework.stereotype.Component;

/**
 * Library of ART related cohort definitions
 */
@Component
public class AttendanceCohortLibrary {


	private AttendanceSummariesSQLCohortLibrary commonSQLLib = new AttendanceSummariesSQLCohortLibrary();
    private BaseSQLCohortLibrary baseSQLCohortLibrary = new BaseSQLCohortLibrary();

	/*
	* Number of new Patients enrolled into HIV care
	*
	* */

    public CohortDefinition malesBelow15NewEnrollment(Integer maxAge){

        return baseSQLCohortLibrary.compositionMaxAgeCohort(maxAge,commonSQLLib.malesNewlyEnrolledOnCare());
    }

    public CohortDefinition malesAbove15NewEnrollment(Integer minAge){

        return baseSQLCohortLibrary.compositionMinAgeCohort(minAge,commonSQLLib.malesNewlyEnrolledOnCare());
    }

    public CohortDefinition femalesBelow15NewEnrollment(Integer maxAge){

        return baseSQLCohortLibrary.compositionMaxAgeCohort(maxAge,commonSQLLib.femalesNewlyEnrolledOnCare());
    }

    public CohortDefinition femalesAbove15NewEnrollment(Integer minAge){

        return baseSQLCohortLibrary.compositionMinAgeCohort(minAge,commonSQLLib.femalesNewlyEnrolledOnCare());
    }

   /*
	* Patients with revisits
	*
	* */

    public CohortDefinition malesBelow15WithRevisits(Integer maxAge){

        return baseSQLCohortLibrary.compositionMaxAgeCohort(maxAge,commonSQLLib.malesWithRevisits());
    }

    public CohortDefinition malesAbove15WithRevisits(Integer minAge){

        return baseSQLCohortLibrary.compositionMinAgeCohort(minAge,commonSQLLib.malesWithRevisits());
    }

    public CohortDefinition femalesBelow15WithRevisits(Integer maxAge){

        return baseSQLCohortLibrary.compositionMaxAgeCohort(maxAge,commonSQLLib.femalesWithRevisits());
    }

    public CohortDefinition femalesAbove15WithRevisits(Integer minAge){

        return baseSQLCohortLibrary.compositionMinAgeCohort(minAge,commonSQLLib.femalesWithRevisits());
    }



}