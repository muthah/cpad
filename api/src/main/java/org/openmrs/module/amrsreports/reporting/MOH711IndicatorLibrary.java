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
import org.openmrs.module.amrsreports.reporting.indicatorsSQLLib.MOH711.MOH711SQLCohortLibrary;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.stereotype.Component;

/**
 * Library of MOH 731 related indicator definitions. All indicators require parameters ${startDate} and ${endDate}
 */
@Component
public class MOH711IndicatorLibrary {


    private BaseSQLCohortLibrary baseCohorts = new BaseSQLCohortLibrary();
    private MOH711SQLCohortLibrary sqlQueries = new MOH711SQLCohortLibrary();



    //indicators for Enrolled in care
    public CohortIndicator malesBelow15EnrolledInCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.cumulativeMalesInCare()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveEnrolledInCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.cumulativeMalesInCare()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15EnrolledInCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.cumulativeFemalesInCare()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveEnrolledInCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.cumulativeFemalesInCare()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }


    public CohortIndicator malesBelow15EnrolledInCarePMTCT() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsEnrolledInCareThroPMTCTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveEnrolledInCarePMTCT() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsEnrolledInCareThroPMTCTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }



    public CohortIndicator femalesBelow15EnrolledInCarePMTCT() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsEnrolledInCareThroPMTCTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveEnrolledInCarePMTCT() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsEnrolledInCareThroPMTCTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }


    public CohortIndicator malesBelow15EnrolledInCareVCT() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsEnrolledInCareThroVCTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveEnrolledInCareVCT() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsEnrolledInCareThroVCTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }



    public CohortIndicator femalesBelow15EnrolledInCareVCT() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsEnrolledInCareThroVCTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveEnrolledInCareVCT() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsEnrolledInCareThroVCTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }


    //===============================================

    public CohortIndicator malesBelow15EnrolledInCareTB() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsEnrolledInCareThroTBBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveEnrolledInCareTB() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsEnrolledInCareThroTBBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }



    public CohortIndicator femalesBelow15EnrolledInCareTB() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsEnrolledInCareThroTBBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveEnrolledInCareTB() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsEnrolledInCareThroTBBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    //===============================================================

    public CohortIndicator malesBelow15EnrolledInCareInpatient() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsEnrolledInCareThroInpatientBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveEnrolledInCareInpatient() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsEnrolledInCareThroInpatientBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }



    public CohortIndicator femalesBelow15EnrolledInCareInpatient() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsEnrolledInCareThroInpatientBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveEnrolledInCareInpatient() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsEnrolledInCareThroInpatientBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    //==============================================================

    public CohortIndicator malesBelow15EnrolledInCareCWC() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsEnrolledInCareThroInpatientBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveEnrolledInCareCWC() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsEnrolledInCareThroInpatientBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }



    public CohortIndicator femalesBelow15EnrolledInCareCWC() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsEnrolledInCareThroInpatientBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveEnrolledInCareCWC() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsEnrolledInCareThroInpatientBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    //======================================================

    public CohortIndicator malesBelow15EnrolledInCareAllOthers() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsEnrolledInCareThroAllOthersBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveEnrolledInCareAllOthers() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsEnrolledInCareThroAllOthersBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }



    public CohortIndicator femalesBelow15EnrolledInCareAllOthers() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsEnrolledInCareThroAllOthersBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveEnrolledInCareAllOthers() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsEnrolledInCareThroAllOthersBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    //=====================================================


    //---------------------------------------- WHO -----------------------------------
    public CohortIndicator malesBelow15StartingARTWHO1() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsStartingARTWHO1BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveStartingARTWHO1() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsStartingARTWHO1BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }



    public CohortIndicator femalesBelow15StartingARTWHO1() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsStartingARTWHO1BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveStartingARTWHO1() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsStartingARTWHO1BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }


    public CohortIndicator malesBelow15StartingARTWHO2() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsStartingARTWHO2BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveStartingARTWHO2() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsStartingARTWHO2BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }



    public CohortIndicator femalesBelow15StartingARTWHO2() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsStartingARTWHO2BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveStartingARTWHO2() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsStartingARTWHO2BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }


    //===============================================

    public CohortIndicator malesBelow15StartingARTWHO3() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsStartingARTWHO3BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveStartingARTWHO3() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsStartingARTWHO3BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }



    public CohortIndicator femalesBelow15StartingARTWHO3() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsStartingARTWHO3BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveStartingARTWHO3() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsStartingARTWHO3BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    //===============================================================

    public CohortIndicator malesBelow15StartingARTWHO4() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsStartingARTWHO4BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveStartingARTWHO4() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsStartingARTWHO4BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }



    public CohortIndicator femalesBelow15StartingARTWHO4() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsStartingARTWHO4BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveStartingARTWHO4() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsStartingARTWHO4BetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    //==============================================================
    /**
     * Currently Enrolled in care
     * @return
     */

    public CohortIndicator infantsCurrentlyEnrolledInCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Infants Currently Enrolled in care",
                ReportUtils.map(baseCohorts.compositionAgeInMonthsCohort(0,11,sqlQueries.infantsCurrentlyInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator malesBelow15CurrentlyEnrolledInCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Currently Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsCurrentlyInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveCurrentlyEnrolledInCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Currently Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsCurrentlyInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15CurrentlyEnrolledInCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Currently Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsCurrentlyInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveCurrentlyEnrolledInCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Currently Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsCurrentlyInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }


    /**
     * Indicators for Cotrimoxazole
     * @return
     */

    public CohortIndicator infantsExposedWithin2Months() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Infants Exposed within 2 months",
                ReportUtils.map(baseCohorts.compositionAgeInMonthsCohort(0,11,sqlQueries.exposedInfantsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator infantsExposedAt2Months() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Infants Exposed at 2 months",
                ReportUtils.map(baseCohorts.compositionAgeInMonthsCohort(0,11,sqlQueries.exposedInfantsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }


    public CohortIndicator malesBelow15OnCotrimoxazole() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 On Cotrimoxazole",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsOnCotrimoxazoleBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveOnCotrimoxazole() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 On Cotrimoxazole",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsOnCotrimoxazoleBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15OnCotrimoxazole() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 On Cotrimoxazole",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsOnCotrimoxazoleBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveOnCotrimoxazole() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 On Cotrimoxazole",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsOnCotrimoxazoleBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    /**
     * Currently on ART
     * @return
     */
    public CohortIndicator infantsCurrentlyOnART() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Infants currently on ART",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.infantsCurrentlyOnARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }
    public CohortIndicator malesBelow15CurrentlyOnART() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 currently on ART",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsCurrentlyOnARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveCurrentlyOnART() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 currently on ART",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsCurrentlyOnARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15CurrentlyOnART() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 currently on ART",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsCurrentlyOnARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveCurrentlyOnART() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 currently on ART",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsCurrentlyOnARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    /**
     * ever on care
     * @return
     */
    public CohortIndicator malesBelow15EverOnCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Ever on Care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsEverOnCareQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveEverOnCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Ever on Care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsEverOnCareQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15EverOnCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Ever on Care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsEverOnCareQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveEverOnCare() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Ever on Care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsEverOnCareQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    /**
     * revisits
     * @return
     */
    public CohortIndicator infantsWithARTRevisits() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Infants With Revisits",
                ReportUtils.map(baseCohorts.compositionAgeInMonthsCohort(0,11,sqlQueries.infantsWithRevisitsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }
    public CohortIndicator malesBelow15WithARTRevisits() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 With Revisits",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsWithRevisitsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveWithARTRevisits() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 With Revisits",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsWithRevisitsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15WithARTRevisits() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 With Revisits",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsWithRevisitsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveWithARTRevisits() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 With Revisits",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsWithRevisitsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    /**
     * starting art
     * @return
     */
    public CohortIndicator infantsStartingART() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Infants Starting ART",
                ReportUtils.map(baseCohorts.compositionAgeInMonthsCohort(0,11,sqlQueries.infantsStartingARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }
    public CohortIndicator malesBelow15StartingART() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males < 15 Starting ART",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsStartingARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveStartingART() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Males >=15 Starting ART",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsStartingARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15StartingART() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females < 15 Starting ART",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsStartingARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveStartingART() {
        return ICAPCommonIndicatorLibrary.createCohortIndicator("Females >=15 Starting ART",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsStartingARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

}