package org.openmrs.module.amrsreports.reporting.provider;

/**
 * Provides mechanisms for rendering the MOH 361A Pre-ART Register
 */
public class SQLCohortDefTest extends ReportProvider {
/*


    private CommonICAPCohortLibrary commonCohorts = new CommonICAPCohortLibrary();
    private CommonSQLCohortLibrary sqlCohortLibrary = new CommonSQLCohortLibrary();

	public SQLCohortDefTest() {
		this.name = "Antony's Test provider";
		this.visible = true;
	}

	@Override
	public ReportDefinition getReportDefinition() {

		ReportDefinition report = new PeriodIndicatorReportDefinition();
		report.setName("SQL COhort Test Design");

        // set up parameter mappings
        Map<String, Object> paramMappings = new HashMap<String, Object>();
        paramMappings.put("reportDate", "${reportDate}");
       */
/* paramMappings.put("minAge", "${minAge}");
        paramMappings.put("maxAge", "${maxAge}");*//*

        paramMappings.put("endDate", "${endDate}");
        paramMappings.put("locationList", "${locationList}");

        //define general cohorts

        */
/**
         * Define cohorts for males above 15
         *//*


        CohortDefinition malesBelow15Cohort = commonCohorts.facilityMalesWithAgeAtMost(14);
        CohortIndicator malesBelow15CohortAtStartIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Males below 15 at start of quarter", ReportUtils.map(malesBelow15Cohort, "reportDate=${reportDate},locationList=${locationList}"));
        CohortIndicator malesBelow15CohortAtEndIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Males below 15 at end of quarter", ReportUtils.map(malesBelow15Cohort, "reportDate=${endDate},locationList=${locationList}"));


        CohortDefinition malesAbove15Cohort = commonCohorts.facilityMalesWithAgeAtLeast(15);
        CohortIndicator malesAbove15CohortAtStartIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Males Above 15 at start of quarter", ReportUtils.map(malesAbove15Cohort, "reportDate=${reportDate},locationList=${locationList}"));
        CohortIndicator malesAbove15CohortAtEndIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Males Above 15 at end of quarter", ReportUtils.map(malesAbove15Cohort, "reportDate=${endDate},locationList=${locationList}"));


        */
/**
         * Define cohorts for females above and below 15
         *//*



        CohortDefinition femalesBelow15Cohort = commonCohorts.facilityFemalesWithAgeAtMost(14);
        CohortIndicator femalesBelow15CohortAtStartIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Females below 15 at start of quarter", ReportUtils.map(femalesBelow15Cohort, "reportDate=${reportDate},locationList=${locationList}"));
        CohortIndicator femalesBelow15CohortAtEndIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Females below 15 at end of quarter", ReportUtils.map(femalesBelow15Cohort, "reportDate=${endDate},locationList=${locationList}"));

        CohortDefinition femalesAbove15Cohort = commonCohorts.facilityFemalesWithAgeAtLeast(15);
        CohortIndicator femalesAbove15CohortAtStartIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Females Above 15 at start of quarter", ReportUtils.map(femalesAbove15Cohort, "reportDate=${reportDate},locationList=${locationList}"));
        CohortIndicator femalesAbove15CohortAtEndIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Females Above 15 at end of quarter", ReportUtils.map(femalesAbove15Cohort, "reportDate=${endDate},locationList=${locationList}"));

        */
/**
         * Define Cohorts for peds
         *//*



        //Construct age and gender cohorts
        CohortDefinition malesPedsZeroToOneCohort = commonCohorts.facilityMalesWithAgeBetween(0,1);
        CohortDefinition malesPedsTwoToFourCohort = commonCohorts.facilityMalesWithAgeBetween(2,4);;
        CohortDefinition malesPedsFiveToFourteenCohort = commonCohorts.facilityMalesWithAgeBetween(5,14);;

        CohortDefinition femalesPedsZeroToOneCohort = commonCohorts.facilityFemalesWithAgeBetween(0,1);;
        CohortDefinition femalesPedsTwoToFourCohort = commonCohorts.facilityFemalesWithAgeBetween(2,4);
        CohortDefinition femalesPedsFiveToFourteenCohort = commonCohorts.facilityFemalesWithAgeBetween(5,14);

        //Define indicators
        CohortIndicator malesPedsZeroToOneCohortAtStartIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Male peds up to 1 at start of quarter", ReportUtils.map(malesPedsZeroToOneCohort, "reportDate=${reportDate},locationList=${locationList}"));
        CohortIndicator malesPedsZeroToOneCohortAtEndIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Male peds up to 1 at end of quarter", ReportUtils.map(malesPedsZeroToOneCohort, "reportDate=${endDate},locationList=${locationList}"));

        CohortIndicator malesPedsTwoToFourCohortAtStartIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for male peds btw 2 and 4 at start of quarter", ReportUtils.map(malesPedsTwoToFourCohort, "reportDate=${reportDate},locationList=${locationList}"));
        CohortIndicator malesPedsTwoToFourCohortAtEndIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for male peds btw 2 and 4 at end of quarter", ReportUtils.map(malesPedsTwoToFourCohort, "reportDate=${endDate},locationList=${locationList}"));


        CohortIndicator malesPedsFiveToFourteenCohortAtStartIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for male peds btw 5 and 14 at start of quarter", ReportUtils.map(malesPedsFiveToFourteenCohort, "reportDate=${reportDate},locationList=${locationList}"));
        CohortIndicator malesPedsFiveToFourteenCohortAtEndIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for male peds btw 5 and 14 at end of quarter", ReportUtils.map(malesPedsFiveToFourteenCohort, "reportDate=${endDate},locationList=${locationList}"));


        CohortIndicator femalesPedsZeroToOneCohortAtStartIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Female peds up to 1 at start of quarter", ReportUtils.map(femalesPedsZeroToOneCohort, "reportDate=${reportDate},locationList=${locationList}"));
        CohortIndicator femalesPedsZeroToOneCohortAtEndIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Female peds up to 1 at end of quarter", ReportUtils.map(femalesPedsZeroToOneCohort, "reportDate=${endDate},locationList=${locationList}"));

        CohortIndicator femalesPedsTwoToFourCohortAtStartIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Female peds btw 2 and 4 at start of quarter", ReportUtils.map(femalesPedsTwoToFourCohort, "reportDate=${reportDate},locationList=${locationList}"));
        CohortIndicator femalesPedsTwoToFourCohortAtEndIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Female peds btw 2 and 4 at end of quarter", ReportUtils.map(femalesPedsTwoToFourCohort, "reportDate=${endDate},locationList=${locationList}"));


        CohortIndicator femalesPedsFiveToFourteenCohortAtStartIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Female peds btw 5 and 14 at start of quarter", ReportUtils.map(femalesPedsFiveToFourteenCohort, "reportDate=${reportDate},locationList=${locationList}"));
        CohortIndicator femalesPedsFiveToFourteenCohortAtEndIndicator = CommonIndicatorLibrary.createSQLCohortIndicator("Indicator for Female peds btw 5 and 14 at end of quarter", ReportUtils.map(femalesPedsFiveToFourteenCohort, "reportDate=${endDate},locationList=${locationList}"));

        CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
        dsd.addParameter(new Parameter("reportDate", "Report Date", Date.class));
       */
/* dsd.addParameter(new Parameter("minAge", "Min Age", Integer.class));
        dsd.addParameter(new Parameter("maxAge", "Max Age", Integer.class));*//*

        dsd.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        dsd.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        //set reportDefinition params
        report.addParameter(new Parameter("reportDate", "Report Date", Date.class));
        */
/*report.addParameter(new Parameter("minAge", "Min Age", Integer.class));
        report.addParameter(new Parameter("maxAge", "Max Age", Integer.class));*//*

        report.addParameter(new Parameter("endDate", "End Reporting Date", Date.class));
        report.addParameter(new Parameter("locationList", "List of Locations", Location.class));

        //dsd.addDimension("compositionDimension", new Mapped<CohortDefinitionDimension>(compositionDimension,dimensionMappings));
        dsd.addColumn("malesBelow15", "Males Below 15", new Mapped<CohortIndicator>(malesBelow15CohortAtStartIndicator, paramMappings), "");
        dsd.addColumn("malesAbove15", "Males 15 or more", new Mapped<CohortIndicator>(malesAbove15CohortAtStartIndicator, paramMappings), "");
        dsd.addColumn("femalesBelow15", "Females Below 15", new Mapped<CohortIndicator>(femalesBelow15CohortAtStartIndicator, paramMappings), "");
        dsd.addColumn("femalesAbove15", "Females 15 or more", new Mapped<CohortIndicator>(femalesAbove15CohortAtStartIndicator, paramMappings), "");
        //Make second  column
        dsd.addColumn("NEWmalesBelow15", "Males Below 15", new Mapped<CohortIndicator>(malesBelow15CohortAtEndIndicator, paramMappings), "");
        dsd.addColumn("NEWmalesAbove15", "Males 15 or more", new Mapped<CohortIndicator>(malesAbove15CohortAtEndIndicator, paramMappings), "");
        dsd.addColumn("NEWfemalesBelow15", "Females Below 15", new Mapped<CohortIndicator>(femalesBelow15CohortAtEndIndicator, paramMappings), "");
        dsd.addColumn("NEWfemalesAbove15", "Females 15 or more", new Mapped<CohortIndicator>(femalesAbove15CohortAtEndIndicator, paramMappings), "");

        */
/**
         * Add columns for peds
         *//*

        dsd.addColumn("malesPedsAt1", "Male Peds up to one year", new Mapped<CohortIndicator>(malesPedsZeroToOneCohortAtStartIndicator, paramMappings), "");
        dsd.addColumn("malesPedsBtw2n4", "Males peds between 2 and 4", new Mapped<CohortIndicator>(malesPedsTwoToFourCohortAtStartIndicator, paramMappings), "");
        dsd.addColumn("malesPedsBtw5n14", "Male Ped btw 5 and 14", new Mapped<CohortIndicator>(malesPedsFiveToFourteenCohortAtStartIndicator, paramMappings), "");
        dsd.addColumn("femalesPedsAt1", "Female peds at one ", new Mapped<CohortIndicator>(femalesPedsZeroToOneCohortAtStartIndicator, paramMappings), "");
        dsd.addColumn("femalesPedsBtw2n4", "Female peds  btw 2 and 4", new Mapped<CohortIndicator>(femalesPedsTwoToFourCohortAtStartIndicator, paramMappings), "");
        dsd.addColumn("femalesPedsBtw5n14", "Female peds between 5 and 14", new Mapped<CohortIndicator>(femalesPedsFiveToFourteenCohortAtStartIndicator, paramMappings), "");

        */
/**
         * Fill second column for peds
         *//*

        dsd.addColumn("NEWmalesPedsAt1", "Male Peds up to one year", new Mapped<CohortIndicator>(malesPedsZeroToOneCohortAtEndIndicator, paramMappings), "");
        dsd.addColumn("NEWmalesPedsBtw2n4", "Males peds between 2 and 4", new Mapped<CohortIndicator>(malesPedsTwoToFourCohortAtEndIndicator, paramMappings), "");
        dsd.addColumn("NEWmalesPedsBtw5n14", "Male Ped btw 5 and 14", new Mapped<CohortIndicator>(malesPedsFiveToFourteenCohortAtEndIndicator, paramMappings), "");
        dsd.addColumn("NEWfemalesPedsAt1", "Female peds at one ", new Mapped<CohortIndicator>(femalesPedsZeroToOneCohortAtEndIndicator, paramMappings), "");
        dsd.addColumn("NEWfemalesPedsBtw2n4", "Female peds  btw 2 and 4", new Mapped<CohortIndicator>(femalesPedsTwoToFourCohortAtEndIndicator, paramMappings), "");
        dsd.addColumn("NEWfemalesPedsBtw5n14", "Female peds between 5 and 14", new Mapped<CohortIndicator>(femalesPedsFiveToFourteenCohortAtEndIndicator, paramMappings), "");


        //add columns for medication
        */
/*dsd.addColumn("malesBelow15onMedication", "Males below 15 on Medication", new Mapped<CohortIndicator>(malesBelow15OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("malesAbove15onMedication", "Males 15+ on Medication", new Mapped<CohortIndicator>(malesAbove15OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("femalesBelow15onMedication", "FeMales 15+ on Medication", new Mapped<CohortIndicator>(femalesBelow15OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("femalesAbove15onMedication", "FeMales below 15 on Medication", new Mapped<CohortIndicator>(femalesAbove15OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("malesPedsAt1onMedication", "peds below 2 on Medication", new Mapped<CohortIndicator>(pedsMalesZeroTo1OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("femalesPedsAt1onMedication", "Males 15+ on Medication", new Mapped<CohortIndicator>(pedsFemalesZeroTo1OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("malesPedsBtw2n4onMedication", "Males 15+ on Medication", new Mapped<CohortIndicator>(pedsmales2To4OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("femalesPedsBtw2n4onMedication", "Males 15+ on Medication", new Mapped<CohortIndicator>(pedsFemales2To4OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("malesPedsBtw5n14onMedication", "Males 15+ on Medication", new Mapped<CohortIndicator>(pedsmales5To14OnMedCohortIndi, periodMappings), "");
        dsd.addColumn("femalesPedsBtw5n14onMedication", "Males 15+ on Medication", new Mapped<CohortIndicator>(pedsFemales5To14OnMedCohortIndi, periodMappings), "");*//*


		report.addDataSetDefinition(dsd, paramMappings);

		return report;
	}

	@Override
	public CohortDefinition getCohortDefinition() {
		return new CCCPatientCohortDefinition();
	}

	@Override
	public ReportDesign getReportDesign() {
		ReportDesign design = new ReportDesign();
		design.setName("Hiv Palliative Care Design");
		design.setReportDefinition(this.getReportDefinition());
		design.setRendererType(ExcelTemplateRenderer.class);

		ReportDesignResource resource = new ReportDesignResource();
		resource.setName("template.xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream("templates/Quarterly_Facility-BasedHIVCare-template.xls");

		if (is == null)
			throw new APIException("Could not find report template.");

		try {
			resource.setContents(IOUtils.toByteArray(is));
		} catch (IOException ex) {
			throw new APIException("Could not create report design for MOH 361B Register.", ex);
		}

		IOUtils.closeQuietly(is);
		design.addResource(resource);

		return design;
	}

*/

}