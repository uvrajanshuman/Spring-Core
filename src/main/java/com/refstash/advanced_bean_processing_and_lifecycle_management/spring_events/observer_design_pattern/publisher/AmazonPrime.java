package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.publisher;

import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.event.Episode;
import com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.subscriber.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmazonPrime implements Broadcaster{
    private Map<String, List<Subscriber>> subscribersByShow = new HashMap<>();

    public AmazonPrime() {
        //initializing subscriberByShow for available shows
        subscribersByShow.put("family-man", new ArrayList<>());
        subscribersByShow.put("panchayat", new ArrayList<>());
    }

    @Override
    public void addSubscriber(String show, Subscriber subscriber) {
        List<Subscriber> subscribers = subscribersByShow.get(show);
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscriber(String show, Subscriber subscriber) {
        List<Subscriber> subscribers = subscribersByShow.get(show);
        subscribers.remove(subscriber);
    }

    @Override
    public void broadcast(Episode episode) {
        List<Subscriber> subscribers = subscribersByShow.get(episode.getShowName());
        for (Subscriber subscriber: subscribers) {
            subscriber.update(episode);
        }
    }
}