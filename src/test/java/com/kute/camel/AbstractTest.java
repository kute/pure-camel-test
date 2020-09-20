package com.kute.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.SimpleRegistry;

import java.util.concurrent.TimeUnit;

/**
 * created by kute at 2020/8/31 10:14 下午
 */
public class AbstractTest {

    protected static void superTest(RouteBuilder routeBuilder, int sleep) {
        CamelContext context = new DefaultCamelContext();
        superTest(routeBuilder, context);
    }

    protected static void superTest(RouteBuilder routeBuilder, CamelContext context) {
        // 日志脱敏
        context.setLogMask(true);
        try {
            context.addRoutes(routeBuilder);
            context.start();
            TimeUnit.SECONDS.sleep(5);
            context.stop();
        } catch (Exception e) {

        } finally {
            context.stop();
        }
    }

    protected static void superTest(RouteBuilder routeBuilder) {
        superTest(routeBuilder, 5);
    }

}
