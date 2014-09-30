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
import org.openmrs.module.amrsreports.reporting.cohort.definition.CurrentlyInCareCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.CurrentlyOnARTCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.PatientsWithRecentEncCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.DeadPatientsCohortDefinition;
import org.openmrs.module.amrsreports.reporting.cohort.definition.TransferOUTCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.AgeCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.common.DurationUnit;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Library of ART related cohort definitions
 */
@Component
public class MOH731CohortLibrary {


	private ICAPCommonCohortLibrary commonCohortLibrary = new ICAPCommonCohortLibrary();


    /**
     * Patients currently enrolled in care
     */

    //-------------------------------------------------------------------------------------------------------------------------------------------------------
    public CohortDefinition infantsCurrentlyInCare() {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        CurrentlyInCareCohortDefinition cohortDefinition = new CurrentlyInCareCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        AgeCohortDefinition ageCohortDefinition = new AgeCohortDefinition();
        ageCohortDefinition.setMaxAgeUnit(DurationUnit.MONTHS);
        ageCohortDefinition.setMaxAge(11);
        ageCohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));

        cd.addSearch("agedCohort", ReportUtils.<CohortDefinition>map(ageCohortDefinition, "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("agedCohort AND sqlCohort");
        return cd;
    }


    public CohortDefinition femalesAgedAtLeastXCurrentlyInCare(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        CurrentlyInCareCohortDefinition cohortDefinition = new CurrentlyInCareCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtLeastXCurrentlyInCare(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        CurrentlyInCareCohortDefinition cohortDefinition = new CurrentlyInCareCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition femalesAgedAtMostXCurrentlyInCare(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        CurrentlyInCareCohortDefinition cohortDefinition = new CurrentlyInCareCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtMostXCurrentlyInCare(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        CurrentlyInCareCohortDefinition cohortDefinition = new CurrentlyInCareCohortDefinition();
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
     * Patients currently enrolled on ART
     */

    //-------------------------------------------------------------------------------------------------------------------------------------------------------
    public CohortDefinition infantsCurrentlyOnART() {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        CurrentlyOnARTCohortDefinition cohortDefinition = new CurrentlyOnARTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        AgeCohortDefinition ageCohortDefinition = new AgeCohortDefinition();
        ageCohortDefinition.setMaxAgeUnit(DurationUnit.MONTHS);
        ageCohortDefinition.setMaxAge(11);
        ageCohortDefinition.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));

        cd.addSearch("agedCohort", ReportUtils.<CohortDefinition>map(ageCohortDefinition, "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("agedCohort AND sqlCohort");
        return cd;
    }


    public CohortDefinition femalesAgedAtLeastXCurrentlyOnART(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        CurrentlyOnARTCohortDefinition cohortDefinition = new CurrentlyOnARTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtLeastXCurrentlyOnART(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        CurrentlyOnARTCohortDefinition cohortDefinition = new CurrentlyOnARTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition femalesAgedAtMostXCurrentlyOnART(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        CurrentlyOnARTCohortDefinition cohortDefinition = new CurrentlyOnARTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtMostXCurrentlyOnART(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        CurrentlyOnARTCohortDefinition cohortDefinition = new CurrentlyOnARTCohortDefinition();
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

	/*
	* Transfers
	*
	* */

    public CohortDefinition femalesAtLeastTransfers(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        TransferOUTCohortDefinition cohortDefinition = new TransferOUTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition femalesAtMostTransfers(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        TransferOUTCohortDefinition cohortDefinition = new TransferOUTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }


    public CohortDefinition malesAtLeastTransfers(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        TransferOUTCohortDefinition cohortDefinition = new TransferOUTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAtMostTransfers(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        TransferOUTCohortDefinition cohortDefinition = new TransferOUTCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------

    public CohortDefinition femalesAgedAtLeastXActive(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        PatientsWithRecentEncCohortDefinition cohortDefinition = new PatientsWithRecentEncCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtLeastXActive(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        PatientsWithRecentEncCohortDefinition cohortDefinition = new PatientsWithRecentEncCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition femalesAgedAtMostXActive(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        PatientsWithRecentEncCohortDefinition cohortDefinition = new PatientsWithRecentEncCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtMostXActive(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        PatientsWithRecentEncCohortDefinition cohortDefinition = new PatientsWithRecentEncCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }
    //------------------------------------------------------------------------------------------------------------------


    public CohortDefinition femalesAgedAtLeastXDead(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DeadPatientsCohortDefinition cohortDefinition = new DeadPatientsCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtLeastXDead(Integer minAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DeadPatientsCohortDefinition cohortDefinition = new DeadPatientsCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtLeast(minAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition femalesAgedAtMostXDead(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("females aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DeadPatientsCohortDefinition cohortDefinition = new DeadPatientsCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("females", ReportUtils.map(commonCohortLibrary.females()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("females AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }

    public CohortDefinition malesAgedAtMostXDead(Integer maxAge) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();

        cd.setName("males aged at least Some age");
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        DeadPatientsCohortDefinition cohortDefinition = new DeadPatientsCohortDefinition();
        cohortDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDefinition.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        cd.addSearch("males", ReportUtils.map(commonCohortLibrary.males()));
        cd.addSearch("agedAtLeastSomeAge", ReportUtils.map(commonCohortLibrary.agedAtMost(maxAge), "effectiveDate=${endDate}"));
        cd.addSearch("sqlCohort", ReportUtils.<CohortDefinition>map(cohortDefinition, "startDate=${startDate},locationList=${locationList},endDate=${endDate}"));
        cd.setCompositionString("males AND agedAtLeastSomeAge AND sqlCohort");
        return cd;
    }




}