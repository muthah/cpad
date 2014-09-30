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
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.artRegimens.RegimensSQLCohortLibrary;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.springframework.stereotype.Component;

/**
 * Library of Regimens
 */
@Component
public class RegimensCohortLibrary {


	private BaseSQLCohortLibrary baseCohorts = new BaseSQLCohortLibrary();
    private RegimensSQLCohortLibrary sqlDefinitions = new RegimensSQLCohortLibrary();

    //Define Children Cohorts

    public CohortDefinition childrenOn10ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn10A());
    public CohortDefinition childrenOn10BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn10B());
    public CohortDefinition childrenOn11BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn11B());
    public CohortDefinition childrenOn1BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn1B());
    public CohortDefinition childrenOn2BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn2B());
    public CohortDefinition childrenOn4ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn4A());
    public CohortDefinition childrenOn4BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn4B());
    public CohortDefinition childrenOn5ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn5A());
    public CohortDefinition childrenOn5BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn5B());
    public CohortDefinition childrenOn6ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn6A());
    public CohortDefinition childrenOn6BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn6B());
    public CohortDefinition childrenOn7ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn7A());
    public CohortDefinition childrenOn7BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn7B());
    public CohortDefinition childrenOn9Cohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOn9());
    public CohortDefinition childrenOnAF1ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAF1A());
    public CohortDefinition childrenOnAF1BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAF1B());
    public CohortDefinition childrenOnAF1CCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAF1C());
    public CohortDefinition childrenOnAF2ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAF2A());
    public CohortDefinition childrenOnAF2BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAF2B());
    public CohortDefinition childrenOnAF2CCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAF2C());
    public CohortDefinition childrenOnAF3ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAF3A());
    public CohortDefinition childrenOnAF3BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAF3B());
    public CohortDefinition childrenOnAF3CCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAF3C());
    public CohortDefinition childrenOnAS1ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAS1A());
    public CohortDefinition childrenOnAS1BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAS1B());
    public CohortDefinition childrenOnAS2ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAS2A());
    public CohortDefinition childrenOnAS2BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAS2B());
    public CohortDefinition childrenOnAS2CCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAS2C());
    public CohortDefinition childrenOnAS3ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAS3A());
    public CohortDefinition childrenOnAS4ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnAS4A());
    public CohortDefinition childrenOnB3BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnB3B());
    public CohortDefinition childrenOnB3CCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnB3C());
    public CohortDefinition childrenOnB3LCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnB3L());
    public CohortDefinition childrenOnC1BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC1B());
    public CohortDefinition childrenOnC1CCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC1C());
    public CohortDefinition childrenOnC1DCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC1D());
    public CohortDefinition childrenOnC2CCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC2C());
    public CohortDefinition childrenOnC2DCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC2D());
    public CohortDefinition childrenOnC3CCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC3C());
    public CohortDefinition childrenOnC3DCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC3D());
    public CohortDefinition childrenOnC4BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC4B());
    public CohortDefinition childrenOnC4CCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC4C());
    public CohortDefinition childrenOnC4DCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC4D());
    public CohortDefinition childrenOnC5ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsONC5A());
    public CohortDefinition childrenOnC5BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC5B());
    public CohortDefinition childrenOnC6ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC6A());
    public CohortDefinition childrenOnC6BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC6B());
    public CohortDefinition childrenOnC7BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnC7B());
    public CohortDefinition childrenOnCF1ACohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnCF1A());
    public CohortDefinition childrenOnCF1BCohort = baseCohorts.compositionMaxAgeCohort(14,sqlDefinitions.patientsOnCF1B());


    //Define cohorts for adults
    public CohortDefinition adultsOn10ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn10A());
    public CohortDefinition adultsOn10BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn10B());
    public CohortDefinition adultsOn11BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn11B());
    public CohortDefinition adultsOn1BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn1B());
    public CohortDefinition adultsOn2BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn2B());
    public CohortDefinition adultsOn4ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn4A());
    public CohortDefinition adultsOn4BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn4B());
    public CohortDefinition adultsOn5ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn5A());
    public CohortDefinition adultsOn5BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn5B());
    public CohortDefinition adultsOn6ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn6A());
    public CohortDefinition adultsOn6BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn6B());
    public CohortDefinition adultsOn7ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn7A());
    public CohortDefinition adultsOn7BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn7B());
    public CohortDefinition adultsOn9Cohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOn9());
    public CohortDefinition adultsOnAF1ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAF1A());
    public CohortDefinition adultsOnAF1BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAF1B());
    public CohortDefinition adultsOnAF1CCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAF1C());
    public CohortDefinition adultsOnAF2ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAF2A());
    public CohortDefinition adultsOnAF2BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAF2B());
    public CohortDefinition adultsOnAF2CCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAF2C());
    public CohortDefinition adultsOnAF3ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAF3A());
    public CohortDefinition adultsOnAF3BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAF3B());
    public CohortDefinition adultsOnAF3CCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAF3C());
    public CohortDefinition adultsOnAS1ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAS1A());
    public CohortDefinition adultsOnAS1BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAS1B());
    public CohortDefinition adultsOnAS2ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAS2A());
    public CohortDefinition adultsOnAS2BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAS2B());
    public CohortDefinition adultsOnAS2CCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAS2C());
    public CohortDefinition adultsOnAS3ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAS3A());
    public CohortDefinition adultsOnAS4ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnAS4A());
    public CohortDefinition adultsOnB3BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnB3B());
    public CohortDefinition adultsOnB3CCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnB3C());
    public CohortDefinition adultsOnB3LCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnB3L());
    public CohortDefinition adultsOnC1BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC1B());
    public CohortDefinition adultsOnC1CCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC1C());
    public CohortDefinition adultsOnC1DCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC1D());
    public CohortDefinition adultsOnC2CCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC2C());
    public CohortDefinition adultsOnC2DCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC2D());
    public CohortDefinition adultsOnC3CCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC3C());
    public CohortDefinition adultsOnC3DCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC3D());
    public CohortDefinition adultsOnC4BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC4B());
    public CohortDefinition adultsOnC4CCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC4C());
    public CohortDefinition adultsOnC4DCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC4D());
    public CohortDefinition adultsOnC5ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsONC5A());
    public CohortDefinition adultsOnC5BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC5B());
    public CohortDefinition adultsOnC6ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC6A());
    public CohortDefinition adultsOnC6BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC6B());
    public CohortDefinition adultsOnC7BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnC7B());
    public CohortDefinition adultsOnCF1ACohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnCF1A());
    public CohortDefinition adultsOnCF1BCohort = baseCohorts.compositionMinAgeCohort(15,sqlDefinitions.patientsOnCF1B());



}