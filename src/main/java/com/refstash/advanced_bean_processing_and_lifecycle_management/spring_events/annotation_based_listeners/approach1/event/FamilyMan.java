package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.annotation_based_listeners.approach1.event;

public class FamilyMan {

    public static final String showName = "family-man";

    private final String episodeNo;

    public FamilyMan(String episodeNo) {
        this.episodeNo = episodeNo;
    }

    public String getShowName() {
        return showName;
    }

    public String getEpisodeNo() {
        return episodeNo;
    }

}
