# Spring Bean Scopes

The scope of a bean defines the lifecycle and visibility of the bean within the application. 
Spring provides several bean scopes that determine how instances of beans are created and managed.

Here are the commonly used bean scopes in Spring:

1. Singleton: This is the default scope in Spring. A singleton bean is created once per container and shared by all the requests for that bean. It means that all the requests for the bean will receive the same instance.

2. Prototype: A prototype bean is created each time it is requested from the container. It means that a new instance is created every time the bean is injected into another bean or is requested.

3. Request: A request-scoped bean is created once per HTTP request. It means that each new HTTP request will have its own instance of the bean. This scope is typically used for web applications.

4. Session: A session-scoped bean is created once per user session. It means that each new user session will have its own instance of the bean. This scope is also used in web applications.

5. Application: An application-scoped bean is created once for the entire application context. It means that all requests within the application will share the same instance of the bean. Only valid in the context of a web-aware Spring ApplicationContext.

6. Websocket: This scope is specific to WebSocket-based applications. A bean with this scope is created once per WebSocket session.

The last four scopes mentioned request, session, application and websocket, are only available in a web-aware application.
The singleton and prototype scopes are available in any type of IOC container.

These bean scopes allow you to control how instances of beans are managed and shared within the application, based on your specific requirements.<br>
You can define the scope of a bean by using annotations like `@Scope` or XML configuration in Spring.

## Singleton
This is the default bean scope.

The Spring container creates and manages only one bean class instance per container.<br>
This single instance is stored in a cache of such singleton beans, and all subsequent requests and references for that named bean return the cached instance.

By default, ApplicationContext eagerly initializes all the Singleton beans during the initialization process. This is generally
desirable as it helps to detect the configuration issues in early stages.<br>
But, when this behaviour is not desirable the **Singleton** beans can be marked for lazy-initialization; and the IOC container
will create the bean instance when it is first requested, rather than at startup.<br>
[Lazy initailization of Singleton beans](../dependency_injection/lazy_initialization/README.md)

Spring manages the entire lifecycle of singleton beans.

>**Note:**<br>
> Spring's singleton beans are a bit different from the singleton pattern defined in Gang of Four (GOF) patterns book.<br>
> As per GOF there should be single instance of Singleton class per ClassLoader, but Spring's singleton beans have a single
> instance per container/application context.

[SingletonBean.java](./SingletonBean.java)
```java
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) //This statement is redundant - singleton is default scope
//@Scope("singleton")
public class SingletonBean {
    //Bean implementation
}
```

[App.java](./App.java)
```java
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
    }
}
```
Output:
```shell
Singleton scoped bean:
singletonInstance1.hashcode : 20094719, singletonInstance2.hashcode: 20094719
singletonInstance1 == singletonInstance2 : true
```
> The container provided same instance for each request.

## Prototype

For Prototype scoped beans the spring container creates a new bean instance each time a request for the bean is made by the application code.

In contrast to the other scopes, Spring does not manage the complete lifecycle of a prototype bean. 
The destruction bean lifecycle methods are not called for prototype scoped beans; only initialization callback methods are called. 
So, its developer's responsibility to clean up prototype-scoped bean instances and any resources they hold.<br>
To get the Spring container to release resources held by prototype-scoped beans, a custom bean post-processor can be implemented, 
which holds a reference to all the beans that need to be cleaned up.

[Prototype.java](./PrototypeBean.java)
```java
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeBean {
}
```

[App.java]
```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        PrototypeBean prototypeInstance1 = context.getBean(PrototypeBean.class);
        PrototypeBean prototypeInstance2 = context.getBean(PrototypeBean.class);
        System.out.println("\nPrototype scoped bean:");
        System.out.println("prototypeInstance1.hashcode : "+prototypeInstance1.hashCode()
                +", prototypeInstance2.hashcode: "+prototypeInstance2.hashCode());
        System.out.println("prototypeInstance1 == prototypeInstance2 : "+ (prototypeInstance1 == prototypeInstance2));
    }
}
```
Output:
```shell
Prototype scoped bean:
prototypeInstance1.hashcode : 773662650, prototypeInstance2.hashcode: 1641415002
prototypeInstance1 == prototypeInstance2 : false
```
>The container provided different instance for each request.

>Singleton: Usually preferred for stateless beans, generic beans that do not have any user information and can be used across the application.<br>
>Prototype beans: Usually preferred for stateful beans. Ex. if we want to create a bean to hold user info, in such cases single bean can not be used across the application

## Complex scope scenarios

**Case 1: Bean, and it's dependency both are of Singleton scope**<br>
- In this case same instance of bean and the same instance of dependency will be provided each time it is requested.

**Case 2: Bean is of scope Prototype but dependency is of scope Singleton**<br>
- In this case for each request a new instance of bean will be provided, but the dependency of these instances will always
remain the same instance of dependency class.

**Case 3: Bean is of scope Singleton but dependency is of scope Prototype**<br>
- In this case even though dependency has Prototype scope, still for each request the same instance of bean as well as same
instance of dependency will be provided.
- Since the container creates the singleton bean only once, it gets only one opportunity to set the properties.
  As a result, the container cannot provide the singleton bean with a new instance of the non-singleton bean every time it is needed.

### Workarounds for Singleton beans with Prototype scoped dependencies.

As specified above, even if a singleton bean has prototype scoped dependency, the same instance of bean and its dependency
will be provided for each request.<br>

#### **1. Configuring proxy:**

- In case a new instance of dependency is desired each time the main bean is requested, then **proxy** needs to be configured.
- Proxy will ensure a new instance of dependency is provided each time. Instead of real instance, a proxy object gets injected as
dependency, and whenever getter method is called on it, a new dependency instance is returned.

[DependentClass.java](./DependentClass.java)
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DependentClass {
    @Autowired
    private DependencyClass dependencyClass;

    public DependencyClass getDependencyClass() {
        return dependencyClass;
    }

    public void setDependencyClass(DependencyClass dependencyClass) {
        this.dependencyClass = dependencyClass;
    }
}
```

[DependencyClass.java](./DependencyClass.java)
```java
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DependencyClass {
}
```

[App.java](./App.java)
```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        DependentClass singletonBeanPrototypeDependencyInstance1 = context.getBean(DependentClass.class);
        DependentClass singletonBeanPrototypeDependencyInstance2 = context.getBean(DependentClass.class);
        System.out.println("\nSingleton bean with Prototype scoped dependency:");
        System.out.println("bean1.hashcode: " + singletonBeanPrototypeDependencyInstance1.hashCode()
                + ", bean2.hashcode: " + singletonBeanPrototypeDependencyInstance2.hashCode());
        System.out.println("bean1.dependency : " + singletonBeanPrototypeDependencyInstance1.getDependencyClass()
                + ", bean2.dependency : " + singletonBeanPrototypeDependencyInstance2.getDependencyClass());

    }
}
```
Output:
```shell
Singleton bean with Prototype scoped dependency:
bean1.hashcode: 623247230, bean2.hashcode: 623247230
bean1.dependency : com.refstash.bean_scope.DependencyClass@4da4253, bean2.dependency : com.refstash.bean_scope.DependencyClass@3972a855
```

>Different instances of Dependency class provided

#### **2. Using ApplicationContext directly:**
One solution to this is to forego some inversion of control; and make the dependent bean aware of `ApplicationContext` by implementing the `ApplicationContextAware`
interface (or directly autowiring ApplicationContext) and by using `getBean()` method to request a new instance of the prototype dependency whenever needed.

[Using ApplicationContext](../method_injection/README.md#using-applicationcontext-directly)

#### **3. Using Method injection:**
Lookup method injection provides a cleaner approach to handle the dependency between singleton and prototype beans.
It involves declaring a method as a lookup method in a bean and letting Spring provide the implementation dynamically at runtime.

[Using Method injection](../method_injection/README.md#lookup-method-injection)
