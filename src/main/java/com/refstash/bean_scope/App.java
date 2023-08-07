package com.refstash.bean_scope;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        SingletonBean singletonInstance1 = context.getBean(SingletonBean.class);
        SingletonBean singletonInstance2 = context.getBean(SingletonBean.class);
        System.out.println("Singleton scoped bean:");
        System.out.println("singletonInstance1.hashcode : "+singletonInstance1.hashCode()
                +", singletonInstance2.hashcode: "+singletonInstance2.hashCode());
        System.out.println("singletonInstance1 == singletonInstance2 : "+ (singletonInstance1 == singletonInstance2));

        PrototypeBean prototypeInstance1 = context.getBean(PrototypeBean.class);
        PrototypeBean prototypeInstance2 = context.getBean(PrototypeBean.class);
        System.out.println("\nPrototype scoped bean:");
        System.out.println("prototypeInstance1.hashcode : "+prototypeInstance1.hashCode()
                +", prototypeInstance2.hashcode: "+prototypeInstance2.hashCode());
        System.out.println("prototypeInstance1 == prototypeInstance2 : "+ (prototypeInstance1 == prototypeInstance2));

        DependentClass singletonBeanPrototypeDependencyInstance1 = context.getBean(DependentClass.class);
        DependentClass singletonBeanPrototypeDependencyInstance2 = context.getBean(DependentClass.class);
        System.out.println("\nSingleton bean with Prototype scoped dependency:");
        System.out.println("bean1.hashcode: " + singletonBeanPrototypeDependencyInstance1.hashCode()
                + ", bean2.hashcode: " + singletonBeanPrototypeDependencyInstance2.hashCode());
        System.out.println("bean1.dependency : " + singletonBeanPrototypeDependencyInstance1.getDependencyClass()
                + ", bean2.dependency : " + singletonBeanPrototypeDependencyInstance2.getDependencyClass());

    }
}

/*
 * Output:
 * Singleton scoped bean:
 * singletonInstance1.hashcode : 20094719, singletonInstance2.hashcode: 20094719
 * singletonInstance1 == singletonInstance2 : true
 *
 * Prototype scoped bean:
 * prototypeInstance1.hashcode : 773662650, prototypeInstance2.hashcode: 1641415002
 * prototypeInstance1 == prototypeInstance2 : false
 *
 * Singleton bean with Prototype scoped dependency:
 * bean1.hashcode: 623247230, bean2.hashcode: 623247230
 * bean1.dependency : com.refstash.bean_scope.DependencyClass@4da4253, bean2.dependency : com.refstash.bean_scope.DependencyClass@3972a855
 */