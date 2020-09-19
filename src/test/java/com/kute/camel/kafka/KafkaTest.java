package com.kute.camel.kafka;

import com.kute.camel.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

/**
 * created by bailong001 on 2020/09/09 16:46
 */
@Slf4j
public class KafkaTest extends AbstractTest {


    @Test
    public void test() {

        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("stream:file?fileName=data/inbox/message.json")
                        .to("kafka:sinan-satisfaction-test?brokers=kafka01-test.lianjia.com:9092,kafka02-test.lianjia.com:9092,kafka03-test.lianjia.com:9092");
//                        .to("stream:out");
            }
        });

    }

}
