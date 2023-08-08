package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach2;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach2.event.FamilyMan;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach2.event.Panchayat;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach2.publisher.AmazonPrime;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class App {
    public static void main(String[] args) {
        AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        AmazonPrime prime = applicationContext.getBean("amazonPrime",AmazonPrime.class);

        prime.broadcast(new Panchayat(applicationContext,"S1 : EP05"));
        prime.broadcast(new FamilyMan(applicationContext,"S2 : EP11"));

    }
}
/*
 * Output:
 * Hi Alice: New episode of family-man is available (Episode S2 : EP11)
 * Hi Bob: New episode of family-man is available (Episode S2 : EP11)
 */