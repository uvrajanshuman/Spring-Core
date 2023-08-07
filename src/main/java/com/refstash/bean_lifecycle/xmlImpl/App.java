package com.refstash.bean_lifecycle.xmlImpl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/refstash/bean_lifecycle/xmlImpl/config.xml");
//        ((AbstractApplicationContext)context).registerShutdownHook();
        LifeCycleExample lifeCycleExample = context.getBean("lifeCycleExample",LifeCycleExample.class);
        System.out.println(lifeCycleExample);
       ((AbstractApplicationContext) context).close();
    }
}

/*
 * Output:
 * no-arg constructor invoked
 * setX() invoked
 * init() method invoked
 * toString() invoked
 * LifeCycleExample{x=10}
 * destroy() method invoked
 */
