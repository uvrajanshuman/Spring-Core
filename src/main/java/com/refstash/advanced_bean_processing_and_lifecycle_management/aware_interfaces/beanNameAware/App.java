package com.refstash.advanced_bean_processing_and_lifecycle_management.aware_interfaces.beanNameAware;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
    }
}
/*
 * Output:
 * BeanNameAwareBean aware of it's name: beanNameAwareBean
 */