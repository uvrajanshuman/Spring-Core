# Lazy initialized beans

By default, ApplicationContext eagerly initializes all the Singleton beans during the initialization process. This is generally 
desirable as it helps to detect the configuration issues in early stages.<br> 
But, when this behaviour is not desirable the **Singleton** beans can be marked for lazy-initialization; and the IOC container
will create the bean instance when it is first requested, rather than at startup.

So, Lazy initialization allows us to defer the creation of singleton beans until the moment they are needed.

In can be useful in scenarios where creating all beans during application startup might lead to unnecessary resource consumption and performance issues.

## Approaches to Implement Lazy Initialization
1. Lazy initialization of Specific Beans
2. Lazy initialization of entire configuration (all the contained beans will be lazily initialized)

### Lazy initialization of Specific Beans
- The specifically marked singleton beans will be initialized lazily.

**1. XML based configuration :**

In XML-based configuration, we can set the `lazy-init` attribute of the `<bean>` element to "true" to enable lazy initialization.

Example:
```xml
<bean id="lazyInitializedService" class="com.example.LazyInitializedService" lazy-init="true" />
```

**2. Annotation based configuration :**

In Annotation based configuration `@Lazy` annotation can be used to mark specific beans to be created lazily.

Example:
```java
@Service
@Lazy
public class LazyInitializedService {
// Bean implementation
}
```

**3. Java based configuration:**

In Java configuration (`@Configuration`), `@Lazy` annotation can be applied directly to the `@Bean` methods.

Example:
```java
@Configuration
public class AppConfig {

    @Bean
    @Lazy
    public LazyInitializedService lazyInitializedService() {
        return new LazyInitializedService();
    }
}
```

### Lazy initialization of Entire configuration
- All the singleton beans configured in the specified configuration, will be initialized lazily. The beans defined outside
 the configuration will not be affected.

**1. XML based configuration :**

In XML-based configuration, the `default-lazy-init` attribute of the `<beans>` element to can be set "true" to enable lazy initialization globally.

Example:
```xml
<beans default-lazy-init="true">
   <!-- no singleton beans will be pre-instantiated... -->
   <bean id="lazyInitializedService" class="com.example.LazyInitializedService"/>
</beans>
```

**2. Java based configuration:**

In Java configuration, `@Lazy` annotation can be applied directly to the `@Configuration` class to 
enable lazy initialization globally.

Example:
```java
@Configuration
@Lazy
public class AppConfig {

    // no singleton beans will be pre-instantiated...
    @Bean
    public LazyInitializedService lazyInitializedService() {
        return new LazyInitializedService();
    }
}
```

>**Note:**<br>
> When a lazy-initialized bean is a dependency of a singleton bean that is not lazy-initialized, the ApplicationContext creates the 
> lazy-initialized bean at startup only, because it must satisfy the singleton’s dependencies at the time of instantiation itself.<br>
> So, the containing bean must also be marked for lazy-initialization if the same is expected from the dependency.

## Demo

[_LazyBean.java_](./LazyBean.java)
```java
//@Lazy //uncomment to enable lazy initialization
@Component
public class LazyBean {

    @PostConstruct
    public void onInit() {
        System.out.println("LazyBean created!!");
    }

    public void doNothing() {}
}
```
[_App.java_](./App.java)
```java
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
```
Output: (Without Lazy initialization)
```shell
LazyBean created!!
ApplicationContext initialized!!
```
Output: (With Lazy initialization enabled)
```shell
ApplicationContext initialized!!
LazyBean created!!
```

**Summary**<br>
Lazy initialization in Spring is a powerful feature that allows beans to be created only when needed, 
resulting in improved application startup performance and reduced resource consumption.<br>
Although, this Spring configuration issues will not be discovered at startup.
