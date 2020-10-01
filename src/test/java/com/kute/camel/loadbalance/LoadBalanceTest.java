package com.kute.camel.loadbalance;

import com.kute.camel.AbstractTest;
import com.kute.camel.routing_slip.ComputeSlipBean;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.loadbalancer.RoundRobinLoadBalancer;
import org.apache.camel.processor.loadbalancer.StickyLoadBalancer;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.stream.IntStream;

/**
 * created by bailong001 on 2020/09/28 19:27
 * <p>
 * 负载均衡策略类型：org.apache.camel.processor.loadbalancer.LoadBalancer 的实现类
 */
@SpringBootTest
@ComponentScan(value = "com.kute.camel")
public class LoadBalanceTest extends AbstractTest {

    @Autowired
    private CamelContext camelContext;

    @BeforeEach
    public void before() throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:A")
                        .log("receive A meesage=${body}, header=${header.level}");
            }
        });

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:B")
                        .log("receive B meesage=${body}, header=${header.level}");
            }
        });

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:C")
                        .log("receive C meesage=${body}, header=${header.level}");
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

//                        .loadBalance()
//                            .roundRobin()
//                                .to("direct:B").to("direct:C").to("direct:A")

                        .loadBalance(new RoundRobinLoadBalancer())
                        .to("direct:B").to("direct:C").to("direct:A")

                        .end();
            }
        });

        ProducerTemplate template = camelContext.createProducerTemplate();
        IntStream.rangeClosed(1, 7).forEach(i -> {
            template.requestBody("direct:start", "hello world");
        });
        camelContext.stop();
    }

    @Test
    public void testSticky() throws Exception {

        // hash load balance

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .log("receive start meesage=${body}")

                        .loadBalance()
                        .sticky(header("level"))
                        .to("direct:B").to("direct:C").to("direct:A")

                        .end();
            }
        });

        ProducerTemplate template = camelContext.createProducerTemplate();
        IntStream.rangeClosed(1, 7).forEach(i -> {
            template.requestBodyAndHeader("direct:start", "hello world", "level", RandomUtils.nextInt(1, 4));
        });
        camelContext.stop();
    }

    @Test
    public void testFailover() throws Exception {

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .log("receive start meesage=${body}")

                        .loadBalance()
                        .failover() // B出现任何异常，将会路由到C，否则一直只会路由到B
                        .failover(IOException.class, ClassCastException.class)// 指定异常
                        .to("direct:B").to("direct:C")

                        .end();
            }
        });
    }

}
