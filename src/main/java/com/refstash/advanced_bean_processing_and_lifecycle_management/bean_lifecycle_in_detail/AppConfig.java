package com.refstash.advanced_bean_processing_and_lifecycle_management.bean_lifecycle_in_detail;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

@Configuration
public class AppConfig {

    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
    public AwareBean awareBean() {
        AwareBean awareBean = new AwareBean();
        awareBean.setValue(10);
        return awareBean;
    }

    @Bean
    public CustomBeanFactoryPostProcessor customBeanFactoryPostProcessor() {
        return new CustomBeanFactoryPostProcessor();
    }

    @Bean
    public CustomBeanPostProcessor customBeanPostProcessor() {
        return new CustomBeanPostProcessor();
    }

    public static void main(String[] args) {
        AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println("ApplicationContext ready to use");
        AwareBean awareBean = applicationContext.getBean(AwareBean.class);
        awareBean.doSomething();
        applicationContext.close();
    }
}

/*
customBeanFactoryPostProcessor: BeanFactoryPostProcessor's postProcessBeanFactory()
awareBean: constructor
awareBean: setValue()
awareBean: BeanNameAware's setBeanName()
awareBean: BeanFactoryAware's setBeanFactory()
awareBean: ApplicationContextAwrae's setApplicationContext()
awareBean : BeanPostProcessor's postProcessBeforeInitialization()
awareBean: @PostConstruct
awareBean: InitializingBean's afterPropertiesSet()
awareBean: custom init()
awareBean : BeanPostProcessor's postProcessAfterInitialization()
ApplicationContext ready to use
awareBean: doSomething()
awareBean: @PreDestroy
awareBean: DisposableBean's destroy()
awareBean: custom destroy()
 */