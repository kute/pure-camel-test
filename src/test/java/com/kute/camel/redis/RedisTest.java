package com.kute.camel.redis;

import com.kute.camel.AbstractTest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * created by kute at 2020/9/7 9:56 下午
 */
@SpringBootTest
public class RedisTest extends AbstractTest {


    @Test
    public void test() {

        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer://foo?fixedRate=true&period=1000")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                System.out.println(exchange.getProperties());
                                exchange.getMessage().setBody(exchange.getMessage().getHeader("CamelTimerCounter"));
                            }
                        })
                        .to("bean:com.kute.camel.spring.HelloBean?method=hello");
            }
        });


    }

}
