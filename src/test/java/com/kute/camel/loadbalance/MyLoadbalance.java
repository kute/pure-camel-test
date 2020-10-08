package com.kute.camel.loadbalance;

import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.processor.loadbalancer.QueueLoadBalancer;

/**
 * created by bailong001 on 2020/09/30 14:14
 */
public class MyLoadbalance extends QueueLoadBalancer {
    @Override
    protected AsyncProcessor chooseProcessor(AsyncProcessor[] processors, Exchange exchange) {
        return null;
    }
}
