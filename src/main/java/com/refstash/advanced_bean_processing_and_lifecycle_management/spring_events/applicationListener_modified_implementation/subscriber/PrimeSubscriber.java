package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation.subscriber;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation.event.Episode;
import org.springframework.context.ApplicationListener;

import java.util.Set;

public class PrimeSubscriber implements ApplicationListener<Episode>{
    private String name;
    private Set<String> subscribedShows;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setSubscribedShows(Set<String> subscribedShows){
        this.subscribedShows = subscribedShows;
    }

    @Override
    public void onApplicationEvent(Episode event) {
        if(subscribedShows.contains(event.getShowName()))
        System.out.println("Hi "+ name
                + ": New episode of " + event.getShowName()
                + " is available (Episode " + event.getEpisodeNo() + ")");
    }

}
