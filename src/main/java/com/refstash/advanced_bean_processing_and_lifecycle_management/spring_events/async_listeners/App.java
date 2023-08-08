package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.async_listeners;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.async_listeners.publisher.AmazonPrime;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.async_listeners.event.FamilyMan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        AmazonPrime amazonPrime = applicationContext.getBean("amazonPrime", AmazonPrime.class);
        //publishing the event
        System.out.println("Publishing the Event: Family Man");
        amazonPrime.broadcastFamilyMan(new FamilyMan("S2: EP10"));
        System.out.println("Event published!!!");
    }
}
