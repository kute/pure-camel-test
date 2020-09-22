package com.kute.camel.aggregation;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.spi.AggregationRepository;

import java.util.Set;

/**
 * created by bailong001 on 2020/09/22 16:41
 */
public class RedisAggregationRepository implements AggregationRepository {
    @Override
    public Exchange add(CamelContext camelContext, String key, Exchange exchange) {
        return null;
    }

    @Override
    public Exchange get(CamelContext camelContext, String key) {
        return null;
    }

    @Override
    public void remove(CamelContext camelContext, String key, Exchange exchange) {

    }

    @Override
    public void confirm(CamelContext camelContext, String exchangeId) {

    }

    @Override
    public Set<String> getKeys() {
        return null;
    }
}
