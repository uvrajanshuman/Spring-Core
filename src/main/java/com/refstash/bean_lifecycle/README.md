# Spring Bean Lifecycle Callbacks

There are list of activities that happen behind the scenes between bean instantiation and destruction.<br>
Two of the major callbacks are:
1. **Initialization callback** : The callback happens at the time of bean initialization. Gets executed as soon as the bean is initialized. 
2. **Destruction callback** : The callback happens at the time of bean destruction. Gets executed as just before the bean is destroyed.

Spring provides support of Lifecycle callbacks in all the configuration types.<br>
It also provides an additional way to configure Lifecycle callbacks through Interfaces.

1. [Lifecycle callbacks in XML based configuration](#lifecycle-callbacks-in-xml-based-configuration)
2. [Lifecycle callbacks in Annotation based configuration](#lifecycle-callbacks-in-annotation-based-configuration)
3. [Lifecycle callbacks in Java based configuration](#lifecycle-callbacks-in-java-based-configuration)
4. [Lifecycle callbacks through Interfaces implementations](#lifecycle-callbacks-through-interfaces-implementations)

## Shutting Down the Spring IoC Container Gracefully in Non-Web Applications
Spring's web-based `ApplicationContext` implementations implicitly handle the graceful shut-down of the IOC container, when the relevant web app is shut down.

But, in a non-web application the resources are not released automatically on the IOC container shutdown.<br>
In a non-web application there is a need to register a shutdown hook with JVM. 
Doing so ensures a graceful shutdown and calls the relevant destroy methods on the singleton beans so 
that all resources are released. <br>
The destroy callback methods still needs to configured.

To register a shutdown hook, the `registerShutdownHook()` method needs to be called that is declared on the `ConfigurableApplicationContext` interface,
as the following example shows:

```java
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class App {

	public static void main(final String[] args) throws Exception {
		ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

		// add a shutdown hook for the above context...
		ctx.registerShutdownHook();

		// ...

		// main method exits, hook is called prior to the app shutting down...
	}
}

```
Another way would be manually closing the container using `close()` method on the container.

## Lifecycle callbacks in XML based configuration

In the case of XML-based configuration, the `init-method` attribute is used to specify the name of the method that has a void no-argument signature 
which will get executed as soon as the bean is initialized.<br>
Similarly, the `destroy-method` attribute is used to specify the name of the method that has a void no-argument signature which will get executed
just before the bean is destroyed.

[_LifeCycleExample.java_](./xmlImpl/LifeCycleExample.java)
```java
public class LifeCycleExample {
    private int x;

    public LifeCycleExample() {
        System.out.println("no-arg constructor invoked");
    }

    public LifeCycleExample(int x) {
        System.out.println("one-arg constructor invoked");
        this.x = x;
    }

    public int getX() {
        System.out.println("getX() invoked");
        return x;
    }

    public void setX(int x) {
        System.out.println("setX() invoked");
        this.x = x;
    }

    @Override
    public String toString() {
        System.out.println("toString() invoked");
        return "LifeCycleExample{" +
                "x=" + x +
                '}';
    }

    public void init() {
        System.out.println("init() method invoked");
    }

    public void destroy() {
        System.out.println("destroy() method invoked");
    }
}
```

[_config.xml_](../../../../resources/com/refstash/bean_lifecycle/xmlImpl/config.xml)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--bean declarations-->
    <bean class="com.refstash.bean_lifecycle.xmlImpl.LifeCycleExample"
          id="lifeCycleExample"
          init-method="init"
          destroy-method="destroy">
        <property name="x" value="10"/>
    </bean>
</beans>
```
- `init-method` specifies the Initialization callback method name.
- `destroy-method` specifies the Destruction callback method name.
- These methods can be named anything, as long as they have the appropriate method signature.

[_App.java_](./xmlImpl/App.java)
```java
//imports
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/refstash/bean_lifecycle/xmlImpl/config.xml");
        ((AbstractApplicationContext)context).registerShutdownHook();
//       ((AbstractApplicationContext) context).close();
        LifeCycleExample lifeCycleExample = context.getBean("lifeCycleExample",LifeCycleExample.class);
        System.out.println(lifeCycleExample);
    }
}
```
Output:
```shell
 no-arg constructor invoked
 setX() invoked
 init() method invoked
 toString() invoked
 LifeCycleExample{x=10}
 destroy() method invoked
```
**Default initialization and destroy methods :**

In case there are several beans having initialization and/or destroy methods with the same name, then there is no need to declare `init-method` and `destroy-method` on each `<bean/>`<br>
Instead, the `default-init-method` and `default-destroy-method` attribute can be used on `<beans>` element to define the callback method names globally.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd"
       default-init-method = "init"
       default-destroy-method = "destroy">

    <!--bean declarations-->
    <bean class="com.refstash.bean_lifecycle.xmlImpl.LifeCycleExample"
          id="lifeCycleExample">
        <property name="x" value="10"/>
    </bean>
</beans>
```
- Now, all the bean declaration will follow this convention **init** as initialization method name and **destroy** as destruction method name.

## Lifecycle callbacks in Annotation based configuration

In the case of Annotation based configuration, the `@PostConstruct` annotation is used to specify the name of the method that has a void no-argument signature
which will get executed as soon as the bean is initialized.<br>
Similarly, the `@Predestroy` annotation is used to specify the name of the method that has a void no-argument signature which will get executed
just before the bean is destroyed.

**Note:**<br>
- The `@PostConstruct` and `@PreDestroy` annotations are part of Jakarta EE.
- These annotations were part of standard Java libraries from JDK 6 to 8. Then the entire `javax.annotation` package got 
separated from core Java modules in JDK 9 and eventually got removed in JDK 11.<br>
As of Jakarta EE 9, the package now resides in `jakarta.annotation` now
- So an additional dependency (`jakarta.annotation`) needs to be added to use these annotations:

[_build.gradle_](../../../../../../build.gradle)
```properties
...
dependencies {
    //...
    // https://mvnrepository.com/artifact/jakarta.annotation/jakarta.annotation-api
    implementation group: 'jakarta.annotation', name: 'jakarta.annotation-api', version: '2.1.1'
}
```

[_LifeCycleExample.java_](./annotationImpl/LifeCycleExample.java)
```java
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class LifeCycleExample {
    private int x;

    public LifeCycleExample() {
        System.out.println("no-arg constructor invoked");
    }

    public LifeCycleExample(int x) {
        System.out.println("one-arg constructor invoked");
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        System.out.println("toString() invoked");
        return "LifeCycleExample{" +
                "x=" + x +
                '}';
    }

    @PostConstruct
    public void init() {
        System.out.println("init() method invoked");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("destroy() method invoked");
    }
}
```
- `@PostConstruct` specifies Initialization callback method.
- `@PreDestroy` specifies Destruction callback method.
- These methods can be named anything, as long as they have the appropriate method signature.

[_config.xml_](../../../../resources/com/refstash/bean_lifecycle/annotationImpl/config.xml)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <!--bean declarations-->
    <!-- by default @PostConstruct and @PreDestroy are disabled in xml based declarations -->

    <!-- to enable all annotations  -->
    <context:annotation-config/>

    <!-- To enable @PostConstruct and @PreDestroy only
    <bean class="org.springframework.context.annotation.CommonAnnotationBeanPostProcessor"/>
    -->
    <bean name="lifeCycleExample" class="com.refstash.bean_lifecycle.annotationImpl.LifeCycleExample">
        <property name="x" value="10"/>
    </bean>
</beans>
```
- The `CommonAnnotationBeanPostProcessor` provides support for `@Resource`, JSR-250 lifecycle annotations: `jakarta.annotation.PostConstruct` and `jakarta.annotation.PreDestroy`.
- `<context:annotation-config/>` enables major spring annotations and a large number of bean post processors including `CommonAnnotationBeanPostProcessor`.

The configuration can also be provided as Java based configuration instead of xml configuration.

[_App.java_](./annotationImpl/App.java)
```java
//imports
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/refstash/bean_lifecycle/annotationImpl/config.xml");
        ((AbstractApplicationContext)context).registerShutdownHook();
//       ((AbstractApplicationContext) context).close();
        LifeCycleExample lifeCycleExample = context.getBean("lifeCycleExample", LifeCycleExample.class);
        System.out.println(lifeCycleExample);
    }
}
```

Output:
```shell
no-arg constructor invoked
setX() invoked
init() method invoked
toString() invoked
LifeCycleExample{x=10}
destroy() method invoked
```

## Lifecycle callbacks in Java based configuration

In the case of Java based configuration, the `initMethod` attribute of `@Bean` annotation is used to specify the name of the method that has a void no-argument signature
which will get executed as soon as the bean is initialized.<br>
Similarly, the `destroyMethod` attribute of `@Bean` annotation is used to specify the name of the method that has a void no-argument signature which will get executed
just before the bean is destroyed.

[_LifeCycleExample.java_](./javaImpl/LifeCycleExample.java)
```java
public class LifeCycleExample {
    private int x;

    public LifeCycleExample() {
        System.out.println("no-arg constructor invoked");
    }

    public LifeCycleExample(int x) {
        System.out.println("one-arg constructor invoked");
        this.x = x;
    }

    public int getX() {
        System.out.println("getX() invoked");
        return x;
    }

    public void setX(int x) {
        System.out.println("setX() invoked");
        this.x = x;
    }

    @Override
    public String toString() {
        System.out.println("toString() invoked");
        return "LifeCycleExample{" +
                "x=" + x +
                '}';
    }

    public void init() {
        System.out.println("init() method invoked");
    }

    public void destroy() {
        System.out.println("destroy() method invoked");
    }
}
```

[_AppConfig.java_](./javaImpl/AppConfig.java)
```java
//imports
@Configuration
public class AppConfig {

    @Bean(value = "lifeCycleExample", initMethod = "init", destroyMethod = "destroy")
    public LifeCycleExample getLifeCycleExampleBean(){
        return new LifeCycleExample(10);
    }
}
```
- `initMethod` specifies initializing callback method name of the bean.
- `destroyMethod` specifies destruction callback method name of the bean.
> If there is a public method named close() or shutdown() in the bean and no explicit bean-destruction method is specified using `destroyMethod`,
> then it is automatically triggered with a destruction callback by default.

[_App.java_](./javaImpl/App.java)
```java
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ((AbstractApplicationContext)context).registerShutdownHook();
//       ((AbstractApplicationContext) context).close();
        LifeCycleExample lifeCycleExample = context.getBean("lifeCycleExample", LifeCycleExample.class);
        System.out.println(lifeCycleExample);
    }
}
```

Output:
```shell
one-arg constructor invoked
init() method invoked
toString() invoked
LifeCycleExample{x=10}
destroy() method invoked
```

## Lifecycle callbacks through Interfaces implementations

In the case of Interfaces based implementation, `InitializingBean` interface's `afterPropertiesSet()` method is used to 
specify the initialization code that will get executed as soon as the bean is initialized.<br>
Similarly, the `DisposableBean` interface's `destroy()` method is used to specify the pre-bean-destruction code that 
will get executed just before the bean is destroyed.

[_LifeCycleExample.java_](./interfaceImpl/LifeCycleExample.java)
```java
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class LifeCycleExample implements InitializingBean, DisposableBean {
    private int x;

    public LifeCycleExample() {
        System.out.println("no-arg constructor invoked");
    }

    public LifeCycleExample(int x) {
        System.out.println("one-arg constructor invoked");
        this.x = x;
    }

    public int getX() {
        System.out.println("getX() invoked");
        return x;
    }

    public void setX(int x) {
        System.out.println("getX() invoked");
        this.x = x;
    }

    @Override
    public String toString() {
        System.out.println("toString() invoked");
        return "LifeCycleExample{" +
                "x=" + x +
                '}';
    }

    public void init() {
        System.out.println("init() method invoked");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet() method invoked");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy() method invoked");
    }
}
```
- `afterPropertiesSet()` specifies initializing callback method of the bean.
- `destroy()` specifies destruction callback method of the bean.

[_config.xml_](../../../../resources/com/refstash/bean_lifecycle/interfaceImpl/config.xml)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--bean declarations-->
    <bean class="com.refstash.bean_lifecycle.interfaceImpl.LifeCycleExample"
          id="lifeCycleExample">
        <property name="x" value="10"/>
    </bean>
</beans>
```

[_App.java_](./interfaceImpl/App.java)
```java
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/refstash/bean_lifecycle/interfaceImpl/config.xml");
        ((AbstractApplicationContext)context).registerShutdownHook();
//       ((AbstractApplicationContext) context).close();
        LifeCycleExample lifeCycleExample = context.getBean("lifeCycleExample", LifeCycleExample.class);
        System.out.println(lifeCycleExample);
    }
}
```
Output:
```shell
no-arg constructor invoked
getX() invoked
afterPropertiesSet() method invoked
toString() invoked
LifeCycleExample{x=10}
destroy() method invoked
```

## Combining Lifecycle Mechanisms

Spring provides following three lifecycle callback methods:
1. The `InitializingBean` and `DisposableBean` callback interfaces
2. Custom `init()` and `destroy()` methods
3. The `@PostConstruct` and `@PreDestroy` annotations
The three bean lifecycle callbacks can be combined as well.
   
If multiple lifecycle mechanisms are configured for a bean and each mechanism is configured with a different method name, 
then each configured method is run in the following order:
1. Methods annotated with `@PostConstruct`
2. InitializingBean's afterPropertiesSet()
3. A custom configured `init()` method

Destroy methods are also called in same order:
1. Methods annotated with `@PreDestroy`
2. DisposableBean's `destroy()` method
3. A custom configured `destroy()` method

>However, if the same method name is configured for more than one lifecycle mechanisms (example: init() for Initializing Bean's implementation as well as custom init method), that method will be run only once.
