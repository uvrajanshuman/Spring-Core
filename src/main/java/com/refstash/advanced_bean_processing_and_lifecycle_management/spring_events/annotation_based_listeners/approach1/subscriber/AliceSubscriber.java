package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach1.subscriber;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach1.event.FamilyMan;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach1.event.Panchayat;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AliceSubscriber {
    private String name = "Alice";

    @EventListener
    public void handlePanchayatEvent(Panchayat event) {
        System.out.println("Hi "+ name
                + ": New episode of " + event.getShowName()
                + " is available (Episode " + event.getEpisodeNo() + ")");
    }

    @EventListener
    public void hadleFamilyManEvent(FamilyMan event) {
        System.out.println("Hi "+ name
                + ": New episode of " + event.getShowName()
                + " is available (Episode " + event.getEpisodeNo() + ")");
    }

}
