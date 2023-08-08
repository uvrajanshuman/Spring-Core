package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.event;

public class FamilyMan implements Episode {

    public static String showName = "family-man";

    private final String episodeNo;

    public FamilyMan(String episodeNo) {
        this.episodeNo = episodeNo;
    }

    @Override
    public String getShowName() {
        return showName;
    }

    public String getEpisodeNo() {
        return episodeNo;
    }

}
