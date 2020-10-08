package com.kute.camel.myself.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * created by kute at 2020/10/4 9:47 上午
 */
public abstract class AbstractRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        errorHandler(
                defaultErrorHandler()
                        .maximumRedeliveries(5)
                        .maximumRedeliveryDelay(2000)
                        // 重试日志级别
                        .retryAttemptedLogLevel(LoggingLevel.WARN)
                        .logStackTrace(true)
                        .logRetryStackTrace(true)
                        .logExhaustedMessageHistory(true));
    }
}
