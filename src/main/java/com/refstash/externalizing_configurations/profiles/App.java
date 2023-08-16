package com.refstash.externalizing_configurations.profiles;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        /*
         setting the active profile, if none specified default is active
         */
//        applicationContext.getEnvironment().setActiveProfiles("development");
        applicationContext.getEnvironment().setActiveProfiles("production");

        /*
         The configuration should be registered after setting the profile
        */
        applicationContext.register(AppConfig.class);
        applicationContext.refresh();

        DataSource dataSource = applicationContext.getBean(DataSource.class);
        System.out.println(dataSource);
    }
}

/*
 * Output: (when no profile is specified)
 * DataSource{implEnvironment='default'}
 */

/* Output: (when "development" profile is active)
 * DataSource{implEnvironment='dev'}
 */

/* Output: (when "production" profile is active)
 * DataSource{implEnvironment='prod'}
 */