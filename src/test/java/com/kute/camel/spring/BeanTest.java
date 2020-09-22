package com.kute.camel.spring;

import com.google.common.base.Preconditions;
import com.kute.camel.AbstractTest;
import org.apache.camel.BeanScope;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.Synchronization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

/**
 * created by kute at 2020/9/6 4:27 下午
 * <p>
 * org.apache.camel.component.bean.BeanProcessor
 */
@SpringBootTest
@ComponentScan(value = "com.kute.camel")
public class BeanTest extends AbstractTest {

    /**
     * spring环境下的context: SpringBootCamelContext
     * <p>
     * org.apache.camel.spring.boot.CamelAutoConfiguration
     */
    @Autowired
    private CamelContext context;

    @Autowired
    private HelloBean helloBean;

    @Test
    public void test() {
        superTest(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("stream:file?fileName=data/inbox/message.json")
                        // 当bean只有一个public的方法时无需声明
                        .bean(helloBean, "hello", BeanScope.Singleton)
//                        .bean("com.kute.camel.spring.HelloBean", "hello")
//                        .bean(HelloBean.class, "hello")
                        .to("stream:out");
                HelloBean helloBean2 = context.getRegistry().lookupByNameAndType("helloBean", HelloBean.class);

                Preconditions.checkArgument(helloBean.hashCode() == helloBean2.hashCode());

            }
        }, context);


    }

    @Test
    public void templateTest() throws Exception {

        String endpoint = "direct:nothing";

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(endpoint)
                        .bean(helloBean, "hello");
            }
        });

        ProducerTemplate template = context.createProducerTemplate();

        // requestBody: inout message
//        Object reply = template.requestBody(endpoint, "new message");
//        String reply_string = template.requestBody(endpoint, "new message", String.class);
//        String reply_string_2 = template.requestBodyAndHeader(endpoint, "new message", "my-header", "header-value", String.class);
//        System.out.println(reply);
//        System.out.println(reply_string);
//        System.out.println(reply_string_2);

//        template.asyncRequestBody(endpoint, "new message")
//                .whenComplete((o, throwable) -> {
//                    System.out.println("complete for " + o);
//                });
//
//        template.asyncCallbackRequestBody(endpoint, "new message", new Synchronization() {
//            @Override
//            public void onComplete(Exchange exchange) {
//                System.out.println("asyncCallbackRequestBody onComplete for: " + exchange.getMessage().getBody());
//            }
//
//            @Override
//            public void onFailure(Exchange exchange) {
//                System.out.println("asyncCallbackRequestBody onFailure for: " + exchange.getMessage().getBody());
//            }
//        });

        // send: inonly
        template.asyncCallbackSendBody(endpoint, "new message", new Synchronization() {
            @Override
            public void onComplete(Exchange exchange) {
                System.out.println("asyncCallbackSendBody onComplete for: " + exchange.getMessage().getBody());
                System.out.println("asyncCallbackSendBody onComplete for: " + exchange.getIn().getBody());
            }

            @Override
            public void onFailure(Exchange exchange) {
                System.out.println("asyncCallbackSendBody onComplete for: " + exchange.getMessage().getBody());
            }
        });


    }

}
