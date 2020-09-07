package com.kute.camel.spring;

import com.kute.camel.AbstractTest;
import com.kute.camel.spring.HelloBean;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

/**
 * created by kute at 2020/9/6 4:27 下午
 */
@SpringBootTest
@ComponentScan(value = "com.kute.camel")
public class BeanTest extends AbstractTest {

    @Autowired
    private HelloBean helloBean;

    @Test
    public void test() {
        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:data/inbox?noop=true")
                        .bean(helloBean, "hello")
                        .to("file:data/outbox");
            }
        });
    }

}
