package com.refstash.advanced_bean_processing_and_lifecycle_management.aware_interfaces.applicationContextAware;

import org.springframework.stereotype.Component;

public interface NotificationServiceProvider {
    void sendNotification(String recipient, String message);
}

@Component("emailProvider")
class EmailProvider implements NotificationServiceProvider {
    @Override
    public void sendNotification(String recipient, String message) {
        // Send notification via email
        System.out.println("Sending email notification to " + recipient + ": " + message);
    }
}

@Component("smsProvider")
class SMSProvider implements NotificationServiceProvider {
    @Override
    public void sendNotification(String recipient, String message) {
        // Send notification via SMS
        System.out.println("Sending SMS notification to " + recipient + ": " + message);
    }
}

@Component("pushNotificationProvider")
class PushNotificationProvider implements NotificationServiceProvider {
    @Override
    public void sendNotification(String recipient, String message) {
        // Send notification via push notification
        System.out.println("Sending push notification to " + recipient + ": " + message);
    }
}