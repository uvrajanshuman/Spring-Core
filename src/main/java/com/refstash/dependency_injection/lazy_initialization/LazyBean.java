package com.refstash.dependency_injection.lazy_initialization;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Lazy
@Component
public class LazyBean {

    @PostConstruct
    public void onInit() {
        System.out.println("LazyBean created!!");
    }

    public void doNothing() {}
}