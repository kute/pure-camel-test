package com.kute.camel.split;

import com.google.common.collect.Lists;
import com.kute.camel.AbstractTest;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

/**
 * created by bailong001 on 2020/09/22 17:41
 */
@SpringBootTest
@ComponentScan(value = "com.kute.camel")
public class SplitTest extends AbstractTest {

    @Autowired
    private CamelContext camelContext;

    @Test
    public void test() throws Exception {
        String directEndpoint = "direct:start";

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(directEndpoint)
                        // 这里的 aggregationStrategy是对split后的结果再进行 聚合策略运算的
                        .split(body(), AggregationStrategies.string(","))
                        .to("log:AggregationTest?showAll=true&multiline=true");
            }
        });

        ProducerTemplate template = camelContext.createProducerTemplate();

        template.requestBody(directEndpoint, Lists.newArrayList("1", 2, "4"));

        template.requestBody(directEndpoint, Lists.newArrayList(Lists.newArrayList("11", 12, "14"), Lists.newArrayList("1", 2, "4")));

        camelContext.stop();
    }

}
