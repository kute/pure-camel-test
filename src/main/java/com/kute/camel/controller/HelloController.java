package com.kute.camel.controller;

import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by kute at 2020/10/2 9:45 上午
 */
@RestController
@RequestMapping("/api/v1/hello")
public class HelloController {

    @EndpointInject(value = "geocoder:address:current")
    private FluentProducerTemplate producerTemplate;

    @RequestMapping("/geo")
    public Object hello() {
        String where = producerTemplate.request(String.class);
        return "hello response with " + where;
    }

}
