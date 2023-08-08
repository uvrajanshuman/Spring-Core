package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach2.event;

import org.springframework.context.ApplicationEvent;

public abstract class Episode extends ApplicationEvent{
    
    public Episode(Object source) {
        super(source);
    }
    
    public abstract String getShowName();
    public abstract String getEpisodeNo();
}
