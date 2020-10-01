package com.kute.camel.dynamic_route;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.DynamicRouter;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * created by bailong001 on 2020/09/30 10://33
 * 动态计算路由，与 routingslip的区别在于，routingslip需要预先计算好所有的路由，而 dynamic route是在每一次发送前计算
 */
@Slf4j
@Component
public class DynamicComputeBean {

    public String compute(@Body String body, @ExchangeProperties Map<String, String> propertyMap) {
        log.info("Dynamic route compute with body={}, propertyMap={}", body, propertyMap);
        String previous_route = propertyMap.get(Exchange.SLIP_ENDPOINT);
        if (Strings.isNullOrEmpty(previous_route) || "direct://start".equals(previous_route)) {
            return "direct://A";
        } else if ("direct://A".equals(previous_route)) {
            return "direct://C";
        } else if ("direct://C".equals(previous_route)) {
            return "direct://B";
        }
        // return null indicate to end dynamic route
        return null;
    }

    @DynamicRouter
    public String computeAnnotation(@Body String body, @ExchangeProperties Map<String, String> propertyMap) {
        log.info("Dynamic route compute with body={}, propertyMap={}", body, propertyMap);
        String previous_route = propertyMap.get(Exchange.SLIP_ENDPOINT);
        if (Strings.isNullOrEmpty(previous_route) || "direct://start".equals(previous_route)) {
            return "direct://A";
        } else if ("direct://A".equals(previous_route)) {
            return "direct://C";
        } else if ("direct://C".equals(previous_route)) {
            return "direct://B";
        }
        // return null indicate to end dynamic route
        return null;
    }

}
