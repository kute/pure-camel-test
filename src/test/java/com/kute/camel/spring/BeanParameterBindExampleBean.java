package com.kute.camel.spring;

import org.apache.camel.*;
import org.apache.camel.spi.Registry;

import java.util.Map;

/**
 * created by bailong001 on 2020/09/15 11:54
 * <p>
 * bean method 方法参数绑定示例
 */
public class BeanParameterBindExampleBean {

    // @Handler 注解表明当有多个同名的方法时优先选择带有此注解的方法
    @Handler
    public void m1(@Body String body,
                   @Headers Map<String, String> headers,
                   @ExchangeProperty("property-name") String exchangeProperty,
                   @ExchangeProperties Map<String, String> properties,
                   @ExchangeException Exception e,
                   @Header("heander-name") String headerValue) {

    }

    // 只有一个参数时无需声明  @Body
    public void m2(@Body String a) {

    }

    /**
     * 如下参数类型会自动绑定
     *
     * @param exchange
     * @param message
     * @param context
     * @param typeConverter
     * @param registry
     * @param e             如果有任何异常会绑定到此参数
     */
    public void m3_auto_bind(Exchange exchange, Message message, CamelContext context,
                             TypeConverter typeConverter, Registry registry, Exception e) {

    }

}
