package com.refstash.advanced_bean_processing_and_lifecycle_management.aware_interfaces.beanNameAware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

@Component
public class BeanNameAwareBean implements BeanNameAware {
    private String name;

    @Override
    public void setBeanName(String name) {
        this.name = name;
        System.out.println("BeanNameAwareBean aware of it's name: " + name);
    }
}
