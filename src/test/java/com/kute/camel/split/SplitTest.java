package com.kute.camel.split;

import com.google.common.collect.Lists;
import com.kute.camel.AbstractTest;
import com.kute.camel.model.Customer;
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
                        // 任何可遍历的都可以split，如 Collection, Iterator, Iterable, org.w3c.dom, NodeList, String (with entries separated by commas) and arrays
                        .split(body(), AggregationStrategies.string(","))
                        .process(exchange -> {
                            /**
                             * Exchange.SPLIT_INDEX
                             * Exchange.SPLIT_SIZE
                             * Exchange.SPLIT_COMPLETE
                             * ...
                             */
                            log.info("split with properties={}", exchange.getProperties());
                        })
                        .to("log:AggregationTest?showAll=true&multiline=true");
            }
        });

        ProducerTemplate template = camelContext.createProducerTemplate();

        template.requestBody(directEndpoint, Lists.newArrayList("1", 2, "4"));

        template.requestBody(directEndpoint, Lists.newArrayList(Lists.newArrayList("11", 12, "14"), Lists.newArrayList("1", 2, "4")));

        camelContext.stop();
    }


    @Test
    public void test2() throws Exception {
        String directEndpoint = "direct:start";

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(directEndpoint)
                        // use bean to split instead of body()
//                        .split(method(SomeSplitBean.class, "splitDepartments"))
//                        .split().method(SomeSplitBean.class, "splitDepartments")

                        // use simple language
                        .split(simple("${body.departments}"))

                        .stopOnException()

                        .stopOnAggregateException()

                        .process(exchange -> {
                            /**
                             * Exchange.SPLIT_INDEX
                             * Exchange.SPLIT_SIZE
                             * Exchange.SPLIT_COMPLETE
                             * ...
                             */
                            log.info("split with properties={}", exchange.getProperties());
                        })
                        .to("log:AggregationTest?showAll=true&multiline=true");
            }
        });

        ProducerTemplate template = camelContext.createProducerTemplate();

        template.requestBody(directEndpoint, Customer.newInstance());

        camelContext.stop();
    }

    @Test
    public void testStream() throws Exception {

        //TODO split with stream
        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:data/inbox")
                        .log("begin deal with file-name=${header.CamelFileName}")
                        .split(body().tokenize("\n"))
//                        .streaming()
//                        .end()
                        .to("log:logTest?level=INFO&showAll=true&multiline=true");
            }
        });
    }

}
