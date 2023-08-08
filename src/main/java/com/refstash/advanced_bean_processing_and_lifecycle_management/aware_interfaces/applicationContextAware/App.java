package com.refstash.advanced_bean_processing_and_lifecycle_management.aware_interfaces.applicationContextAware;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:com/refstash/aware_interfaces/applicationContextAware/application.properties")
@Configuration
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.refstash.aware_interfaces.applicationContextAware");
        context.refresh();

        NotificationService notificationService = context.getBean(NotificationService.class);
        notificationService.sendNotification("user@example.com", "Hello, this is a notification!");

        context.close();
    }
}
