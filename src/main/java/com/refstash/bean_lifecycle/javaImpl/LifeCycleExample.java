package com.refstash.bean_lifecycle.javaImpl;

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

    public void init() {
        System.out.println("init() method invoked");
    }

    public void destroy() {
        System.out.println("destroy() method invoked");
    }
}
