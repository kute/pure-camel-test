package com.kute.camel.aggregation;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * created by bailong001 on 2020/09/22 15:27
 */
@Component
@Slf4j
public class JsonAggregationStrategyBean {

    private static final String JSON_GROUP_EXCHANGE = "CamelJsonGroupedExchange";

    /**
     * 参数自动绑定，允许old为空，即 将第一次到来的消息作为 newBody
     *
     * @param oldBody
     * @param oldHeaders
     * @param oldProperties
     * @param newBody
     * @param newHeaders
     * @param newProperties
     * @return
     */
    public String jsonAppendAllowNull(String oldBody, Map<String, String> oldHeaders, Map<String, String> oldProperties,
                                      String newBody, Map<String, String> newHeaders, Map<String, String> newProperties) {
        log.info("JsonAggregationStrategyPojo begin aggregate for oldBody={}, oldHeaders={}, oldProperties={}, " +
                        "newBody={}, newHeaders={}, newProperties={}",
                oldBody, oldHeaders, oldProperties, newBody, newHeaders, newProperties);
        JSONObject finalJson;
        if (Strings.isNullOrEmpty(oldBody)) {
            finalJson = new JSONObject();
        } else {
            finalJson = JSONObject.parseObject(oldBody);
        }
        finalJson.put(newBody, newBody);
        return finalJson.toJSONString();
    }

    /**
     * 不允许 为空，即 将 第一次到来的消息作为 oldBody
     *
     * @param oldBody
     * @param oldHeaders
     * @param oldProperties
     * @param newBody
     * @param newHeaders
     * @param newProperties
     * @return
     */
    public String jsonAppendNotAllNull(String oldBody, Map<String, String> oldHeaders, Map<String, Object> oldProperties,
                                       String newBody, Map<String, String> newHeaders, Map<String, Object> newProperties) {
        log.info("JsonAggregationStrategyPojo begin aggregate for oldBody={}, oldHeaders={}, oldProperties={}, " +
                        "newBody={}, newHeaders={}, newProperties={}",
                oldBody, oldHeaders, oldProperties, newBody, newHeaders, newProperties);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(oldBody));

        JSONObject jsonObject = (JSONObject) oldProperties.get(JSON_GROUP_EXCHANGE);
        if (null == jsonObject) {
            jsonObject = new JSONObject();
            oldProperties.put(JSON_GROUP_EXCHANGE, jsonObject);
            jsonObject.put(oldBody, oldBody);
        }

        jsonObject.put(newBody, newBody);

        return jsonObject.toJSONString();
    }

}
