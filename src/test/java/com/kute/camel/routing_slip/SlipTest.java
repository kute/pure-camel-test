package com.kute.camel.routing_slip;

import com.kute.camel.AbstractTest;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

/**
 * created by bailong001 on 2020/09/28 19:27
 * <p>
 * 根据消息动态路由到若干个节点
 */
@SpringBootTest
@ComponentScan(value = "com.kute.camel")
public class SlipTest extends AbstractTest {

    @Autowired
    private CamelContext camelContext;
    @Autowired
    private ProducerTemplate template;

    @BeforeEach
    public void before() throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:A")
                        .log("receive A meesage=${body}")
                        .process(exchange -> {
//                            Exchange.SLIP_ENDPOINT;
//                            Exchange.SLIP_PRODUCER;
//                            headers={sliplist=direct:A,direct:C}, properties={CamelSlipEndpoint=direct://A, CamelToEndpoint=direct://A, CamelSlipProducer=Producer[direct://A]}
                            log.info("receive A with headers={}, properties={}",
                                    exchange.getMessage().getHeaders(), exchange.getProperties());
                        });
            }
        });

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:B")
                        .log("receive B meesage=${body}");
            }
        });

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:C")
                        .log("receive C meesage=${body}");
            }
        });
    }

    @Test
    public void test() throws Exception {

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .log("receive start meesage=${body}")
//                        默认逗号分隔
//                        .routingSlip(header("sliplist"))
                        .routingSlip(header("sliplist"), ",");
            }
        });

        template.requestBodyAndHeader("direct:start", "hello world", "sliplist", "direct:A,direct:C");
        camelContext.stop();
    }

    @Test
    public void testWithBean() throws Exception {

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .log("receive start meesage=${body}")
                        // 使用bean计算 routingslip
                        .routingSlip(method(new ComputeSlipBean(), "compute"));
            }
        });

        template.requestBodyAndHeader("direct:start", "hello world", "count", 5);
        camelContext.stop();
    }

    @Test
    public void testWithBeanAnnotation() throws Exception {

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .log("receive start meesage=${body}")
                        //使用注解来声明 routingslip bean method，就直接使用bean(。。。)
                        .bean(new ComputeSlipBean(), "annotationMethod");
            }
        });

        template.requestBodyAndHeader("direct:start", "hello world", "count", 5);
//        template.requestBodyAndHeader("bean:com.kute.camel.slip.ComputeSlipBean?method=annotationMethod", "hello world", "count", 5);
        camelContext.stop();
    }
}
