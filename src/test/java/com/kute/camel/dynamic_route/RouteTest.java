package com.kute.camel.dynamic_route;

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
 * created by bailong001 on 2020/09/30 10:31
 */
@SpringBootTest
@ComponentScan(value = "com.kute.camel")
public class RouteTest extends AbstractTest {

    @Autowired
    private CamelContext camelContext;
    @Autowired
    private DynamicComputeBean dynamicComputeBean;

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
                        .log("receive B meesage=${body}")
                        .process(exchange -> {
//                            Exchange.SLIP_ENDPOINT;
//                            Exchange.SLIP_PRODUCER;
//                            headers={sliplist=direct:A,direct:C}, properties={CamelSlipEndpoint=direct://A, CamelToEndpoint=direct://A, CamelSlipProducer=Producer[direct://A]}
                            log.info("receive B with headers={}, properties={}",
                                    exchange.getMessage().getHeaders(), exchange.getProperties());
                        });
            }
        });

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:C")
                        .log("receive C meesage=${body}")
                        .process(exchange -> {
//                            Exchange.SLIP_ENDPOINT;
//                            Exchange.SLIP_PRODUCER;
//                            headers={sliplist=direct:A,direct:C}, properties={CamelSlipEndpoint=direct://A, CamelToEndpoint=direct://A, CamelSlipProducer=Producer[direct://A]}
                            log.info("receive C with headers={}, properties={}",
                                    exchange.getMessage().getHeaders(), exchange.getProperties());
                        });
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
                        .dynamicRouter(method(dynamicComputeBean, "compute"));
            }
        });

        ProducerTemplate template = camelContext.createProducerTemplate();
        template.requestBody("direct:start", "hello world");
        camelContext.stop();
    }

    @Test
    public void testWithAnnotation() throws Exception {

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("direct:start")
                        .log("receive start meesage=${body}")
                        .bean(dynamicComputeBean, "computeAnnotation");
            }
        });

        ProducerTemplate template = camelContext.createProducerTemplate();
        template.requestBody("direct:start", "hello world");
        camelContext.stop();
    }

}
