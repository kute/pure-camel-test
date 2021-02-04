package com.kute.camel.kafka;

import com.google.common.base.Strings;
import com.kute.camel.AbstractTest;
import com.kute.camel.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;

/**
 * created by bailong001 on 2020/09/09 16:46
 * <p>
 * <p>
 * https://camel.apache.org/components/latest/kafka-component.html
 * kafka:topic-1,topic-2,topic-3...?option=value
 * <p>
 * <p>
 * option配置列表：org.apache.camel.component.kafka.KafkaConfiguration
 * <p>
 * <p>
 * additionalProperties: 配置额外的一些kafka 属性
 * brokers： ip1:port1,ip2:port2
 * clientId: 客户端标识，用来追查日志的
 * headerFilterStrategy：
 * reconnectBackoffMaxMs:reconnect_backoff_ms每次再次连接失败会以指数增长,增长到的最大限度就是这个参数,为了避免连接风暴,连接重试的时间间隔会在一个范围内随机调整,上浮或下调20%,也就是说每次重连的时间间隔不一定就是这个值本身,而是上下浮动20%. 另外这里解释下连接风暴,当我们的kafka集群出现问题后,所有的producer和consumer都会尝试重连,重连的间隔就会达到这个参数所设置的最大值,比如大家都是每秒尝试重连,这时候如果集群回复了,那么在同一秒可能就会有大量的连接打到kafka集群上,这就造成了连接风暴,但是如果随机上下浮动就可能把重连时间给错开,不会造成同事的大量连接 Default: 1000
 * shutdownTimeout: 等待producer 或者 consumer 优雅关闭的默认超时时间
 * allowManualCommit：是否允许手动提交，若允许，将会在exchange header中存储一个 KafkaManualCommit，方便用户调用API手动提交
 * autoCommitEnable：是否允许自动提交
 * autoCommitIntervalMs：自动提交时间间隔
 * autoCommitOnStop: consumer停止时是否自动提交，可选值：async, sync, none，默认 sync
 * autoOffsetReset: 当初始化时或者offset超出范围时的重置策略：earliest, latest, none
 * breakOnFirstError: true: 当遇到错误时重新消费发生错误的消息；false: 遇到错误继续消费下一个消息
 * bridgeErrorHandler: boolean, 是否在消费异常时由errorhandler(即 BridgeExceptionHandlerToErrorHandler)处理，默认使用 org.apache.camel.spi.ExceptionHandler
 * consumersCount: 消费者个数
 * consumerStreams：并行处理的消费者个数
 * fetchMaxBytes： 批量获取的数据大小
 * fetchMinBytes:
 * groupId:
 * heartbeatIntervalMs: 消费者心跳检测间隔，不能大于session.timeout.ms，一般配置不大于 1/3
 * kafkaHeaderDeserializer：
 * keySerializerClass: default KafkaConstants.KAFKA_DEFAULT_SERIALIZER
 * keyDeserializer： default KafkaConstants.KAFKA_DEFAULT_DESERIALIZER
 * valueDeserializer： default KafkaConstants.KAFKA_DEFAULT_DESERIALIZER
 * serializerClass: message 序列化
 * maxPartitionFetchBytes:
 * topicIsPattern: 是否topic是一个正则表达式，用于动态订阅
 * bufferMemorySize：producer在发送给broker前可缓存的字节大小，若消息大小在发送前超过了此设置，则 根据block.on.buffer.full 设置的策略阻塞或者抛异常
 * compressionCodec：消息压缩，gzip, snappy, none
 * key：发消息时指定的key
 * metricReporters:
 * partitionKey: partition
 * producerBatchSize: 批量发送大小
 * requestRequiredAcks： ack值，0：leader不确认任何，1：只确认自己，不确认follows，all：leader以及follows都确认写入
 * workerPool：ExecutorService
 * workerPoolCoreSize：
 * workerPoolMaxSize：
 * interceptorClasses：用于监控的拦截器
 */
@SpringBootTest
@ComponentScan(value = "com.kute.camel")
@Slf4j
public class KafkaTest extends AbstractTest {

    @Autowired
    private CamelContext camelContext;

    private static final String BROKER_LIST = "";

    private static final String TOPIC = "sinan-satisfaction-test";

    public static String defaultOptions(String brokers, String partitionKey, String messageKey) {
        Map<String, String> optionMap = new HashMap<>();
        optionMap.put("brokers", brokers);
        optionMap.put("groupId", "my-consumer");
        optionMap.put("autoCommitEnable", "true");
        optionMap.put("autoCommitIntervalMs", "100");
        optionMap.put("autoCommitOnStop", "sync");
        optionMap.put("autoOffsetReset", "latest");
        optionMap.put("breakOnFirstError", "false");
        optionMap.put("consumersCount", "3");
        optionMap.put("fetchMaxBytes", "16384"); // 16k
        optionMap.put("retries", "0");
        if (!Strings.isNullOrEmpty(partitionKey)) {
            optionMap.put("partitionKey", partitionKey);
        }
        if (!Strings.isNullOrEmpty(messageKey)) {
            optionMap.put("key", messageKey);
        }
        optionMap.put("keySerializerClass", "org.apache.kafka.common.serialization.StringSerializer");
        optionMap.put("keyDeserializer", "org.apache.kafka.common.serialization.StringSerializer");
        return CommonUtil.toUriParam(optionMap);
    }

    @Test
    public void test() {

        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("stream:file?fileName=data/inbox/mutil-line-message.line")
                        .split(body().tokenize("\n"))
                        .to("kafka:sinan-satisfaction-test?" + defaultOptions(BROKER_LIST, "0", null));
            }
        });

    }

    @Test
    public void test2() {

        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("kafka:sinan-satisfaction-test?" + defaultOptions(BROKER_LIST, "0", null))
                        .log("Message received from Kafka : ${body}")
                        .log("on the topic ${headers[kafka.TOPIC]}")
                        .process(exchange -> {
                            log.info("consume kafka message headers={}", exchange.getMessage().getHeaders());
                        })
                        .to("log:logTest?showAll=true&multiline=true");
            }
        });

    }

    @Test
    public void test3() {
        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .setBody(constant("Message from Camel"))          // Message to send
                        .setHeader(KafkaConstants.KEY, constant("Camel"))
                        .to("kafka:sinan-satisfaction-test?" + defaultOptions(BROKER_LIST, "0", null));
            }
        });
    }

}
