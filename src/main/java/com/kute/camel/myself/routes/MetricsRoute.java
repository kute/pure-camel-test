package com.kute.camel.myself.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class MetricsRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // 配置 http请求域名端口
        restConfiguration()
                .scheme("http")
                .host("0.0.0.0")
                .port(8080)
                .enableCORS(true)
                .bindingMode(RestBindingMode.json);

        // 请求 /actuator/mappings 端点url
        from("timer:queryTimer?repeatCount=1")
                .process(exchange -> log.info("properties value={}", exchange.getProperties()))
                .to("rest:get:/actuator/mappings")
                .unmarshal()
                .json(true)
                .to("log:INFO?multiline=true");

        from("timer:metricsTimer?period=2s")
                .to("rest:get:/actuator/metrics/system.cpu.usage")
                .unmarshal()
                .json(true)
                .to("log:INFO?multiline=true");
    }
}
