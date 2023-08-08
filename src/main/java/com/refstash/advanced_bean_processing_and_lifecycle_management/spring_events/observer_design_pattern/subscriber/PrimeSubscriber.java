package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.subscriber;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.event.Episode;

public class PrimeSubscriber implements Subscriber {

    private String name;

    public PrimeSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void update(Episode episode) {
        System.out.println("Hi "+ name
                + ": New episode of " + episode.getShowName()
                + " is available (Episode " + episode.getEpisodeNo() + ")");
    }
}
