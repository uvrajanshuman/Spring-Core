package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_implementation.event;

import org.springframework.context.ApplicationEvent;

public class FamilyMan extends ApplicationEvent {

    public static String showName = "family-man";

    private String episodeNo;

    public FamilyMan(Object source) {
        super(source);
    }

    public FamilyMan(Object source, String episodeNo) {
        super(source);
        this.episodeNo = episodeNo;
    }

    public String getShowName() {
        return showName;
    }

    public String getEpisodeNo() {
        return episodeNo;
    }

}