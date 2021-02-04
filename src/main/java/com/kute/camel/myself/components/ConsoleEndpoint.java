package com.kute.camel.myself.components;

import org.apache.camel.Category;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.support.DefaultEndpoint;

/**
 * created by kute at 2020/10/26 8:56 下午
 */
@UriEndpoint(
        firstVersion = "1.0.0",
        scheme = "bean",
        title = "Bean",
        syntax = "bean:beanName",
        producerOnly = true,
        category = {Category.CORE, Category.JAVA}
)
public class ConsoleEndpoint extends DefaultEndpoint {
    @Override
    public String getEndpointBaseUri() {
        return null;
    }

    @Override
    public Producer createProducer() throws Exception {
        return null;
    }

    @Override
    public boolean isSingletonProducer() {
        return false;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return null;
    }
}
