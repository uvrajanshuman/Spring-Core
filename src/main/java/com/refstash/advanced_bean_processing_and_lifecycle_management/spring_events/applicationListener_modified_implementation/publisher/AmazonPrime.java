package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation.publisher;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation.event.Episode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class AmazonPrime implements Broadcaster {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void broadcast(Episode episode) {
        applicationEventPublisher.publishEvent(episode);
    }
}