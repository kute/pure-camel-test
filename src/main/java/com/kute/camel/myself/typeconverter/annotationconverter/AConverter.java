package com.kute.camel.myself.typeconverter.annotationconverter;

import org.apache.camel.Converter;

/**
 * created by kute at 2020/9/6 10:11 上午
 * fallback=true表示 找不到合适的converter就使用该converter，即 兜底converter
 */
@Converter(fallback = true)
public final class AConverter {

    @Converter
    public static String convert(String body) {
        return body;
    }
}
