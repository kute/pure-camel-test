package com.kute.camel;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * created by kute at 2020/10/1 6:35 下午
 */
@Configuration
public class BeanConfiguration {

    @Bean(name = "myExecutorService")
    public ScheduledExecutorService myExecutorService() {
        return new ScheduledThreadPoolExecutor(10,
                new ThreadFactoryBuilder().setNameFormat("scheduler-thread-%d").build(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }
}
