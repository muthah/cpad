package org.openmrs.module.amrsreports.reporting.converter;

import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.module.reporting.data.converter.DataConverter;

/**
 * Converter to pull just the location out of an encounter
 */
public class OnARTOrCareConverter implements DataConverter {
	@Override
	public Object convert(Object original) {
		String e = (String) original;

		if (e == null)
			return null;
        String response = e.equals("Yes")?"ART":"Care";

		return response;
	}

	@Override
	public Class<?> getInputDataType() {
		return String.class;
	}

	@Override
	public Class<?> getDataType() {
		return String.class;
	}
}
