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

    /**
     * indicators for Enrolled in care
     * @return
     */

	public CohortIndicator infantsEnrolledInCare() {
		return CommonIndicatorLibrary.createCohortIndicator("Infants Enrolled in care",
				ReportUtils.map(baseCohorts.compositionAgeInMonthsCohort(0,11,sqlQueries.infantsEnrolledInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
	}

    public CohortIndicator malesBelow15EnrolledInCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Males < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsEnrolledInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveEnrolledInCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Males >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsEnrolledInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15EnrolledInCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Females < 15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsEnrolledInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveEnrolledInCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Females >=15 Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsEnrolledInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }


    /**
     * Currently Enrolled in care
     * @return
     */

    public CohortIndicator infantsCurrentlyEnrolledInCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Infants Currently Enrolled in care",
                ReportUtils.map(baseCohorts.compositionAgeInMonthsCohort(0,11,sqlQueries.infantsCurrentlyInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator malesBelow15CurrentlyEnrolledInCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Males < 15 Currently Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsCurrentlyInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveCurrentlyEnrolledInCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Males >=15 Currently Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsCurrentlyInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15CurrentlyEnrolledInCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Females < 15 Currently Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsCurrentlyInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveCurrentlyEnrolledInCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Females >=15 Currently Enrolled in care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsCurrentlyInCareBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }


    /**
     * Indicators for Cotrimoxazole
     * @return
     */

    public CohortIndicator infantsExposedWithin2Months() {
        return CommonIndicatorLibrary.createCohortIndicator("Infants Exposed within 2 months",
                ReportUtils.map(baseCohorts.compositionAgeInMonthsCohort(0,11,sqlQueries.exposedInfantsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator infantsExposedAt2Months() {
        return CommonIndicatorLibrary.createCohortIndicator("Infants Exposed at 2 months",
                ReportUtils.map(baseCohorts.compositionAgeInMonthsCohort(0,11,sqlQueries.exposedInfantsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }


    public CohortIndicator malesBelow15OnCotrimoxazole() {
        return CommonIndicatorLibrary.createCohortIndicator("Males < 15 On Cotrimoxazole",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsOnCotrimoxazoleBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveOnCotrimoxazole() {
        return CommonIndicatorLibrary.createCohortIndicator("Males >=15 On Cotrimoxazole",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsOnCotrimoxazoleBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15OnCotrimoxazole() {
        return CommonIndicatorLibrary.createCohortIndicator("Females < 15 On Cotrimoxazole",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsOnCotrimoxazoleBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveOnCotrimoxazole() {
        return CommonIndicatorLibrary.createCohortIndicator("Females >=15 On Cotrimoxazole",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsOnCotrimoxazoleBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    /**
     * Currently on ART
     * @return
     */
    public CohortIndicator infantsCurrentlyOnART() {
        return CommonIndicatorLibrary.createCohortIndicator("Infants currently on ART",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.infantsCurrentlyOnARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }
    public CohortIndicator malesBelow15CurrentlyOnART() {
        return CommonIndicatorLibrary.createCohortIndicator("Males < 15 currently on ART",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsCurrentlyOnARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveCurrentlyOnART() {
        return CommonIndicatorLibrary.createCohortIndicator("Males >=15 currently on ART",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsCurrentlyOnARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15CurrentlyOnART() {
        return CommonIndicatorLibrary.createCohortIndicator("Females < 15 currently on ART",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsCurrentlyOnARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveCurrentlyOnART() {
        return CommonIndicatorLibrary.createCohortIndicator("Females >=15 currently on ART",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsCurrentlyOnARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    /**
     * ever on care
     * @return
     */
    public CohortIndicator malesBelow15EverOnCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Males < 15 Ever on Care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsEverOnCareQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveEverOnCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Males >=15 Ever on Care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsEverOnCareQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15EverOnCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Females < 15 Ever on Care",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsEverOnCareQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveEverOnCare() {
        return CommonIndicatorLibrary.createCohortIndicator("Females >=15 Ever on Care",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsEverOnCareQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    /**
     * revisits
     * @return
     */
    public CohortIndicator infantsWithARTRevisits() {
        return CommonIndicatorLibrary.createCohortIndicator("Infants With Revisits",
                ReportUtils.map(baseCohorts.compositionAgeInMonthsCohort(0,11,sqlQueries.infantsWithRevisitsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }
    public CohortIndicator malesBelow15WithARTRevisits() {
        return CommonIndicatorLibrary.createCohortIndicator("Males < 15 With Revisits",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsWithRevisitsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveWithARTRevisits() {
        return CommonIndicatorLibrary.createCohortIndicator("Males >=15 With Revisits",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsWithRevisitsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15WithARTRevisits() {
        return CommonIndicatorLibrary.createCohortIndicator("Females < 15 With Revisits",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsWithRevisitsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveWithARTRevisits() {
        return CommonIndicatorLibrary.createCohortIndicator("Females >=15 With Revisits",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsWithRevisitsBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    /**
     * starting art
     * @return
     */
    public CohortIndicator infantsStartingART() {
        return CommonIndicatorLibrary.createCohortIndicator("Infants Starting ART",
                ReportUtils.map(baseCohorts.compositionAgeInMonthsCohort(0,11,sqlQueries.infantsStartingARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }
    public CohortIndicator malesBelow15StartingART() {
        return CommonIndicatorLibrary.createCohortIndicator("Males < 15 Starting ART",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsStartingARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveStartingART() {
        return CommonIndicatorLibrary.createCohortIndicator("Males >=15 Starting ART",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsStartingARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15StartingART() {
        return CommonIndicatorLibrary.createCohortIndicator("Females < 15 Starting ART",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsStartingARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveStartingART() {
        return CommonIndicatorLibrary.createCohortIndicator("Females >=15 Starting ART",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsStartingARTBetweenDatesQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    /**
     * Regimens
     */

    public CohortIndicator patientsOnOriginalFirstLine() {
        return CommonIndicatorLibrary.createCohortIndicator("Patients on Original First Line",
                ReportUtils.map(baseCohorts.createCohortDefinition("patients on Original First Line",sqlQueries.patientsOnOriginalFirstLineRegimenQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator patientsOnAlternativeFirstLine() {
        return CommonIndicatorLibrary.createCohortIndicator("Patients on alternative First Line",
                ReportUtils.map(baseCohorts.createCohortDefinition("Patients on alternative First Line",sqlQueries.patientsOnAlternativeFirstLineRegimenQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator patientsOnSecondLineAndHigherRegimen() {
        return CommonIndicatorLibrary.createCohortIndicator("Patients on Second Line or Higher Regimen",
                ReportUtils.map(baseCohorts.createCohortDefinition("Patients on Second Line and Higher",sqlQueries.patientsOnSecondLineOrHigherRegimenQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    /**
     * TB Screening
     */
    public CohortIndicator malesBelow15ScreenedForTB() {
        return CommonIndicatorLibrary.createCohortIndicator("Males < 15 Screened for TB",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.malePatientsWhoHadTBScreeningQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator males15AndAboveScreenedForTB() {
        return CommonIndicatorLibrary.createCohortIndicator("Males >=15 Screened for TB",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.malePatientsWhoHadTBScreeningQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator femalesBelow15ScreenedForTB() {
        return CommonIndicatorLibrary.createCohortIndicator("Females < 15 Screened for TB",
                ReportUtils.map(baseCohorts.compositionMaxAgeCohort(14,sqlQueries.femalePatientsWhoHadTBScreeningQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }

    public CohortIndicator females15AndAboveScreenedForTB() {
        return CommonIndicatorLibrary.createCohortIndicator("Females >=15 Screened for TB",
                ReportUtils.map(baseCohorts.compositionMinAgeCohort(15,sqlQueries.femalePatientsWhoHadTBScreeningQry()), "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
    }



}