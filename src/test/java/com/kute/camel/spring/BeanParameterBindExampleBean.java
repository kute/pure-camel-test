package com.kute.camel.spring;

import org.apache.camel.*;
import org.apache.camel.jsonpath.JsonPath;
import org.apache.camel.language.bean.Bean;
import org.apache.camel.language.xpath.XPath;
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

    // 若 body是一个xml document
    public void m2(@XPath("/order/@id") String id,
            @XPath("/order/status/text()") String text) {
    }

    // 调用bean的某个method
    public void m3(@Bean(ref = "myBeanName", method = "myMethod") String beanMethodResult) {

    }

    public void m4(@JsonPath(value = "$.order.result") String result) {

    }

    public void m5(String id, String status, String slash) {
        // 方法签名参数绑定
        // from("").bean("myBean", "myMethod(${body}, ${eader.status}, ${header.slash})")
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

