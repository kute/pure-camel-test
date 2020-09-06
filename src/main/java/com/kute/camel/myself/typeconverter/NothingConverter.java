package com.kute.camel.myself.typeconverter;

import org.apache.camel.*;
import org.apache.camel.support.TypeConverterSupport;

/**
 * created by kute at 2020/8/2 11:04 上午
 */
public class NothingConverter extends TypeConverterSupport {

    @Override
    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
        if (!NothingType.class.isAssignableFrom(type)) {
            return (T) MISS_VALUE;
        }

        // reuse converter before
        TypeConverter typeConverter = exchange.getContext().getTypeConverter();
        String newValue = typeConverter.convertTo(String.class, value);

        return (T) NothingType.builder().value(newValue).build();
    }
}
