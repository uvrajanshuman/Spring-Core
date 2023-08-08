package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation.event.FamilyMan;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation.event.Panchayat;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation.publisher.AmazonPrime;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class App {
    public static void main(String[] args) {
        AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        AmazonPrime prime = applicationContext.getBean("amazonPrime",AmazonPrime.class);

        prime.broadcast(new Panchayat(applicationContext,"s1:01"));
        prime.broadcast(new FamilyMan(applicationContext,"s2:11"));

    }
}

/*
 * Output:
 * Hi Alice: New episode of panchayat is available (Episode s1:01)
 * Hi Alice: New episode of family-man is available (Episode s2:11)
 * Hi Bob: New episode of family-man is available (Episode s2:11)
 */