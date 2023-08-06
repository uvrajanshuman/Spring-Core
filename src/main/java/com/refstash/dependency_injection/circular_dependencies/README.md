# Dependency resolution in Spring

The creation of a bean can potentially lead to a graph of beans being created, including the bean's dependencies and their dependencies, and so on. 
This ensures that all the required dependencies are available and properly assigned before the beans are used in the application.

Example: Let's say **BeanA** depends on **BeanB** which in turn depends on **BeanC**
- **BeanA &#8594; BeanB &#8594; BeanC**

Spring will first create **BeanC**, then create **BeanB** injecting **BeanC** into it, and it will finally create **BeanA** injecting the **BeanB**.
Generally when one or more dependency beans are being injected into a dependent bean,
each dependency bean is totally configured prior to being injected into the dependent bean.
This ensures that all dependencies are properly resolved before they are used in the application.

>Through constructor injection, it is possible to create an unresolvable circular dependency.

# Circular Dependencies

Circular dependencies can occur when beans depend on each other, creating a situation where one bean requires another bean before it's fully initialized itself. 

Example: Let's say **BeanA** depends on **BeanB** which in turn depends back on **BeanA**
- **BeanA &#8594; BeanB &#8594; BeanA**

Here in, Spring cannot decide which of the beans should be created first since they depend on one another.

This happens with **Constructor injection**, in other variants of dependency injection this isn't the problem as they can
be instantiated independently (their constructor are not dependent on each other).

Spring's IOC container detects circular dependencies during runtime and throws a `BeanCurrentlyInCreationException` if they are configured using constructor injection. 

### Example: Circular Dependency
```java
//imports

@Component
public class BeanA {
    private BeanB beanB;

    @Autowired
    public BeanA(BeanB beanB) {
        this.beanB = beanB;
    }

    //..
}

@Component
public class BeanB {
    private BeanA beanA;

    @Autowired
    public BeanB(BeanA beanA) {
        this.beanA = beanA;
    }

   //..
}
```
- Both BeanA and BeanB are dependent on each other.
```java
//imports

@Configuration
@ComponentScan
public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        //..
    }
}
```
- The configuration is only needed to load the context.
```shell
org.springframework.beans.factory.BeanCurrentlyInCreationException: Error creating bean with name 'beanA': Requested bean is currently in creation: Is there an unresolvable circular reference?
```

>Overall, Spring's IoC container does a good job of detecting configuration problems like circular dependencies during container load-time.<br>
>However, these errors are not detected at load time for prototype scoped beans since they are lazily initialized.


## Resolutions for Circular Dependencies
The cyclic dependency can be generally considered as design problem and may require redesign (as the responsibilities are not well separated).<br>
But, it is possible to resolve it through following ways:
### **1. Using setter/field injection instead of constructor injection:**
This way, Spring creates the beans, but the dependencies are not injected until they are needed. And, since the constructors of beans are independent
there is no problem in instance creation.

```java
//imports

@Component
public class BeanA {
    private BeanB beanB;

    @Autowired
    public setBean(BeanB beanB) {
        this.beanB = beanB;
    }

    //..
}

@Component
public class BeanB {
    private BeanA beanA;

    @Autowired
    public setBeanA(BeanA beanA) {
        this.beanA = beanA;
    }

   //..
}
```

### **2. Using lazy initialization:**
A simple way to break the cycle is by telling Spring to initialize one of the beans lazily. 
So, instead of fully initializing the specific bean, it will create a proxy to inject it into the other bean. 
The injected bean will only be fully created when it’s first needed.
```java
//imports

@Component
public class BeanA {
    private BeanB beanB;

    @Autowired
    public BeanA(@Lazy BeanB beanB) {
        this.beanB = beanB;
    }

    //..
}

@Component
public class BeanB {
    private BeanA beanA;

    @Autowired
    public BeanB(BeanA beanA) {
        this.beanA = beanA;
    }

   //..
}
```

### **3. Using Bean's Lifecycle initialization callback method:**

Another way of breaking this cyclic dependency would be using the same bean to perform dependency injection of both the beans.
i.e: The current bean's dependency can be set through normal constructor injection, while other bean's dependency can be set
through the lifecycle initialization callback method of same bean (wherein current instance can be passed as dependency).

```java
//imports

@Component
public class BeanA {
    private BeanB beanB;

    @Autowired
    public BeanA(BeanB beanB) {
        this.beanB = beanB;
    }

    @PostConstruct
    public void init() {
        //setting current instance as dependency of BeanB
        this.beanB.setBeanA(this);
    }

    //..
}

@Component
public class BeanB {
    private BeanA beanA;
    
    public void setBeanA(BeanA beanA) {
        this.beanA = beanA;
    }

    //..
}
```

### **4. Using ApplicationContext for manual wiring:**

Another way would be custom wiring of dependent beans by making a bean aware of `ApplicationContext` either through `ApplicationContextAware`
interface or by directly autowiring the `ApplicationContext`, while the other bean can have normal dependency injection through constructor.

```java
//imports

@Component
public class BeanA {
    private BeanB beanB;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        this.beanB = applicationContext.getBean(BeanB.class);
    }

    //..
}

@Component
public class BeanB {
    private BeanA beanA;

    @Autowired
    public BeanB(BeanA beanA) {
        this.beanA = beanA;
    }

    //..
}
```

**Conclusion:**<br>
The circular dependency should first be considered for redesigning, but if redesigning is not possible the described workarounds
can be used to resolve it.<br>
Out of these workarounds, setter injection is the one suggested by Spring framework.
