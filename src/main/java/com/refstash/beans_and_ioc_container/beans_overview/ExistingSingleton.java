package com.refstash.beans_and_ioc_container.beans_overview;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

public class ExistingSingleton {
    private final String message;

    public ExistingSingleton(String message) {
        this.message = message;
    }

    public void displayMessage() {
        System.out.println("Message from ExistingSingleton: " + message);
    }
}

@Configuration
class App{
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);

        ExistingSingleton existingSingleton = new ExistingSingleton("manually created singleton");
        //registering an existing singleton class
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        listableBeanFactory.registerSingleton("existingSingleton",existingSingleton);

        applicationContext.getBean(ExistingSingleton.class).displayMessage();

    }

}

/*
 * Output:
 * Message from ExistingSingleton: manually created singleton
 */