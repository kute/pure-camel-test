package com.kute.camel.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * created by bailong001 on 2020/09/07 13:03
 */
@Service
@Slf4j
public class HelloBean {

    public void hello(String body) {
        log.info("==========" + body);
    }

}
