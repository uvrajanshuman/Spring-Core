# Method Injection

In Spring the dependency injection is generally achieved through constructor injection or setter injection.
But spring provides yet another technique of dependency injection called Method injection.

Method injection is a form of dependency injection where dependencies are provided by calling a factory method on the bean itself.<br>
The method injection can be achieved in two ways:
1. Using `@Autowired` annotation
2. Lookup method injection

## Method injection using _@Autowired_ annotation

@Autowired can also be used on arbitrary methods, the specified method arguments would get autowired automatically.

Example:
Let's consider a NotificationService bean that needs to send notifications via different channels such as email and SMS.<br> 
The specific channel to be used for sending notifications is determined dynamically based on user preferences.

_NotificationSender and different implementations (represents different notification channels)_
```java
//imports
public interface NotificationSender {
    void sendNotification(String message);
}

@Component
class EmailNotification implements NotificationSender {
    @Override
    public void sendNotification(String message) {
        System.out.println("Sending email notification: " + message);
    }
}

@Component
class SmsNotification implements NotificationSender {
    @Override
    public void sendNotification(String message) {
        System.out.println("Sending SMS notification: " + message);
    }
}
```
_Service class to send notifications_
```java
@Service
class NotificationService {
    private NotificationSender emailNotification;
    private NotificationSender smsNotification;

    // Method injection
    @Autowired
    public void setEmailNotification(NotificationSender emailNotification) {
        this.emailNotification = emailNotification;
    }

    // Method injection
    @Autowired
    public void setSmsNotification(NotificationSender smsNotification) {
        this.smsNotification = smsNotification;
    }

    public void sendNotification(String message, String userPreference) {
        if ("email".equalsIgnoreCase(userPreference)) {
            emailNotification.sendNotification(message);
        } else if ("sms".equalsIgnoreCase(userPreference)) {
            smsNotification.sendNotification(message);
        } else {
            throw new IllegalArgumentException("Invalid user preference");
        }
    }
}
```
- @Autowired used on factory methods. The method params would be injected automatically. 

_Driver and Configuration class_
```java
@Configuration
@ComponentScan
public class App {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        NotificationService notificationService = applicationContext.getBean(NotificationService.class);

        String message = "Hello, Spring!";
        String userPreference = "email"; // or "sms"
        notificationService.sendNotification(message, userPreference);
    }
}
```
Output:
```shell
Sending email notification: Hello, Spring!
```

In this example, `@Autowired` is used on the `setEmailNotification` and `setSmsNotification` methods. 
When Spring creates the `NotificationService` bean, it automatically identifies the required dependencies and injects them into these methods.

[Complete working code for this approach](./using_autowired)

## Lookup method injection

Lookup method injection, involves declaring a method as _lookup method_ in a bean and letting Spring provide the implementation dynamically at runtime.<br> 
Spring dynamically generates the actual implementation of the lookup method.

This type of method injection is useful when a singleton bean needs to collaborate with a non-singleton (prototype) bean.<br>
Since the container creates the singleton bean only once, it gets only one opportunity to set the properties. 
As a result, the container cannot provide the singleton bean with a new instance of the non-singleton bean every time it is needed.

### Using **ApplicationContext** directly
One solution to this is to forego some inversion of control; and make the bean aware of `ApplicationContext` by implementing the `ApplicationContextAware`
interface (or directly autowiring ApplicationContext) and by using `getBean()` method to request a new instance of dependency whenever needed.

_Prototype scoped bean_
```java
//imports

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeDependency {
    private static int identifier = 0;

    public PrototypeDependency() {
        identifier++;
    }

    public void processData() {
        System.out.println("Processing data in PrototypeDependency " + identifier);
    }
}
```
_Singleton scoped bean with Prototype dependency_
```java
//imports

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SingletonBean {
    private PrototypeDependency prototypeDependency;

    @Autowired
    private ApplicationContext applicationContext;

    public PrototypeDependency getPrototypeDependency() {
        return applicationContext.getBean(PrototypeDependency.class);
    }
}
```
- `getPrototypeDependency()` method provides a new instance of `PrototypeDependency` bean on each invocation.

_Driver and Configuration class_
```java
//imports

@Configuration
@ComponentScan
public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        SingletonBean singletonBean = applicationContext.getBean(SingletonBean.class);
        singletonBean.getPrototypeDependency().processData();
        singletonBean.getPrototypeDependency().processData();
    }
}
```
Output:
```shell
Processing data in PrototypeDependency 1
Processing data in PrototypeDependency 2
```
In this example, the SingletonBean requests a new instance of the PrototypeDependency bean using the getBean method of 
ApplicationContext whenever getPrototypeDependency method is called.

Although this approach is not desirable, because the code is aware of and coupled to the Spring Framework.
The Lookup method injection provide a better approach to handle this cleanly

### Using Lookup method injection 
Lookup method injection provides a cleaner approach to handle the dependency between singleton and prototype beans. 
It involves declaring a method as a lookup method in a bean and letting Spring provide the implementation dynamically at runtime.

**Annotation based implementation :**

The `SingletonBean` from previous example will require following modification:

```java
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public abstract class SingletonBean {
    private PrototypeDependency prototypeDependency;

    @Lookup
    public abstract PrototypeDependency getPrototypeDependency();
}
```
Spring will implement the **Lookup** marked method by dynamically generating a subclass of this bean that overrides this method with somewhat
following implementation:
```java
public PrototypeDependency getPrototypeDependency() {
    return applicationContext.getBean(PrototypeDependency.class);
}
```
This is a better approach as the code become decoupled from the framework.<br>
The specific target bean name can also be provided in the lookup annotation like `@Lookup("prototypeDependency")`. Now, the bean with
specified name will be provided by the framework.

**Equivalent XML based configuration implementation :**

The classes would not be annotated in this case. Instead, will be declared in XML configuration file.

_config.xml_
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- PrototypeDependency bean declaration   -->
    <bean id="prototypeDependency" class="com.refstash.method_injection.lookup_method_injection.PrototypeDependency" scope="prototype" />

    <!-- SingletonBean bean declaration   -->
    <bean id="singletonBean" class="com.refstash.method_injection.lookup_method_injection.SingletonBean" scope="singleton">
        <lookup-method name="getPrototypeDependency" bean="prototypeDependency"/>
    </bean>

</beans>
```
- `ClasspathXmlApplicationContext` will be used insted of `AnnotationConfigApplicationContext` while instantiating the container.

In this example, we declare the `PrototypeDependency` bean as a prototype bean and the `SingletonBean` as a singleton bean. 
We use the `<lookup-method/>` tag to specify the lookup method `getPrototypeDependency`, which will be dynamically implemented by Spring to provide 
the specified target bean.

>**Note:**<br>
>The lookup marked method does not need to be abstract always.
> It can be a concrete implementation as well (the implementation won't matter, as it will be dynamically overriden by Spring)<br>
> If the method is abstract, the dynamically-generated subclass implements the method. Otherwise, the dynamically-generated subclass overrides the concrete method defined in the original class.

[Complete working code for this approach](./lookup_method_injection)
