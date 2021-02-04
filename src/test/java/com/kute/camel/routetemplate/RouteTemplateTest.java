package com.kute.camel.routetemplate;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.kute.camel.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.TemplatedRouteBuilder;
import org.apache.camel.main.Main;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.TimeUnit;

/**
 * created by kute at 2020/10/27 8:11 上午
 * <p>
 * route template + input parameters = route
 */
@SpringBootTest
@ComponentScan(value = "com.kute.camel")
@Slf4j
public class RouteTemplateTest extends AbstractTest {

    public static final String ROUTE_TEMPLATE_ID = "myTemplate";

    @Autowired
    private CamelContext camelContext;

    @Test
    public void test() throws Exception {
        // 像添加普通的route一样添加route template
//        camelContext.addRoutes(new RouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                // 创建一个template
//                routeTemplate(ROUTE_TEMPLATE_ID)
//                        // 定义 route template的模板变量（参数）
//                        .description("some")
//                        .templateParameter("name")
//                        .templateParameter("greeting")
//                        .templateParameter("myPeriod", "3s")
//                        // 开始 route的具体消费行为定义，模板变量使用 {{var}} 访问
//                        .from("timer:{{name}}?period={{myPeriod}}")
//                        .setBody(simple("{{greeting}} ${body}"))
//                        .log("${body}");
//            }
//        });

        Main main = new Main();
        main.configure().addRoutesBuilder();


        // 通过template创建route: 1
        String addedRouteId = camelContext.addRouteFromTemplate("my-route-template-1", ROUTE_TEMPLATE_ID, Maps.newHashMap(ImmutableMap.of(
                "name", "kute",
                "greeting", "hello"
        )));

        if(null == addedRouteId) {
            log.error("添加失败");
        }

        // 通过template创建route: 2
        addedRouteId = TemplatedRouteBuilder.builder(camelContext, ROUTE_TEMPLATE_ID)
                .routeId("my-route-template-2")
                .parameter("name", "kute2")
                .parameter("greeting", "hello2")
                .handler(routeTemplateDefinition -> {
                    log.info("my-route-template-2 consumer handle definition={}", routeTemplateDefinition);
                })
                .add();

        // 通过template创建route: 3
        // 在配置文件中配置

        TimeUnit.SECONDS.sleep(20);

        camelContext.stop();

    }

}
