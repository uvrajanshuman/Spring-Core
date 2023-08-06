package com.refstash.dependency_injection.circular_dependencies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanB {
    private BeanA beanA;

    @Autowired
    public BeanB(BeanA beanA) {
        this.beanA = beanA;
    }

   //..
}
