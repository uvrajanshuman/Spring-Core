package com.refstash.advanced_bean_processing_and_lifecycle_management.bean_lifecycle_in_detail;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class CustomBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName+" : BeanPostProcessor's postProcessBeforeInitialization()");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName+" : BeanPostProcessor's postProcessAfterInitialization()");
        return bean;
    }
}
