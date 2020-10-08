package com.kute.camel.error;

import com.kute.camel.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.Resilience4jConfigurationDefinition;
import org.apache.camel.processor.errorhandler.ExceptionPolicyStrategy;
import org.apache.http.auth.AuthenticationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * created by kute at 2020/10/2 10:38 上午
 */
@SpringBootTest
@ComponentScan(value = "com.kute.camel")
@Slf4j
public class FailureTest extends AbstractTest {

    @Autowired
    private CamelContext camelContext;

    @Test
    public void test() throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                // context-scope error handler

                // 配置此routebuilder所有route的errorhandler
                errorHandler(
                        defaultErrorHandler()
//                                .retryWhile(
//                                        simple("${header.CamelRedeliveryCounter} < 3")
//                                )
                                .maximumRedeliveries(5)
                                .maximumRedeliveryDelay(2000)
                                // 重试日志级别
                                .retryAttemptedLogLevel(LoggingLevel.WARN)
                                .logStackTrace(true)
                                .logRetryStackTrace(true)
                                .logExhaustedMessageHistory(true)
                );

                // 对于特定的异常的处理
                onException(IOException.class, ConnectException.class)
                        // 标明是否 处理该exchange并且继续route，即忽略异常
                        .continued(false)
                        .maximumRedeliveries(10);

                onException(ClassCastException.class)
                        // 何时 应用异常策略
                        .onWhen(exchange -> {
                            return true;
                        })
                        // 标明 要处理该异常，默认不处理
                        .handled(true)
                        .process(exchange -> {

                        })
                        .maximumRedeliveries(10);


                // 死信队列
                errorHandler(deadLetterChannel("log:dealLetterLog?level=ERROR")
                                .maximumRedeliveries(5)
                                // 是否使用 最开始的message，包括body 和header（即 from的message）放到死信队列，而不是当前流转的message
                                .useOriginalMessage()
                                // 只 body 使用最开始的
//                        .useOriginalBody()
                                .onExceptionOccurred(exchange -> {
                                    // 异常发生时调用
                                })
                                .onPrepareFailure(exchange -> {
                                    // 发生异常后，在被error handler处理前调用
                                })
                                .onRedelivery(exchange -> {
                                    // 消息再次重新投递时调用，重试
                                })
                );

//                errorHandler(noErrorHandler());

                from("direct:start")
//                        .onException(IOException.class)
//                        .to("")
//                        .end()
                        .process(exchange -> {
                            Integer counter = exchange.getMessage().getHeader(Exchange.REDELIVERY_COUNTER, Integer.class);
                            Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                            log.info("route process message={} in thread={} with headers={}, properties={}, caught={}",
                                    exchange.getMessage().getBody(), Thread.currentThread().getId(),
                                    exchange.getMessage().getHeaders(), exchange.getProperties(), exception);
                            // 模拟异常
                            if (counter == null || counter < 3) {
                                throw new IOException("occur io exception");
                            }
                        })
                        .to("stream:out");
            }
        });

        ProducerTemplate template = camelContext.createProducerTemplate();

        template.requestBody("direct:start", "hello world");

        TimeUnit.SECONDS.sleep(30);

        camelContext.stop();
    }

    @Test
    public void testTryCatchFinally() throws Exception {

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("")
                        .bean("")
                        .doTry()
                        .bean("")
                        .process(exchange -> {

                        })
                        .endDoTry()
                        .doCatch(IOException.class)
                        .process(exchange -> {

                        })
                        .to("");
            }
        });

    }

    @Test
    public void testFatal() throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                onException(AuthenticationException.class)
                        .handled(true)
                        .process(exchange -> {
                            log.info("begin process AuthenticationException");
                            throw new NullPointerException("Process with NullPointException occur");
                        });

                onException(Exception.class)
                        .handled(true)
                        .process(exchange -> {
                            log.info("begin process general exception");
                            throw new AuthenticationException("Process with AuthenticationException occur");
                        });

                from("direct:start")
                        .log("receive boey=${body}")
                        .process(exchange -> {
                            log.info("begin throw exception");
                            throw new AuthenticationException("Process with Not auth exception");
                        });
            }
        });

        ProducerTemplate template = camelContext.createProducerTemplate();

        template.requestBody("direct:start", "hello world");

        TimeUnit.SECONDS.sleep(30);

        camelContext.stop();
    }

}
