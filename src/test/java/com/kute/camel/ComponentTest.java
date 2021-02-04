package com.kute.camel;

import com.kute.camel.myself.components.ConsoleComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

/**
 * created by kute at 2020/10/26 8:48 下午
 */
@SpringBootTest
@ComponentScan(value = "com.kute.camel")
@Slf4j
public class ComponentTest extends AbstractTest {

    @Autowired
    private CamelContext camelContext;

    @Test
    public void test() {

        camelContext.addComponent("console", new ConsoleComponent());

    }

}
