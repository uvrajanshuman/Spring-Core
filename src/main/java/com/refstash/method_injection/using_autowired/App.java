package com.refstash.method_injection.using_autowired;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class App {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        NotificationService notificationService = applicationContext.getBean(NotificationService.class);

        String message = "Hello, Spring!";
        String userPreference = "email"; // or "sms"
        notificationService.sendNotification(message, userPreference);
    }
}

/*
 * Output:
 * Sending email notification: Hello, Spring!
 */
