package com.refstash.advanced_bean_processing_and_lifecycle_management.asynchronous_processing;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@ComponentScan
public class AppConfig {
    public static void main(String[] args) {
        System.out.println("main() : execution starts");
        AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        AsyncService asyncService = applicationContext.getBean(AsyncService.class);
        asyncService.asyncMethod();
        System.out.println("main() : execution ends");
        applicationContext.close();
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        return executor;
    }
}

/*
 * Output:
 * main() : execution starts
 * main() : execution ends
 * asyncMethod() : execution starts
 * asyncMethod: 1
 * asyncMethod: 2
 * asyncMethod: 3
 * asyncMethod: 4
 * asyncMethod: 5
 * asyncMethod: 6
 * asyncMethod: 7
 * asyncMethod: 8
 * asyncMethod: 9
 * asyncMethod: 10
 * asyncMethod() : execution ends
 */
