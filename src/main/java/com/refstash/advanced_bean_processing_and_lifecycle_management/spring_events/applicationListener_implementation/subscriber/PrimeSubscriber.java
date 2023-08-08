package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_implementation.subscriber;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_implementation.event.FamilyMan;
import org.springframework.context.ApplicationListener;

public class PrimeSubscriber implements ApplicationListener<FamilyMan> {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void onApplicationEvent(FamilyMan event) {
            System.out.println("Hi "+ name
                    + ": New episode of " + event.getShowName()
                    + " is available (Episode " + event.getEpisodeNo() + ")");
    }

}
