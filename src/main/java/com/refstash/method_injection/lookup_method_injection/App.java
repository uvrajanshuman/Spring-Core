package com.refstash.method_injection.lookup_method_injection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Configuration
@ComponentScan
public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        SingletonBean singletonBean = applicationContext.getBean(SingletonBean.class);
        singletonBean.getPrototypeDependency().processData();
        singletonBean.getPrototypeDependency().processData();
    }
}

/*
 * Output:
 * Processing data in PrototypeDependency 1
 * Processing data in PrototypeDependency 2
 */