package com.kute.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * created by kute at 2020/8/31 10:14 下午
 */
public class AbstractTest {

    protected static void superTest(RouteBuilder routeBuilder, int sleep) {

        try {
            CamelContext camelContext = new DefaultCamelContext();
            camelContext.addRoutes(routeBuilder);
            camelContext.start();
            TimeUnit.SECONDS.sleep(sleep);
            camelContext.stop();
        } catch (Exception e) {

        }
    }

    protected static void superTest(RouteBuilder routeBuilder) {
        superTest(routeBuilder, 5);
    }

}
