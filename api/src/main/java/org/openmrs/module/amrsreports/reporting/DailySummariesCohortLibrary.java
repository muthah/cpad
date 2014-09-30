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
import org.openmrs.module.amrsreports.reporting.cohort.definition.*;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Library of ART related cohort definitions
 */
@Component
public class DailySummariesCohortLibrary {


	private ICAPCommonCohortLibrary commonCohortLibrary = new ICAPCommonCohortLibrary();


    /**
     * Patients currently enrolled in care
     */



    public CohortDefinition femalesAgedAtLeastXNewEnrollments(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        NewEnrollmentsCohortDefinition cohortDefinition = new NewEnrollmentsCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtLeastXNewEnrollments(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        NewEnrollmentsCohortDefinition cohortDefinition = new NewEnrollmentsCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition femalesAgedAtMostXNewEnrollments(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        NewEnrollmentsCohortDefinition cohortDefinition = new NewEnrollmentsCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtMostXNewEnrollments(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        NewEnrollmentsCohortDefinition cohortDefinition = new NewEnrollmentsCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }


  //===================================================================================================

    /**
     * Revisits
     */

    //-------------------------------------------------------------------------------------------------------------------------------------------------------



    public CohortDefinition femalesAgedAtLeastXRevisits(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        PatientsWithRevisitsCohortDefinition cohortDefinition = new PatientsWithRevisitsCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtLeastXRevisits(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        PatientsWithRevisitsCohortDefinition cohortDefinition = new PatientsWithRevisitsCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition femalesAgedAtMostXRevisits(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        PatientsWithRevisitsCohortDefinition cohortDefinition = new PatientsWithRevisitsCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtMostXRevisits(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        PatientsWithRevisitsCohortDefinition cohortDefinition = new PatientsWithRevisitsCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }


//===========================================================================================================

    //===================================================================================================

    /**
     * on Care
     */

    //-------------------------------------------------------------------------------------------------------------------------------------------------------



    public CohortDefinition femalesAgedAtLeastXOnCare(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DailyPatientsOnCareCohortDefinition cohortDefinition = new DailyPatientsOnCareCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtLeastXOnCare(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DailyPatientsOnCareCohortDefinition cohortDefinition = new DailyPatientsOnCareCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition femalesAgedAtMostXOnCare(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DailyPatientsOnCareCohortDefinition cohortDefinition = new DailyPatientsOnCareCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtMostXOnCare(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DailyPatientsOnCareCohortDefinition cohortDefinition = new DailyPatientsOnCareCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }


//===========================================================================================================

    //===================================================================================================

    /**
     * on ART
     */

    //-------------------------------------------------------------------------------------------------------------------------------------------------------



    public CohortDefinition femalesAgedAtLeastXOnART(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DailyPatientsOnARTCohortDefinition cohortDefinition = new DailyPatientsOnARTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtLeastXOnART(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DailyPatientsOnARTCohortDefinition cohortDefinition = new DailyPatientsOnARTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition femalesAgedAtMostXOnART(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DailyPatientsOnARTCohortDefinition cohortDefinition = new DailyPatientsOnARTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition patientsAgedAtMostXOnART(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DailyPatientsOnARTCohortDefinition cohortDefinition = new DailyPatientsOnARTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition patientsAgedAtLeastXOnART(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DailyPatientsOnARTCohortDefinition cohortDefinition = new DailyPatientsOnARTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("agedAtMostSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("agedAtMostSomeAge AND sqlCohort");
        return cd;
    }

//===========================================================================================================

    public CohortDefinition patientsAgedAtMostXOnCare(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DailyPatientsOnCareCohortDefinition cohortDefinition = new DailyPatientsOnCareCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition patientsAgedAtLeastXOnCare(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DailyPatientsOnCareCohortDefinition cohortDefinition = new DailyPatientsOnCareCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("agedAtMostSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("agedAtMostSomeAge AND sqlCohort");
        return cd;
    }


}