package com.kute.camel.log;

import org.apache.camel.Exchange;
import org.apache.camel.spi.ExchangeFormatter;
import org.apache.camel.support.processor.DefaultExchangeFormatter;

/**
 * created by kute at 2020/9/19 10:48 上午
 * 定制化日志输出
 * 参考：org.apache.camel.support.processor.DefaultExchangeFormatter
 */
public class CustomizationExchangeFormatter implements ExchangeFormatter {
    @Override
    public String format(Exchange exchange) {
        return new DefaultExchangeFormatter().format(exchange);
    }
}
