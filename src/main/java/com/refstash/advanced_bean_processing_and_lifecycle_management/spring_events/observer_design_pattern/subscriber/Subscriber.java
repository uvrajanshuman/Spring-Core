package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.subscriber;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.event.Episode;

public interface Subscriber {
    void update(Episode episode);
}
