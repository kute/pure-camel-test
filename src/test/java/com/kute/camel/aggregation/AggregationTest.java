package com.kute.camel.aggregation;

import com.kute.camel.AbstractTest;
import org.apache.camel.*;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;
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
    @Autowired
    private JsonAggregationStrategyBean jsonAggregationStrategyBean;

    @Test
    public void test() throws Exception {
        String directEndpoint = "direct:start";
        String identifier = "unique-id";

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(directEndpoint)
                        .log(LoggingLevel.INFO, "receive message=${body} with header unique-id=${header.unique-id}")
                        .process(exchange -> {
                            // 额外的聚合参数配置
                            exchange.setProperty("ignoreInvalidCorrelationKeys", true);
                        })
                        // 同步聚合，保证 aggregationStrategy线程安全

//                        .aggregate(header(identifier), new StringAggregationStrategy().delimiter(",").pick(body()))
//                        .aggregate(header(identifier), AggregationStrategies.string(","))

                        // 实现自定义的json聚合策略
//                        .aggregate(header(identifier), new JsonAggregationStrategy().pickBody(body()))
                        // 不使用AggregationStrategy接口，使用普通的bean实现 json聚合策略：注意这里允许第一次的消息为空，即 第一次消息到来时，old是空的
//                        .aggregate(header(identifier), AggregationStrategies.beanAllowNull(jsonAggregationStrategyBean, "jsonAppendAllowNull"))
                        // 这里会忽略到第一次空的消息，即 聚合时 old永远不为空
                        .aggregate(header(identifier), AggregationStrategies.bean(jsonAggregationStrategyBean, "jsonAppendNotAllNull"))
//                        .aggregationRepository(new RedisAggregationRepository())
                        // 达到指定大小，开始聚合
                        .completionSize(3)
                        // 超过指定时间未接收消息，开始聚合
                        .completionTimeout(1000)
                        // 周期性聚合，会出现同一时间所有的group聚合发送
//                        .completionInterval(1000)
                        /**
                         * 访问 聚合过程中的一些属性，可以从中得知聚合的策略，如 有多个终止条件时哪个条件被触发
                         * String AGGREGATED_SIZE = "CamelAggregatedSize";
                         *     String AGGREGATED_TIMEOUT = "CamelAggregatedTimeout";
                         *     String AGGREGATED_COMPLETED_BY = "CamelAggregatedCompletedBy";
                         *     String AGGREGATED_CORRELATION_KEY = "CamelAggregatedCorrelationKey";
                         *     String AGGREGATED_COLLECTION_GUARD = "CamelAggregatedCollectionGuard";
                         *     String AGGREGATION_STRATEGY = "CamelAggregationStrategy";
                         *     String AGGREGATION_COMPLETE_CURRENT_GROUP = "CamelAggregationCompleteCurrentGroup";
                         *     String AGGREGATION_COMPLETE_ALL_GROUPS = "CamelAggregationCompleteAllGroups";
                         *     String AGGREGATION_COMPLETE_ALL_GROUPS_INCLUSIVE = "CamelAggregationCompleteAllGroupsInclusive";
                         */
                        .process(exchange -> {
                            Map<String, Object> aggregationMap = exchange.getProperties();
                            log.info("some aggregation properties={}", aggregationMap);
                        })
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
