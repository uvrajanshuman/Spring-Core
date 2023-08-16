package com.refstash.externalizing_configurations.profiles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppConfig {

    @Bean("dataSource")
    @Profile("development")
    public DataSource localDataSource() {
        // Configuration for development profile
        return new DataSource("dev");
    }

    @Bean("dataSource")
    @Profile("production")
    public DataSource cloudDataSourceConfig() {
        // Configuration for production profile
        return new DataSource("prod");
    }

    @Bean("dataSource")
    @Profile("default")
    public DataSource dataSource() {
        // Configuration for default profile
        return new DataSource("default");
    }
}
