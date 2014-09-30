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

import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.stereotype.Component;

/**
 * Library for daily attendance summary
 */
@Component
public class AttendanceIndicatorLibrary {


    private AttendanceCohortLibrary baseCohorts = new AttendanceCohortLibrary();



    //new patients
    public CohortIndicator malesBelow15NewEnrollment() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males below 15 newly enrolled",
                ReportUtils.map(baseCohorts.malesBelow15NewEnrollment(14), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15NewEnrollment() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females below 15 newly enrolled",
                ReportUtils.map(baseCohorts.femalesBelow15NewEnrollment(14), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator malesAbove15NewEnrollment() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males 15 and above newly enrolled",
                ReportUtils.map(baseCohorts.malesAbove15NewEnrollment(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesAbove15NewEnrollment() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females 15 and above newly enrolled",
                ReportUtils.map(baseCohorts.femalesAbove15NewEnrollment(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    /**
     * revisits
     * @return
     */
    public CohortIndicator malesBelow15WithRevisits() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males below 15 with revisits",
                ReportUtils.map(baseCohorts.malesBelow15WithRevisits(14), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15WithRevisits() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females below 15 with revisits",
                ReportUtils.map(baseCohorts.femalesBelow15WithRevisits(14), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator malesAbove15WithRevisits() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males 15 and above with revisits",
                ReportUtils.map(baseCohorts.malesAbove15WithRevisits(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesAbove15WithRevisits() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females 15 and above with revisits",
                ReportUtils.map(baseCohorts.femalesAbove15WithRevisits(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }




}