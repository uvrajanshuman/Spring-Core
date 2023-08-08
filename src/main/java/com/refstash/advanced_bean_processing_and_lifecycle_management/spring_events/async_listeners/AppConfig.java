package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.async_listeners;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ComponentScan
@EnableAsync
public class AppConfig {

    //custom implementation of ApplicationEventMulticaster
//    @Bean("applicationEventMulticaster")
//    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
//        SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = new SimpleApplicationEventMulticaster();
//        //taskExecutor set to SimpleAsyncTaskExecutor instead of the default SyncTaskExecutor
//        simpleApplicationEventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
//        return simpleApplicationEventMulticaster;
//    }
}
