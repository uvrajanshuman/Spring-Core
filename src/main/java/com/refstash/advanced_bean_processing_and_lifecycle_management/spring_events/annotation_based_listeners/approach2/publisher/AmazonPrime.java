package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach2.publisher;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach2.event.Episode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class AmazonPrime {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void broadcast(Episode episode) {
        applicationEventPublisher.publishEvent(episode);
    }
}