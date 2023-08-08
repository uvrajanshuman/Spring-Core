package com.refstash.advanced_bean_processing_and_lifecycle_management.spring_events.applicationListener_modified_implementation.event;

public class Panchayat extends Episode {

    public static final String showName = "panchayat";

    private final String episodeNo;

    public Panchayat(Object source, String episodeNo) {
        super(source);
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
