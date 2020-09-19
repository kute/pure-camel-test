package com.kute.camel.redis;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.assertj.core.util.Maps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

/**
 * created by kute at 2020/9/7 9:58 下午
 */
@Configuration
@Slf4j
public class RedisCommonConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "common.redis", value = "host")
    @ConfigurationProperties(prefix = "common.redis")
    public RedisLettuceProperties redisLettuceProperties() {
        return new RedisLettuceProperties();
    }

    @Bean
    @ConditionalOnBean(RedisLettuceProperties.class)
    public RedisSerializer<String> redisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    @ConditionalOnBean(RedisLettuceProperties.class)
    public RedisConnectionFactory redisConnectionFactory() {
        log.info("Init lettuceConnectionFactory ...");
        RedisLettuceProperties properties = this.redisLettuceProperties();
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(properties.getHost());
        configuration.setPort(properties.getPort());
        if (!Strings.isNullOrEmpty(properties.getPassword())) {
            configuration.setPassword(RedisPassword.of(properties.getPassword()));
        }
        LettuceClientConfiguration clientPollConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(properties.getTimeout()))
                .poolConfig(properties.getPoolConfig()).build();
        return new LettuceConnectionFactory(configuration, clientPollConfig);
    }


}
