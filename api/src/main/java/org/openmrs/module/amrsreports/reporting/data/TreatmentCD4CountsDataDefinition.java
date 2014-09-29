package org.openmrs.module.amrsreports.reporting.data;

import org.openmrs.module.reporting.data.BaseDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

import java.util.List;
import java.util.Set;

/**
 * Evaluates age of a person at the date of report evaluation, provided by the context
 */
@Caching(strategy=ConfigurationPropertyCachingStrategy.class)
public class TreatmentCD4CountsDataDefinition extends BaseDataDefinition implements PersonDataDefinition {

	public static final long serialVersionUID = 1L;
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


	/**
	 * Default Constructor
	 */
	public TreatmentCD4CountsDataDefinition() {
		super();
	}

	/**
	 * Constructor to populate name only
	 */
	public TreatmentCD4CountsDataDefinition(String name) {
		super(name);
	}

	//***** INSTANCE METHODS *****

	/**
	 * @see org.openmrs.module.reporting.data.DataDefinition#getDataType()
	 */
	public Class<?> getDataType() {
		return Set.class;
	}

}