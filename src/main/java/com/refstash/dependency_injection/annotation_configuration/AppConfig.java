package com.refstash.dependency_injection.annotation_configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource("classpath:com/refstash/dependency_injection/annotation_configuration/application.properties")
public class AppConfig {
}
