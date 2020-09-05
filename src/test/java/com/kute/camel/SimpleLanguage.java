package com.kute.camel;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * created by kute at 2020/8/31 10:03 下午
 * <p>
 * Camel的表达式语言: org.apache.camel.builder.SimpleBuilder
 */
public class SimpleLanguage extends AbstractTest {

    public static void main(String[] args) {

        RouteBuilder routeBuilder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("file:data/inbox?noop=true")
                        .log(LoggingLevel.INFO, "receive message: ${header.CamelFileName}")
                        .choice()
                        .when(simple("${header.CamelFileName} ends with 'xml'"))
                        .to("file:data/outbox/xml")
                        .when(simple("${header.CamelFileName} ends with 'txt'"))
                        .to("file:data/outbox/txt")
                        .end()
                        .to("file:data/outbox/all");

            }
        };
        superTest(routeBuilder);
    }

}
