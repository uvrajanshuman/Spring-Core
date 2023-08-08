package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.async_listeners.publisher;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.async_listeners.event.FamilyMan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class AmazonPrime {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void broadcastFamilyMan(FamilyMan episode) {
        applicationEventPublisher.publishEvent(episode);
    }

}