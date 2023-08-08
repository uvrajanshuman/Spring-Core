package com.refstash.advanced_bean_processing_and_lifecycle_management.aware_interfaces.beanFactoryAware;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        BeanFactoryAwareBean bean = applicationContext.getBean(BeanFactoryAwareBean.class);
        System.out.println("contains BeanFactory instance: " +bean.containsBeanFactory());
    }
}
/*
 * Output:
 * contains BeanFactory instance: true
 */