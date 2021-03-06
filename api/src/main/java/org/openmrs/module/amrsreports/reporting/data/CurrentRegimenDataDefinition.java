package org.openmrs.module.amrsreports.reporting.data;

import org.openmrs.module.amrsreports.model.RegimenObject;
import org.openmrs.module.amrsreports.model.SortedObsFromDate;
import org.openmrs.module.reporting.data.BaseDataDefinition;
import org.openmrs.module.reporting.data.MappedData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.definition.configuration.ConfigurationProperty;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * TB Status evaluation
 */
@Caching(strategy = ConfigurationPropertyCachingStrategy.class)
public class CurrentRegimenDataDefinition extends BaseDataDefinition implements PersonDataDefinition {

    @Override
    public Class<?> getDataType() {
        return RegimenObject.class;
    }
}
