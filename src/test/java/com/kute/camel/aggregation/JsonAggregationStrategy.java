package com.kute.camel.aggregation;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.support.builder.ExpressionBuilder;

import java.util.Optional;

/**
 * created by kute at 2020/9/20 10:40 上午
 */
@Slf4j
public class JsonAggregationStrategy implements AggregationStrategy {

    private static final String JSON_GROUP_EXCHANGE = "CamelJsonGroupedExchange";

    private Expression pickExpression = ExpressionBuilder.bodyExpression();

    public JsonAggregationStrategy pickBody(Expression expression) {
        this.pickExpression = expression;
        return this;
    }

    /**
     * 消息聚合
     * * 在 aggregate 方法内，要么 返回old 要么返回new，不要新创建 一个 exchange返回
     * * 任何异常情况下，请返回 old
     * <p>
     * 下一次的 循环，newExchange将成为oldExchange
     *
     * @param oldExchange
     * @param newExchange
     * @return
     */
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        log.info("===========oldExchange={}, newExchange={}", oldExchange == null ? "null" : oldExchange.getProperties(), newExchange.getProperties());
        JSONObject result;
        if (oldExchange == null) {
            result = getJSONObject(newExchange);
        } else {
            result = getJSONObject(oldExchange);
        }

        if(newExchange.getException() != null) {
            // some exception occur, then propagate exception
            if(null != oldExchange) {
                oldExchange.setException(newExchange.getException());
            }
        }

        String pick = pickExpression.evaluate(newExchange, String.class);
        if (pick != null) {
            result.put(pick, pick);
        }

        log.info("Json aggregation temp result={}", result.toJSONString());

        return oldExchange != null ? oldExchange : newExchange;
    }

    /**
     * 消息达到 completion条件后被调用，不允许抛出任何异常
     *
     * @param exchange
     */
    @Override
    public void onCompletion(Exchange exchange) {
        Optional.ofNullable(exchange).ifPresent(ex -> {
            JSONObject jsonObject = exchange.getProperty(JSON_GROUP_EXCHANGE, JSONObject.class);
            if (null != jsonObject) {
                ex.getIn().setBody(jsonObject.toJSONString());
            }
        });
    }

    /**
     * 当 消息超时时被调用
     *
     * @param exchange
     * @param index
     * @param total
     * @param timeout
     */
    @Override
    public void timeout(Exchange exchange, int index, int total, long timeout) {
        onCompletion(exchange);
    }

    private JSONObject getJSONObject(Exchange exchange) {
        JSONObject jsonObject = exchange.getProperty(JSON_GROUP_EXCHANGE, JSONObject.class);
        if (null == jsonObject) {
            jsonObject = new JSONObject();
            exchange.setProperty(JSON_GROUP_EXCHANGE, jsonObject);
        }
        return jsonObject;
    }

    @Override
    public boolean canPreComplete() {
        return false;
    }

    /**
     * 仅当 canPreComplete 方法返回 true时被调用
     *
     * @param oldExchange
     * @param newExchange
     * @return
     */
    @Override
    public boolean preComplete(Exchange oldExchange, Exchange newExchange) {
        return false;
    }
}
