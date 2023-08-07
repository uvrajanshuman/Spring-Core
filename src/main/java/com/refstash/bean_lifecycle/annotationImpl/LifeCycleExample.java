package com.refstash.bean_lifecycle.annotationImpl;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class LifeCycleExample {
    private int x;

    public LifeCycleExample() {
        System.out.println("no-arg constructor invoked");
    }

    public LifeCycleExample(int x) {
        System.out.println("one-arg constructor invoked");
        this.x = x;
    }

    public int getX() {
        System.out.println("getX() invoked");
        return x;
    }

    public void setX(int x) {
        System.out.println("setX() invoked");
        this.x = x;
    }

    @Override
    public String toString() {
        System.out.println("toString() invoked");
        return "LifeCycleExample{" +
                "x=" + x +
                '}';
    }

    @PostConstruct
    public void init() {
        System.out.println("init() method invoked");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("destroy() method invoked");
    }

}
