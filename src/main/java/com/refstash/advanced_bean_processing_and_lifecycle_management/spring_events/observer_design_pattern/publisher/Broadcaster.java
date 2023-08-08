package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.publisher;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.event.Episode;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.subscriber.Subscriber;

public interface Broadcaster {

    void addSubscriber(String show, Subscriber subsciber);

    void removeSubscriber(String show, Subscriber subsciber);

    void broadcast(Episode episode);
}
