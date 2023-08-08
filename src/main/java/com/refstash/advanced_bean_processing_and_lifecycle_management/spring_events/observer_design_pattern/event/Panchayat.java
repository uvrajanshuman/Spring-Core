package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.observer_design_pattern.event;

public class Panchayat implements Episode {

    public static String showName = "panchayat";

    private String episodeNo;

    public Panchayat(String episodeNo) {
        this.episodeNo = episodeNo;
    }

    @Override
    public String getShowName() {
        return showName;
    }

    public String getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(String episodeNo) {
        this.episodeNo = episodeNo;
    }
}
