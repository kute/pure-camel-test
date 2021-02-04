package com.kute.camel.routetemplate;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * created by kute at 2020/10/27 8:13 上午
 *
 * route template + input parameters = route
 */
@Component
public class MyRouteTemplate extends RouteBuilder {

    public static final String ROUTE_TEMPLATE_ID = "myTemplate";

    @Override
    public void configure() throws Exception {
        // 创建一个template
        routeTemplate(ROUTE_TEMPLATE_ID)
                // 定义 route template的模板变量（参数）
                .templateParameter("name")
                .templateParameter("greeting")
                .templateParameter("myPeriod", "3s")
                // 开始 route的具体消费行为定义，模板变量使用 {{var}} 访问
                .from("timer:{{name}}?period={{myPeriod}}")
                .setBody(simple("{{greeting}} ${body}"))
                .log("${body}");
    }
}
