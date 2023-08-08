package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach1.publisher;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach1.event.FamilyMan;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach1.event.Panchayat;
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

    public void broadcastPanchayat(Panchayat episode) {
        applicationEventPublisher.publishEvent(episode);
    }
}