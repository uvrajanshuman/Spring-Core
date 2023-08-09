package com.refstash.advanced_bean_processing_and_lifecycle_management.bean_lifecycle_in_detail;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("customBeanFactoryPostProcessor: BeanFactoryPostProcessor's postProcessBeanFactory()");
    }
}
