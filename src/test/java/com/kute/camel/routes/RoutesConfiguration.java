package com.kute.camel.routes;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by bailong001 on 2020/10/09 12:56
 */
@Configuration
@Slf4j
public class RoutesConfiguration {

    @Bean
    public CamelContextConfiguration camelContextConfiguration() {
        return new CamelContextConfiguration() {
            // called just before the Spring context is started
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                log.info("Camel beforeApplicationStart for camelContext={}", camelContext);
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                log.info("Camel afterApplicationStart for camelContext={}", camelContext);
            }
        };
    }

    @Bean
    public RouteBuilder t1Route() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // property config
                from("{{t1route.from}}")
                        .to("{{t1route.to}}");
            }
        };
    }

}
