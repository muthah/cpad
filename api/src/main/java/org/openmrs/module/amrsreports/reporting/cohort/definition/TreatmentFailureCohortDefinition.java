package org.openmrs.module.amrsreports.reporting.cohort.definition;

import org.openmrs.module.reporting.cohort.definition.BaseCohortDefinition;
import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * MOH 361B Register cohort definition
 */
@Caching(strategy = ConfigurationPropertyCachingStrategy.class)
@Localized("reporting.TreatmentFailureCohortDefinition")
public class TreatmentFailureCohortDefinition extends BaseCohortDefinition {
	private Integer minAge;
	private Integer maxAge;
	private Double value1;
	private Double value2;

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public Double getValue1() {
		return value1;
	}

	public void setValue1(Double value1) {
		this.value1 = value1;
	}

	public Double getValue2() {
		return value2;
	}

	public void setValue2(Double value2) {
		this.value2 = value2;
	}

}