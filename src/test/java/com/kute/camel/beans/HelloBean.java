package com.kute.camel.beans;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Handler;
import org.springframework.stereotype.Service;

/**
 * created by bailong001 on 2020/09/07 13:03
 */
@Service
@Slf4j
public class HelloBean {

    // 该注解 只是为了用于 camel挑选bean对应的method时优先选择
    @Handler
    public String hello(String body) {
        log.info("==========" + body);
        return "hello-with-" + body;
    }

}
