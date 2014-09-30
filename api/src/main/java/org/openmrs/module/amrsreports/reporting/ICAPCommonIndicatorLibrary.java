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
import org.openmrs.Program;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Library of common indicator definitions. All indicators require parameters ${startDate} and ${endDate}
 */
@Component
public class ICAPCommonIndicatorLibrary {


	CommonICAPCohortLibrary commonCohorts = new CommonICAPCohortLibrary();


	/**
	 * Number of patients ever enrolled in the given program (including transfers) up to ${endDate}
	 * @param program the program
	 * @return the indicator
	 */
	public CohortIndicator enrolledCumulative(Program program) {
		return createCohortIndicator("Number of patients ever enrolled in " + program.getName() + " excluding transfers",
				ReportUtils.map(commonCohorts.enrolled(program), "enrolledOnOrBefore=${endDate}"));
	}


	/**
	 * Utility method to create a new cohort indicator
	 * @param description the indicator description
	 * @return the cohort indicator
	 */
    public static CohortIndicator createCohortIndicator(String description, Mapped<CohortDefinition> mappedCohort) {
        CohortIndicator ind = new CohortIndicator(description);
        ind.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ind.addParameter(new Parameter("endDate", "End Date", Date.class));
        ind.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        ind.setType(CohortIndicator.IndicatorType.COUNT);
        ind.setCohortDefinition(mappedCohort);
        return ind;
    }

    public static CohortIndicator createSQLCohortIndicator(String description, Mapped<CohortDefinition> mappedCohort) {
        CohortIndicator ind = new CohortIndicator(description);
        ind.addParameter(new Parameter("reportDate", "Report Date", Date.class));
        ind.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        ind.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        ind.setCohortDefinition(mappedCohort);
        return ind;
    }

    public static CohortIndicator createCohortIndicatorAtStart(String description, CohortDefinition  mappedCohort) {
        CohortIndicator ind = new CohortIndicator(description);
        ind.setName("Cohort Indicator for the start of a given date");
        ind.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ind.addParameter(new Parameter("endDate", "End Date", Date.class));
        ind.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        ind.setCohortDefinition(mappedCohort, "effectiveDate=${startDate},locationList=${locationList},onOrBefore=${startDate}");
        return ind;
    }

    public static CohortIndicator createCohortIndicatorAtEnd(String description, CohortDefinition mappedCohort) {
        CohortIndicator ind = new CohortIndicator(description);
        ind.setName("Cohort Indicator at end of a given date");
        ind.addParameter(new Parameter("startDate", "Start Date", Date.class));
        ind.addParameter(new Parameter("endDate", "End Date", Date.class));
        ind.addParameter(new Parameter("locationList", "List of Locations", Location.class));
        ind.setCohortDefinition(mappedCohort, "effectiveDate=${endDate},locationList=${locationList},onOrBefore=${endDate}");
        return ind;
    }

    private void sampleIndicatorReport(){
        /*GenderCohortDefinition malesCohort = new GenderCohortDefinition();
		malesCohort.setName("Gender = Male");
		malesCohort.setMaleIncluded(true);

		GenderCohortDefinition femalesCohort = new GenderCohortDefinition();
		femalesCohort.setName("Gender = Female");
		femalesCohort.setFemaleIncluded(true);

		AgeCohortDefinition pedsCohort = new AgeCohortDefinition();
		pedsCohort.setName("Age < 15");
		pedsCohort.addParameter(new Parameter("effectiveDate", "Date", Date.class));
		pedsCohort.setMaxAge(14);

		CohortIndicator malesIndicator = new CohortIndicator("Count of males");
		malesIndicator.setCohortDefinition(malesCohort, "");

		CohortIndicator femalesIndicator = new CohortIndicator("Count of females");
		femalesIndicator.setCohortDefinition(femalesCohort, "");

		CohortIndicator pedsIndicator = new CohortIndicator("Count of peds");
		pedsIndicator.addParameter(new Parameter("date", "Date", Date.class));
		pedsIndicator.setCohortDefinition(pedsCohort, "effectiveDate=${date}");

		cohortDsd1 = new CohortIndicatorDataSetDefinition();
		cohortDsd1.setName("Cohort DSD1");
		cohortDsd1.addColumn("test-1", "Count of males", new Mapped<CohortIndicator>(malesIndicator, null), "");
		cohortDsd1.addColumn("test-3", "Count of peds", new Mapped<CohortIndicator>(pedsIndicator, null), "");

		cohortDsd2 = new CohortIndicatorDataSetDefinition();
		cohortDsd2.setName("Cohort DSD2");
		cohortDsd1.addColumn("test-2", "Count of females", new Mapped<CohortIndicator>(femalesIndicator, null), "");

		evaluationContext = new EvaluationContext();
		evaluationContext.addParameterValue("date", TestUtils.date(2012, 1, 1));
		evaluationContext.setBaseCohort(new Cohort(Context.getPatientService().getAllPatients()));





		==================================================================================================
        AgeCohortDefinition childrenOnDate = new AgeCohortDefinition();
		childrenOnDate.addParameter(new Parameter("effectiveDate", "effectiveDate", Date.class));
		childrenOnDate.setMaxAge(14);

		AgeCohortDefinition adultsOnDate = new AgeCohortDefinition();
		adultsOnDate.addParameter(new Parameter("effectiveDate", "effectiveDate", Date.class));
		adultsOnDate.setMinAge(15);

		CohortIndicator childrenAtStart = new CohortIndicator();
		childrenAtStart.addParameter(ReportingConstants.START_DATE_PARAMETER);
		childrenAtStart.addParameter(ReportingConstants.END_DATE_PARAMETER);
		childrenAtStart.setUuid(UUID.randomUUID().toString());
		childrenAtStart.setCohortDefinition(childrenOnDate, "effectiveDate=${startDate}");

		CohortIndicator childrenAtEnd = new CohortIndicator();
		childrenAtEnd.addParameter(ReportingConstants.START_DATE_PARAMETER);
		childrenAtEnd.addParameter(ReportingConstants.END_DATE_PARAMETER);
		childrenAtEnd.setUuid(UUID.randomUUID().toString());
		childrenAtEnd.setCohortDefinition(childrenOnDate, "effectiveDate=${endDate}");

		CohortIndicator adultsAtStart = new CohortIndicator();
		adultsAtStart.addParameter(ReportingConstants.START_DATE_PARAMETER);
		adultsAtStart.addParameter(ReportingConstants.END_DATE_PARAMETER);
		adultsAtStart.setUuid(UUID.randomUUID().toString());
		adultsAtStart.setCohortDefinition(adultsOnDate, "effectiveDate=${startDate}");

		CohortIndicator adultsAtEnd = new CohortIndicator();
		adultsAtEnd.addParameter(ReportingConstants.START_DATE_PARAMETER);
		adultsAtEnd.addParameter(ReportingConstants.END_DATE_PARAMETER);
		adultsAtEnd.setUuid(UUID.randomUUID().toString());
		adultsAtEnd.setCohortDefinition(adultsOnDate, "effectiveDate=${endDate}");

		Map<String, Object> periodMappings = new HashMap<String, Object>();
		periodMappings.put("startDate", "${startDate}");
		periodMappings.put("endDate", "${endDate}");

		CohortIndicatorDataSetDefinition d = new CohortIndicatorDataSetDefinition();
		d.addParameter(ReportingConstants.START_DATE_PARAMETER);
		d.addParameter(ReportingConstants.END_DATE_PARAMETER);
		d.addColumn("1", "Children At Start", new Mapped<CohortIndicator>(childrenAtStart, periodMappings), "");
		d.addColumn("2", "Children At End", new Mapped<CohortIndicator>(childrenAtEnd, periodMappings), "");
		d.addColumn("3", "Adults At Start", new Mapped<CohortIndicator>(adultsAtStart, periodMappings), "");
		d.addColumn("4", "Adults At End", new Mapped<CohortIndicator>(adultsAtEnd, periodMappings), "");

		EvaluationContext context = new EvaluationContext();
		context.addParameterValue(ReportingConstants.START_DATE_PARAMETER.getName(), DateUtil.getDateTime(1980, 1, 1));
		context.addParameterValue(ReportingConstants.END_DATE_PARAMETER.getName(), DateUtil.getDateTime(2000, 1, 1));




		==================================================================================================

		*/
    }
}