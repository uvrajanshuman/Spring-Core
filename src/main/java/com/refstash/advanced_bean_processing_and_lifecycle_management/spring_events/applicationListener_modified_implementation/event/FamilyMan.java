package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation.event;

public class FamilyMan extends Episode {

    public static final String showName = "family-man";

    private final String episodeNo;

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
