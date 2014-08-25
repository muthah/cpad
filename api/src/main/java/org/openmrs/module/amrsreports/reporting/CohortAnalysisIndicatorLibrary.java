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

import org.openmrs.Location;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.BaseSQLCohortLibrary;
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.MOH731.MOH731SQLCohortLibrary;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Library of MOH 731 related indicator definitions. All indicators require parameters ${startDate} and ${endDate}
 */
@Component
public class CohortAnalysisIndicatorLibrary {


    private BaseSQLCohortLibrary baseCohorts = new BaseSQLCohortLibrary();
    private MOH731SQLCohortLibrary sqlQueries = new MOH731SQLCohortLibrary();

    /**
     * Dead patients indicator
     */
    public CohortIndicator cohortIndicatorCount(String desc, CohortDefinition cd) {
        CohortIndicator cohortIndicator = new CohortIndicator(desc);
        cohortIndicator.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortIndicator.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortIndicator.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cohortIndicator.setType(CohortIndicator.IndicatorType.COUNT);

        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        cohortIndicator.setCohortDefinition(cd, "startDate=${startDate},locationList=${locationList},endDate=${endDate}");
        return cohortIndicator;
    }





}