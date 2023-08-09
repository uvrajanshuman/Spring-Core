package com.refstash.advanced_bean_processing_and_lifecycle_management.bean_post_processor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan
public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        MonitoredBean monitoredBean = applicationContext.getBean(MonitoredBean.class);
        monitoredBean.doSomething();
    }
}

/*
 * Output:
 * MonitoredBean before : doSomething
 * SampleMonitoredBean: doSomething
 * MonitoredBean after : doSomething
 */