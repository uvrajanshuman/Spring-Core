package com.refstash.advanced_bean_processing_and_lifecycle_management.bean_factory_post_processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    @Bean
    public static PropertyOverrideBeanFactoryPostProcessor customBeanFactoryPostProcessor() {
        return new PropertyOverrideBeanFactoryPostProcessor();
    }

    @Bean
    public static CustomBeanDefinitionBeanFactoryPostProcessor customBeanDefinitionBeanFactoryPostProcessor() {
        return new CustomBeanDefinitionBeanFactoryPostProcessor();
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MyBean myBean = applicationContext.getBean("myBean", MyBean.class);
        System.out.println(myBean.getValue());

        MyAnotherBean myAnotherBean = applicationContext.getBean("myAnotherBean", MyAnotherBean.class);
        System.out.println(myAnotherBean.getValue());
    }
}

class MyBean {
    String value;

    @Value("Hello")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

class MyAnotherBean {
    String value;

    @Value("Hello")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

class PropertyOverrideBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition myBeanDefinition = beanFactory.getBeanDefinition("myBean");
        MutablePropertyValues propertyValues = myBeanDefinition.getPropertyValues();
        propertyValues.addPropertyValue("value","Hello World");
    }
}

class CustomBeanDefinitionBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        GenericBeanDefinition myBeanDefinition = new GenericBeanDefinition();
        myBeanDefinition.setBeanClass(MyAnotherBean.class);
        myBeanDefinition.getPropertyValues().add("value","Hi Buddy!");
        ((DefaultListableBeanFactory)beanFactory).registerBeanDefinition("myAnotherBean",myBeanDefinition);

    }
}

/*
 * Output:
 * Hello World
 * Hi Buddy!
 */