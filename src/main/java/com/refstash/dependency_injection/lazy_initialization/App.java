package com.refstash.dependency_injection.lazy_initialization;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        System.out.println("ApplicationContext initialized!!");
        LazyBean lazyBean = applicationContext.getBean(LazyBean.class);
        lazyBean.doNothing();
    }
}

/*
 * Output (without Lazy loading)
 * LazyBean created!!
 * ApplicationContext initialized!!
 */

/* Output: (with Lazy loading)
 * ApplicationContext initialized!!
 * LazyBean created!!
 */