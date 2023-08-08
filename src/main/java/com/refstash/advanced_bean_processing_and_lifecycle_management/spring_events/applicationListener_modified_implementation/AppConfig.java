package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation.subscriber.PrimeSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
@ComponentScan
public class AppConfig {

    @Bean("alice")
    public PrimeSubscriber aliceSubscriber() {
        PrimeSubscriber primeSubscriber = new PrimeSubscriber();
        primeSubscriber.setName("Alice");
        Set<String> shows = new HashSet<>();
        shows.add("family-man");
        shows.add("panchayat");
        primeSubscriber.setSubscribedShows(shows);
        return primeSubscriber;
    }

    @Bean("bob")
    public PrimeSubscriber bobSubscriber() {
        PrimeSubscriber primeSubscriber = new PrimeSubscriber();
        primeSubscriber.setName("Bob");
        Set<String> shows = new HashSet<>();
        shows.add("family-man");
        primeSubscriber.setSubscribedShows(shows);
        return primeSubscriber;
    }
}
