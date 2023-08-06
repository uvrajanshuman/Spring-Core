package com.refstash.dependency_injection.circular_dependencies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanA {
    private BeanB beanB;

    @Autowired
    public BeanA(BeanB beanB) {
        this.beanB = beanB;
    }

    //..

}
