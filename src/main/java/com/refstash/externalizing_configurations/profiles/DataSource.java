package com.refstash.externalizing_configurations.profiles;

public class DataSource {
    private String implEnvironment;

    public DataSource(String implEnvironment) {
        this.implEnvironment = implEnvironment;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "implEnvironment='" + implEnvironment + '\'' +
                '}';
    }
}
