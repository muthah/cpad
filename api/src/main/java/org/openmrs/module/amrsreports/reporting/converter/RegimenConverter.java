package org.openmrs.module.amrsreports.reporting.converter;

import org.openmrs.module.amrsreports.model.RegimenObject;
import org.openmrs.module.reporting.data.converter.DataConverter;

import java.util.Set;

/**
 * Converter to pull just the location out of an encounter
 */
public class RegimenConverter implements DataConverter {
    private Boolean regName;

    public RegimenConverter(Boolean regName) {
        this.regName = regName;
    }

    @Override
	public Object convert(Object original) {
		RegimenObject e = (RegimenObject) original;

		if (e == null)
			return null;

        String response = regName?e.getRegimenName():e.getRegimenType();

		return response;
	}

	@Override
	public Class<?> getInputDataType() {
		return RegimenObject.class;
	}

	@Override
	public Class<?> getDataType() {
		return String.class;
	}

    public Boolean getRegName() {
        return regName;
    }

    public void setRegName(Boolean regName) {
        this.regName = regName;
    }
}
