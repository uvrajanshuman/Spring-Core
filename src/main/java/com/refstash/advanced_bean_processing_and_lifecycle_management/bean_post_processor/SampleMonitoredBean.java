package com.refstash.advanced_bean_processing_and_lifecycle_management.bean_post_processor;

import org.springframework.stereotype.Component;

@Component
public class SampleMonitoredBean implements MonitoredBean{
    @Override
    public void doSomething() {
        System.out.println("SampleMonitoredBean: doSomething");
    }
}
