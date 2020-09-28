package com.kute.camel.routing_slip;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Header;
import org.apache.camel.RoutingSlip;

/**
 * created by bailong001 on 2020/09/28 19:36
 */
@Slf4j
public class ComputeSlipBean {

    public String compute(@Header("count") Integer count) {
        log.info("Camel routingslip begin compute for count={}", count);
        StringBuffer buffer = new StringBuffer("direct:A");
        if (count != null && count < 4) {
            buffer.append(",direct:B");
        }
        buffer.append(",direct:C");
        return buffer.toString();
    }

    /**
     * 使用注解来声明 此method 作为 routingslip
     *
     * @param count
     * @return
     */
    @RoutingSlip(delimiter = ",", ignoreInvalidEndpoints = true)
    public String annotationMethod(@Header("count") Integer count) {
        log.info("Camel routingslip begin compute for count={}", count);
        StringBuffer buffer = new StringBuffer("direct:A");
        if (count != null && count < 4) {
            buffer.append(",direct:B");
        }
        buffer.append(",direct:C");
        return buffer.toString();
    }

}
