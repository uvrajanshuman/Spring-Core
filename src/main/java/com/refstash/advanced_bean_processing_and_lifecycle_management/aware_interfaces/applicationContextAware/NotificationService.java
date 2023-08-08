package com.refstash.advanced_bean_processing_and_lifecycle_management.aware_interfaces.applicationContextAware;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class NotificationService implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private NotificationServiceProvider notificationProvider;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        init(); // Perform custom initialization after obtaining the BeanFactory
    }

    private void init() {
        // Determine the notification provider based on external configuration
        String notificationProviderName = applicationContext.getEnvironment().getProperty("notification.provider");

        // Lookup the appropriate provider bean based on the configuration
        notificationProvider = applicationContext.getBean(notificationProviderName, NotificationServiceProvider.class);
    }

    public void sendNotification(String recipient, String message) {
        notificationProvider.sendNotification(recipient, message);
    }

    //...
}