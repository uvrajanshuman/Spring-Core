package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach2.subscriber;


import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach2.event.Episode;
import org.springframework.context.event.EventListener;

import java.util.HashSet;
import java.util.Set;

public class PrimeSubscriber {
    private String name;
    public static Set<String> subscribedShows = new HashSet<>();

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setSubscribedShows(Set<String> subscribedShows){
        this.subscribedShows = subscribedShows;
    }

    @EventListener
    public void handleEvent(Episode event){
        if(subscribedShows.contains(event.getShowName())) {
            System.out.println("Hi " + name
                    + ": New episode of " + event.getShowName()
                    + " is available (Episode " + event.getEpisodeNo() + ")");
        }
    }

}
