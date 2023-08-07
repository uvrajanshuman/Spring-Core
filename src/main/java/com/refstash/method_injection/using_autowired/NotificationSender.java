package com.refstash.method_injection.using_autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public interface NotificationSender {
    void sendNotification(String message);
}

@Component("emailNotification")
class EmailNotification implements NotificationSender {
    @Override
    public void sendNotification(String message) {
        System.out.println("Sending email notification: " + message);
    }
}

@Component("smsNotification")
class SmsNotification implements NotificationSender {
    @Override
    public void sendNotification(String message) {
        System.out.println("Sending SMS notification: " + message);
    }
}

@Service
class NotificationService {
    private NotificationSender emailNotification;
    private NotificationSender smsNotification;

    @Autowired
    public void setEmailNotification(NotificationSender emailNotification) {
        this.emailNotification = emailNotification;
    }

    @Autowired
    public void setSmsNotification(NotificationSender smsNotification) {
        this.smsNotification = smsNotification;
    }

    public void sendNotification(String message, String userPreference) {
        if ("email".equalsIgnoreCase(userPreference)) {
            emailNotification.sendNotification(message);
        } else if ("sms".equalsIgnoreCase(userPreference)) {
            smsNotification.sendNotification(message);
        } else {
            throw new IllegalArgumentException("Invalid user preference");
        }
    }
}

