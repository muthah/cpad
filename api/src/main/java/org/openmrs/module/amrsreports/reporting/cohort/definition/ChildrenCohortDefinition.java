package org.openmrs.module.amrsreports.reporting.cohort.definition;

import org.openmrs.module.reporting.cohort.definition.BaseCohortDefinition;
import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * MOH 361B Register cohort definition
 */
@Caching(strategy = ConfigurationPropertyCachingStrategy.class)
@Localized("reporting.ChildrenCohortDefinition")
public class ChildrenCohortDefinition extends BaseCohortDefinition {

    private Integer minAge;
    private Integer maxAge;

    public ChildrenCohortDefinition(Integer maxAge){
        setMaxAge(maxAge);
    }

    public ChildrenCohortDefinition(Integer minAge, Integer maxAge){
        setMinAge(minAge);
        setMaxAge(maxAge);
    }

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
}
