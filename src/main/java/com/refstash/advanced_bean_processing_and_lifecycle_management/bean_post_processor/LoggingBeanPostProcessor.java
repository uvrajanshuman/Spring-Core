package com.refstash.advanced_bean_processing_and_lifecycle_management.bean_post_processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class LoggingBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MonitoredBean){
            return createProxyForMonitoredBean(bean);
        }
        return bean;
    }

    private Object createProxyForMonitoredBean(Object bean) {
        return Proxy.newProxyInstance(
                bean.getClass().getClassLoader(),
                new Class[]{MonitoredBean.class},
                new MonitoredBeanInvocationHandler(bean)
        );
    }

    private static class MonitoredBeanInvocationHandler implements InvocationHandler {
        private final Object targetBean;

        public MonitoredBeanInvocationHandler(Object targetBean) {
            this.targetBean = targetBean;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("MonitoredBean before : "+ method.getName());
            Object result = method.invoke(targetBean, args);
            System.out.println("MonitoredBean after : "+ method.getName());
            return result;
        }
    }
}
