# BeanPostProcessor

`BeanPostProcessor` is an essential interface that allows custom modification of beans during their 
instantiation and initialization. 

Implementing this interface enables you to intervene in the bean creation lifecycle and customize the behavior of beans
before and after they are fully initialized.

Post-processing logics are generally those logics that needs to be executed after the bean class is instantiated and the dependencies are injected.
All the bean specific post-processing logics are handled through the bean initialization callbacks 
(like InitializingBean's afterPropertiesSet or a custom init-method).

BeanPostProcessor is meant for the generic post-processing logics that needs to be applied to all the beans or a large number of beans. 
Instead of writing such logics in all the concerned bean's initialization callback methods, it can directly be provided through BeanPostProcessor.


## Understanding BeanPostProcessor

```java
public interface BeanPostProcessor {
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}
```

The `BeanPostProcessor` interface provides two default methods that can be overridden:

1. **`postProcessBeforeInitialization(Object bean, String beanName)`**:
   This method is called before any bean initialization callbacks(like InitializingBean's afterPropertiesSet or a custom init-method).
   Used to perform any necessary custom processing on the bean before its initialization.

2. **`postProcessAfterInitialization(Object bean, String beanName)`**:
   This method is called after any bean initialization callbacks (like InitializingBean's afterPropertiesSet or a custom init-method).
   Used to perform additional custom processing on the bean after it has been fully initialized.


## Implementing BeanPostProcessor

To create a custom `BeanPostProcessor`, you can implement the `BeanPostProcessor` interface and override its methods. And the
implementing class needs to be registered as spring bean using any of the valid method (like: using `@Bean` or `@Component` annotation
or through xml declaration).

```java
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class CustomBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // Perform custom processing before initialization and return the bean 
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // Perform custom processing after initialization and return the bean
        return bean;
    }
}
```

## Usages:

The `BeanPostProcessor` is a powerful tool that can be utilized for various purposes, such as:

1. **Custom Initialization Logic:**\
   Custom initialization logics can be applied to the beans without modifying their source code. 
   For example, you can initialize certain properties, set default values, or perform data conversions during the bean initialization phase.

2. **Proxy Creation:**\
   `BeanPostProcessor` can be used to create proxies around specific beans. 
    This is particularly useful for adding cross-cutting concerns such as logging, transaction management, security checks, or performance monitoring.

3. **Conditional Bean Creation:**\
   You can conditionally create beans based on certain conditions or configurations, which may involve creating alternate implementations or variations of beans.

4. **Resource Management:**\
   You can use `BeanPostProcessor` to manage resources associated with beans, such as opening or closing connections, streams, or sessions.

5. **Custom Annotations:**\
   `BeanPostProcessors` can be used to handle custom annotations. 
   For example, custom annotations can be created and `BeanPostProcessor` can be used to process beans annotated with those annotations.

**Internal Usage by Spring Framework:**

Internally, Spring Framework itself uses various `BeanPostProcessors` for its core functionalities:

1. **Autowiring Support:** Spring's `AutowiredAnnotationBeanPostProcessor` is responsible for handling the `@Autowired` annotation and injecting dependencies into beans.

2. **Required Fields Check:** The `RequiredAnnotationBeanPostProcessor` checks whether the fields annotated with `@Required` have been properly set during bean initialization.

3. **Bean Validation:** The `InitDestroyAnnotationBeanPostProcessor` handles `@PostConstruct` and `@PreDestroy` annotations, enabling initialization and destruction callbacks for beans.

4. **Property Placeholder Resolution:** The `PropertyPlaceholderConfigurer` is a `BeanPostProcessor` responsible for resolving placeholders in bean properties, using values from property files or other sources.

5. **Bean Scopes:** The `ScopeMetadataResolver` and `ScopedProxyBeanPostProcessor` work together to handle bean scopes, such as singleton, prototype, and custom scopes.

6. **AspectJ Support:** Spring's `AspectJAwareAdvisorAutoProxyCreator` is a `BeanPostProcessor` that enables support for AspectJ-based aspects and creates proxies for advised beans.

These are just a few examples of how Spring Framework leverages `BeanPostProcessors` internally to provide its core functionalities.


## Example : Custom Logging and Monitoring: 
- BeanPostProcessors are often used for logging and monitoring purposes. 
- A custom BeanPostProcessor can be used to log method invocations, measure execution times, or capture error conditions for specific beans in your application.

_MonitoredBean_ : [MonitoredBean.java](MonitoredBean.java)
- The implementation classes of this interface are supposed to be monitored and logged.
```java
public interface MonitoredBean {
    void doSomething();
}
```

_SampleMonitoredBean_ : [SampleMonitoredBean.java](SampleMonitoredBean.java)
- The implementation class that is supposed to be logged.
```java
//imports

@Component
public class SampleMonitoredBean implements MonitoredBean{
    @Override
    public void doSomething() {
        System.out.println("SampleMonitoredBean: doSomething");
    }
}
```

_LoggingBeanPostProcessor_ : [LoggingBeanPostProcessor.java](LoggingBeanPostProcessor.java)
- This BeanPostProcessor implementation is supposed to provide a _Dynamic Proxy Object_ for MonitoredBean implementations.
- And the proxy will be responsible for intercepting the actual method calls on those implementation instances and log them.
```java
//imports

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

    // InvocationHandler logging the method calls
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
```
- With this dynamic proxy in place, when any method of the _MonitoredBean_ interface is called on the _SampleMonitoredBean_ instance, 
the invoke method of the InvocationHandler will be triggered. 
The invoke method will log the method name and then forward the method call to the original _SampleMonitoredBean_ object.

_App (Driver class)_ : [App.java](App.java)
```java
//imports

@Configuration
@ComponentScan
public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        MonitoredBean monitoredBean = applicationContext.getBean(MonitoredBean.class);
        monitoredBean.doSomething();
    }
}
```
Output:
```shell
MonitoredBean before : doSomething
SampleMonitoredBean: doSomething
MonitoredBean after : doSomething
```
 
In real world applications, a more sophisticated logging mechanisms or dedicated monitoring tools are required. And **Spring AOP** is preferred
over this approach.

## Conclusion
The `BeanPostProcessor` interface provides a mechanism to customize bean initialization and allows to perform various tasks during the bean lifecycle. 
By implementing this interface, you can dynamically modify beans, add extra functionality, and decouple cross-cutting concerns from your application logic.

However, since `BeanPostProcessor` operates on all beans, it should be used more judiciously considering its impact on the entire application context. 
