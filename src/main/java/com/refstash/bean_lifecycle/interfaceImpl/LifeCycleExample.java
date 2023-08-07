package com.refstash.bean_lifecycle.interfaceImpl;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class LifeCycleExample implements InitializingBean, DisposableBean {
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
        System.out.println("getX() invoked");
        this.x = x;
    }

    @Override
    public String toString() {
        System.out.println("toString() invoked");
        return "LifeCycleExample{" +
                "x=" + x +
                '}';
    }

    public void init() {
        System.out.println("init() method invoked");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet() method invoked");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy() method invoked");
    }

}
