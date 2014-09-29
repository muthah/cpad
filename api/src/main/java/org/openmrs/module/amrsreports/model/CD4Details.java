package org.openmrs.module.amrsreports.model;

import java.util.Date;

/**
 * POJO for holding cd4 related info
 */
public class CD4Details {
	private Date obs_datetime;
	private Double value_numeric;

	public CD4Details() {
	}

	public CD4Details(Double value_numeric, Date obs_datetime) {
		this.value_numeric = value_numeric;
		this.obs_datetime = obs_datetime;
	}

	public Date getObs_datetime() {
		return obs_datetime;
	}

	public void setObs_datetime(Date obs_datetime) {
		this.obs_datetime = obs_datetime;
	}

	public Double getValue_numeric() {
		return value_numeric;
	}

	public void setValue_numeric(Double value_numeric) {
		this.value_numeric = value_numeric;
	}
}
