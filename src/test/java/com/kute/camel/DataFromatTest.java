package com.kute.camel;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

/**
 * created by kute at 2020/9/6 9:16 上午
 */
public class DataFromatTest extends AbstractTest {

    @Test
    public void testWithProcessor() {
        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {

                            }
                        })
                        .to("stream:out");
            }
        });
    }

    @Test
    public void testWithBean() {

        class MyBean {
            public String map(String body) {
                return null;
            }
        }

        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .bean(new MyBean())
                        .bean(new MyBean(), "map")
                        .to("stream:out");
            }
        });
    }

    @Test
    public void testWithTransform() {

        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .transform(body().regexReplaceAll("", ""))
                        .transform(new Expression() {
                            @Override
                            public <T> T evaluate(Exchange exchange, Class<T> type) {
                                return (T) exchange.getIn().getBody(String.class).replaceAll("", "");
                            }
                        })
                        .to("stream:out");
            }
        });
    }

    @Test
    public void testWithPollEnricher() {

        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                /**
                 * -1: 一直阻塞等待，直到有消息到来
                 * 0: 如果有消息就拉取返回，如果没有则返回null，不会等待消息到来，默认模式
                 * > 0: 阻塞等待n毫秒
                 */
                Integer timeout = 0;
                from("A")
                        /**
                         * 从 C 数据源读取数据，然后和 已存在的 数据进行合并
                         */
                        .pollEnrich("C", timeout, new AggregationStrategy() {
                            // oldExchange=A, newExchange=C
                            @Override
                            public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                                if (null == newExchange) {
                                    return oldExchange;
                                }
                                String oldMessage = oldExchange.getIn().getBody(String.class);
                                String newMessage = newExchange.getIn().getBody(String.class);
                                oldExchange.getIn().setBody(oldMessage + newMessage, String.class);
                                return oldExchange;
                            }
                        })
                        .to("B");
            }
        });
    }

    /**
     * marshal: java objects to  xml, json, csv ets.
     * unmarshal: xml, json, csv to java objects
     */
    @Test
    public void testWithDataFormats() {

        CsvDataFormat csvDataFormat = new CsvDataFormat();
        csvDataFormat.setDelimiter(",");

        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .unmarshal()
                        .csv().split(body())
                        .to("stream:out");

//                from("direct:start")
//                        .marshal().json(JsonLibrary.Fastjson)
//                        .to("stream:out");
//
//                from("direct:start")
//                        .marshal(csvDataFormat)
//                        .to("stream:out");

            }
        });
    }

    /**
     * 使用注册到 TypeConverterRegistry 中的type converter
     */
    @Test
    public void testWithTypeConverter() {
        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:data/inbox?noop=true")
                        .convertBodyTo(String.class, "utf-8")
                        .to("log:logTest?level=INFO&multiline=true");
            }
        });
    }


}
