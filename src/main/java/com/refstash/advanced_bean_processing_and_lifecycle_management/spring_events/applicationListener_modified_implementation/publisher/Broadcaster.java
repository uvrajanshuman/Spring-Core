package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation.publisher;


import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation.event.Episode;

public interface Broadcaster {
    void broadcast(Episode episode);
}
