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
 * Library of MOH 731 related indicator definitions. All indicators require parameters ${startDate} and ${endDate}
 */
@Component
public class PWPIndicatorLibrary {


    private PWPCohortLibrary baseCohorts = new PWPCohortLibrary();



    //indicators for Enrolled in care
    public CohortIndicator malePatientsTested() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males 0 - 1 Enrolled in care",
                ReportUtils.map(baseCohorts.malePatientsTested(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalePatientsTested() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males 2 - 4 Enrolled in care",
                ReportUtils.map(baseCohorts.femalePatientsTested(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator malePatientsWhoReportedDisclosure() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males 5 - 14 Enrolled in care",
                ReportUtils.map(baseCohorts.malePatientsWhoReportedDisclosure(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalePatientsWhoReportedDisclosure() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males => 15 Enrolled in care",
                ReportUtils.map(baseCohorts.femalePatientsWhoReportedDisclosure(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }


    public CohortIndicator malePatientsWhoReceivedCondoms() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males 0 - 1 Enrolled in care",
                ReportUtils.map(baseCohorts.malePatientsWhoReceivedCondoms(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalePatientsWhoReceivedCondoms() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males 2 - 4 Enrolled in care",
                ReportUtils.map(baseCohorts.femalePatientsWhoReceivedCondoms(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator malePatientsProvidedWithModernContraceptives() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males 5 - 14 Enrolled in care",
                ReportUtils.map(baseCohorts.malePatientsProvidedWithModernContraceptives(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalePatientsProvidedWithModernContraceptives() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males => 15 Enrolled in care",
                ReportUtils.map(baseCohorts.femalePatientsProvidedWithModernContraceptives(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }



    //2. Number of patients screened for active TB at enrollment into HIV care
    public CohortIndicator malePatientsScreenedForSTI() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males 0 - 1 Enrolled in care",
                ReportUtils.map(baseCohorts.malePatientsScreenedForSTI(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalePatientsScreenedForSTI() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males 2 - 4 Enrolled in care",
                ReportUtils.map(baseCohorts.femalePatientsScreenedForSTI(15), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator malePatientsOnPWPPackage() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males 5 - 14 Enrolled in care",
                ReportUtils.map(baseCohorts.malePatientsOnPWPPackage(25), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalePatientsOnPWPPackage() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males => 15 Enrolled in care",
                ReportUtils.map(baseCohorts.femalePatientsOnPWPPackage(25), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

}