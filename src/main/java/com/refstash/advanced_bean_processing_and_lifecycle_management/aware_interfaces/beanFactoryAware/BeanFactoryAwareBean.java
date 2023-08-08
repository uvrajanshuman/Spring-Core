package com.refstash.advanced_bean_processing_and_lifecycle_management.aware_interfaces.beanFactoryAware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanFactoryAwareBean implements BeanFactoryAware {
//    @Autowired
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public boolean containsBeanFactory() {
        return  beanFactory != null;
    }
}
