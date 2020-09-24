package com.kute.camel.util;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by bailong001 on 2020/09/23 11:06
 */
public final class CommonUtil {

    private CommonUtil() {
    }


    public static String toUriParam(Map<String, ?> map) {
        return map.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue().toString())
                .collect(Collectors.joining("&"));
    }

}
