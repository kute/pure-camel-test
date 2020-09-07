package com.kute.camel.redis;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.Serializable;

/**
 * created by kute at 2020/9/7 10:09 下午
 */
@Data
@Accessors(chain = true)
public class RedisLettuceProperties implements Serializable {

    private String host;
    private Integer port;
    private String password;
    private Long timeout;
    private GenericObjectPoolConfig poolConfig;
}
