package com.kute.camel;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.boot.SpringBootCamelContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * created by kute at 2020/10/1 5:48 下午
 */
@SpringBootTest
@ComponentScan(value = "com.kute.camel")
public class SchedulerTest extends AbstractTest {

    @Autowired
    private CamelContext camelContext;
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void test() throws Exception {

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("scheduler:myScheduler?delay=2000&repeatCount=10&exchangePattern=InOut&scheduledExecutorService=#myExecutorService")
                        .setBody(simple("Current time is${header.CamelTimerFiredTime}"))
                        .to("stream:out");
            }
        });

        TimeUnit.SECONDS.sleep(10);

        camelContext.stop();

    }
}
