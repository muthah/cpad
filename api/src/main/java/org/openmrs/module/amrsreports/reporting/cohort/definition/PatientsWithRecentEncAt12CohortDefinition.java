package org.openmrs.module.amrsreports.reporting.cohort.definition;

import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * MOH 361B Register cohort definition
 */
@Caching(strategy = ConfigurationPropertyCachingStrategy.class)
@Localized("reporting.PatientsWithRecentEncAt12CohortDefinition")
public class PatientsWithRecentEncAt12CohortDefinition extends AMRSReportsCohortDefinition {

}
