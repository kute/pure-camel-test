package com.kute.camel.log;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.kute.camel.AbstractTest;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.log.LogComponent;
import org.apache.camel.spi.ExchangeFormatter;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by kute at 2020/9/19 10:14 上午
 * <p>
 * org.apache.camel.component.log.LogEndpointConfigurer
 * 1、格式
 * log:className?option=value
 * 2、参数
 * level：ERROR, WARN, INFO, DEBUG, TRACE, OFF
 * logMask: true or false，敏感信息脱敏
 * groupInterval：毫秒，多久统计输出一次log
 * groupSize：一次输出的log大小
 * exchangeFormatter：
 * synchronous：boolean, 是否使用同步log的方式，default false
 * maxChars：每行日志的字符个数，默认10000
 * multiline：boolean，是否日志多行显示
 * showAll：快捷选项，打开所有的设置选项（multiline, maxChars 需要手动打开）
 * showBodyType：boolean 是否显示日志内容的类型
 * showBody：boolean 是否显示日志内容
 * showCaughtException：boolean，是否显示exchange捕获的异常，非堆栈信息
 * showException：boolean 是否显示异常信息
 * showExchangeId: boolean 是否显示exchange id
 * showStackTrace: boolean
 * showFiles: boolean
 * showFuture: boolean
 * showHeaders: boolean
 * showProperties: boolean, exchange properties
 * skipBodyLineSeparator: 忽略日志分隔符，日志显示成一行
 * style：输出样式，Default, Tab, Fixed
 */
@Configuration
public class LogTest extends AbstractTest {

    /**
     * 当 使用定制化的exchangeFormatter时，当有不同的log option时设置 此format为prototype，不共享实例
     *
     * @return
     */
    @Bean
    @Scope(value = "prototype")
    public ExchangeFormatter exchangeFormatter() {
        return new CustomizationExchangeFormatter();
    }

    @Bean
    public LogComponent logComponent() {
        LogComponent logComponent = new LogComponent();
        logComponent.setExchangeFormatter(exchangeFormatter());
        return logComponent;
    }

    @Test
    public void test() {

        Map<String, Object> optionMap = Maps.newHashMap(ImmutableMap.of(
                "level", "INFO",
                "multiline", true,
//                "showCaughtException", true,
//                "showException", true,
                "showExchangeId", true,
                "showHeaders", true
        ));

        String option = optionMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue().toString())
                .collect(Collectors.joining("&"));

        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("stream:file?fileName=data/inbox/message.json")
                        .to("log:com.kute.camel.log.LogTest?" + option);
            }
        });

    }

}
