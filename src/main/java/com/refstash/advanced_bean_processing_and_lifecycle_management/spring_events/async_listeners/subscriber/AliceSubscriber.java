package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.async_listeners.subscriber;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.async_listeners.event.FamilyMan;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AliceSubscriber {
    private String name = "Alice";

    @Async
    @EventListener
    public void hadleFamilyManEvent(FamilyMan event) throws InterruptedException {
        System.out.println("Listener method of Alice");
        Thread.sleep(4000);
        System.out.println("Hi "+ name
                + ": New episode of " + event.getShowName()
                + " is available (Episode " + event.getEpisodeNo() + ")");
    }

}
