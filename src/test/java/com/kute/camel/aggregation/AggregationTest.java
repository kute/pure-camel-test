package com.kute.camel.aggregation;

import com.kute.camel.AbstractTest;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.stream.IntStream;

/**
 * created by kute at 2020/9/19 8:35 下午
 * aggregation 需设置三步：correlation identifier、completion condition、aggregation strategy
 */
@SpringBootTest
@ComponentScan(value = "com.kute.camel")
public class AggregationTest extends AbstractTest {

    @Autowired
    private CamelContext camelContext;

    @Test
    public void test() throws Exception {
        String directEndpoint = "direct:start";
        String identifier = "unique-id";

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(directEndpoint)
                        .log(LoggingLevel.INFO, "receive message=${body} with header unique-id=${header.unique-id}")
                        // 同步聚合，保证 aggregationStrategy线程安全
//                        .aggregate(header(identifier), new StringAggregationStrategy().delimiter(",").pick(body()))
                        .aggregate(header(identifier), new JsonAggregationStrategy().pickBody(body()))
                        // 达到指定大小，开始聚合
                        .completionSize(3)
                        // 超过指定时间未接收消息，开始聚合
//                        .completionTimeout(1000)
                        // 周期性聚合，会出现同一时间所有的group聚合发送
//                        .completionInterval(1000)
                        .to("log:AggregationTest?showAll=true&multiline=true");
            }
        });

        ProducerTemplate template = camelContext.createProducerTemplate();
        IntStream.range(1, 50).forEach(i -> {
            template.requestBodyAndHeader(directEndpoint, "jsdkf-" + i, identifier, RandomUtils.nextInt(1, 3));
        });

        camelContext.stop();
    }

}
