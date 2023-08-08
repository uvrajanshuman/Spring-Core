package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_implementation;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_implementation.subscriber.PrimeSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class AppConfig {

    @Bean("alice")
    public PrimeSubscriber aliceSubscriber() {
        PrimeSubscriber primeSubscriber = new PrimeSubscriber();
        primeSubscriber.setName("Alice");
        return primeSubscriber;
    }

    @Bean("bob")
    public PrimeSubscriber bobSubscriber() {
        PrimeSubscriber primeSubscriber = new PrimeSubscriber();
        primeSubscriber.setName("Bob");
        return primeSubscriber;
    }
}
