package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach1;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach1.event.FamilyMan;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach1.event.Panchayat;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach1.publisher.AmazonPrime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        AmazonPrime amazonPrime = applicationContext.getBean("amazonPrime", AmazonPrime.class);

        amazonPrime.broadcastFamilyMan(new FamilyMan("S1 : EP10"));
        amazonPrime.broadcastPanchayat(new Panchayat("S2 : EP05"));
    }
}

/*
 * Output:
 * Hi Alice: New episode of family-man is available (Episode S1 : EP10)
 * Hi Alice: New episode of panchayat is available (Episode S2 : EP05)
 * Hi Bob: New episode of panchayat is available (Episode S2 : EP05)
 */