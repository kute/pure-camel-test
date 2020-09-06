package com.kute.camel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * created by kute at 2020/9/6 4:27 下午
 */
@SpringBootTest
//@SpringBootTest(classes = PureCamelTestApplication.class)
//@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
class BeanTest {
//public class BeanTest extends AbstractTest {

//    @Autowired
//    private HelloBean helloBean;

    @Test
    public void test() {
//        superTest(new RouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                from("direct:hello")
//                        .to("bean:helloBean?method=hello");
////                        .to("bean:com.kute.camel.BeanTest.HelloBean?method=hello");
//            }
//        });
    }

    //    @Service
    public class HelloBean {
        public void hello(String body) {
            System.out.println("==========" + body);
        }
    }

}
