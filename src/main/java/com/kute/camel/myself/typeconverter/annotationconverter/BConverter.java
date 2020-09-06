package com.kute.camel.myself.typeconverter.annotationconverter;

import org.apache.camel.Converter;

/**
 * created by kute at 2020/9/6 10:11 上午
 */
@Converter
public final class BConverter {
    @Converter(allowNull = true)
    public static String convert(String body) {
        return body;
    }
}
