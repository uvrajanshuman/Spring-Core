package com.refstash.advanced_bean_processing_and_lifecycle_management.bean_lifecycle_in_detail;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AwareBean implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {

    private int value;
    public AwareBean() {
        System.out.println("awareBean: constructor");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("awareBean: BeanNameAware's setBeanName()");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("awareBean: BeanFactoryAware's setBeanFactory()");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("awareBean: ApplicationContextAwrae's setApplicationContext()");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("awareBean: InitializingBean's afterPropertiesSet()");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("awareBean: DisposableBean's destroy()");
    }

    public void customInit() {
        System.out.println("awareBean: custom init()");
    }

    public void customDestroy() {
        System.out.println("awareBean: custom destroy()");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("awareBean: @PostConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("awareBean: @PreDestroy");
    }

    public void setValue(int value) {
        System.out.println("awareBean: setValue()");
        this.value = value;
    }

    public void doSomething() {
        System.out.println("awareBean: doSomething()");
    }

}
