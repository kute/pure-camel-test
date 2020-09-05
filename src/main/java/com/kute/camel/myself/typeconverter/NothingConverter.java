package com.kute.camel.myself.typeconverter;

import org.apache.camel.Exchange;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.TypeConversionException;
import org.apache.camel.TypeConverter;

/**
 * created by kute at 2020/8/2 11:04 上午
 */
public class NothingConverter implements TypeConverter {
	@Override
	public boolean allowNull() {
		return false;
	}

	@Override
	public <T> T convertTo(Class<T> type, Object value) throws TypeConversionException {
		return null;
	}

	@Override
	public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
		return null;
	}

	@Override
	public <T> T mandatoryConvertTo(Class<T> type, Object value) throws TypeConversionException, NoTypeConversionAvailableException {
		return null;
	}

	@Override
	public <T> T mandatoryConvertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException, NoTypeConversionAvailableException {
		return null;
	}

	@Override
	public <T> T tryConvertTo(Class<T> type, Object value) {
		return null;
	}

	@Override
	public <T> T tryConvertTo(Class<T> type, Exchange exchange, Object value) {
		return null;
	}
}
