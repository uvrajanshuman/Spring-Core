package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.event.Episode;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.event.FamilyMan;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.event.Panchayat;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.publisher.AmazonPrime;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.publisher.Broadcaster;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.subscriber.PrimeSubscriber;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.subscriber.Subscriber;

public class App {
    public static void main(String[] args) {
        Broadcaster broadcaster = new AmazonPrime();

        Subscriber subscriber1 = new PrimeSubscriber("Alice");
        Subscriber subscriber2 = new PrimeSubscriber("Bob");

        broadcaster.addSubscriber("family-man",subscriber1);
        broadcaster.addSubscriber("panchayat",subscriber1);
        broadcaster.addSubscriber("panchayat",subscriber2);

        Episode panhayat = new Panchayat("S2:05");
        broadcaster.broadcast(panhayat);

        Episode familyMan = new FamilyMan("S2:10");
        broadcaster.broadcast(familyMan);

    }
}

/*
 * Output:
 * Hi Alice: New episode of panchayat is available (Episode S2:05)
 * Hi Bob: New episode of panchayat is available (Episode S2:05)
 * Hi Alice: New episode of family-man is available (Episode S2:10)
 */