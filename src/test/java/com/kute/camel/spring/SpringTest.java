package com.kute.camel.spring;

import com.kute.camel.PureCamelTestApplication;
import com.kute.camel.myself.routes.MetricsRoute;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.TypeConverter;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * created by bailong001 on 2020/10/19 14:53
 */
@CamelSpringBootTest
@SpringBootTest(classes = PureCamelTestApplication.class)
@Slf4j
public class SpringTest {

    @Autowired
    private CamelContext camelContext;
    @Autowired
    private ProducerTemplate producerTemplate;
    @Autowired
    private ConsumerTemplate consumerTemplate;
    @Autowired
    private TypeConverter typeConverter;

    @Test
    public void test() throws Exception {
        log.info("=======" + camelContext.getName());
        log.info("=======" + camelContext.getComponentNames());
    }

}
