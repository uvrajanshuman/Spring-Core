package com.refstash.bean_lifecycle.javaImpl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ((AbstractApplicationContext)context).registerShutdownHook();
//       ((AbstractApplicationContext) context).close();
        LifeCycleExample lifeCycleExample = context.getBean("lifeCycleExample", LifeCycleExample.class);
        System.out.println(lifeCycleExample);
    }
}

/*
 * Output:
 * one-arg constructor invoked
 * init() method invoked
 * toString() invoked
 * LifeCycleExample{x=10}
 * destroy() method invoked
 */
