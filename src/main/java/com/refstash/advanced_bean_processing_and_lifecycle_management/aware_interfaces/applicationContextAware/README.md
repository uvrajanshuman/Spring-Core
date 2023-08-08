# ApplicationContextAware

The implementing Bean becomes aware of the `ApplicationContext` that created them.

During the instantiation of a Bean that implements the `ApplicationContextAware` interface,
Spring provides that bean the reference of `ApplicationContext` by calling the implemented `setApplicationContext`
method.
The bean can then use this reference to interact with the Spring container and access other beans or perform additional
configuration.

## Implementing ApplicationContextAware

_ApplicationContextAware_

```java
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
```

The `setApplicationContext()` method is automatically called by Spring, passing the `ApplicationContext` reference.

## Obtaining ApplicationContext through ApplicationContextAware interface

_Implementing ApplicationContextAware_

```java
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextAwareBean implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //...
}
```

## Obtaining ApplicationContext through Autowiring

Another way of obtaining the reference of `ApplicationContext` would be through **Autowiring**.

A more concise approach to obtain the `ApplicationContext` is by directly injecting it using `@Autowired`.

_Autowiring ApplicationContext_

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextAwareBean {
    @Autowired
    private ApplicationContext applicationContext;

    //...
}
```

> A bean can become aware of `ApplicationContext` either by implementing the `ApplicationContextAware` interface or
> by directly autowirng the `ApplicationContext` in it.

## Usage:

This can be useful in several scenarios.

### Accessing other beans (the programmatic retrieval of other beans) :

The `ApplicationContext` aware bean can retrieve other beans from the container and use them for its own purposes.
Sometimes this capacity is useful but in general it should be avoided as it couples the code to the Spring;
and violates the IOC style where the dependencies are injected by the framework itself.

Example: Retrieving prototype bean as a dependency of a singleton bean.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MySingletonBean {
  private final MyPrototypeBean prototypeBean;

  @Autowired
  private ApplicationContext applicationContext;

  public MySingletonBean() {
    // The prototypeBean will be different instance each time getBean() is called
    this.prototypeBean = applicationContext.getBean(MyPrototypeBean.class);
  }

  // ...
}
```

### Publishing application events :

The `ApplicationContext` interface internally implements the `ApplicationEventPublisher`, thus also has capability to
publish application events using the `publishEvent` methods.

Instead of Autowirng the `ApplicationEventPublisher`, the bean can become `ApplicationContextAware` and publish events
directly through the `ApplicationContext` reference. Although, this is not recommended.

Example: Publishing a custom event (MyCustomEvent) through ApplicationContext directly

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MyEventPublisherBean {
    @Autowired
    private ApplicationContext applicationContext;

    public void publishEvent() {
        // Instead of using the ApplicationContext directly, prefer using ApplicationEventPublisher
        applicationContext.publishEvent(new MyCustomEvent(this));
    }

    // ...
}
```

### Accessing Environment Properties :

The implementing bean can access properties from the Spring Environment, which holds properties from various sources
like configuration files, system properties, etc.

### Programmatic Bean Registration :

The `ApplicationContext` implementations like `AnnotationConfigApplicationContext` also permit the registration of
existing objects that are created outside the container (by users).

This is done by accessing the `BeanFactory` through the `getBeanFactory()` method of `ApplicationContext`
implementations(like `AnnotationConfigApplicationContext`), which returns the `ConfigurableListableBeanFactory` implementation.<br>
`ConfigurableListableBeanFactory` supports this registration through the `registerSingleton(..)`
and`registerBeanDefinition(..)` methods.

However, typical applications work solely with beans defined through regular bean definition metadata.

[Programmatic Bean Registration](../../../beans_and_ioc_container/beans_overview/README.md#registering-existing-objets-as-spring-beans)

### Custom bean instantiation logic:

In some advanced scenarios, custom bean instantiation logic might be required.
Like a bean might need to be created programmatically with specific configurations before being registered with the
container.
In such cases, `ApplicationContextAware` can provide the flexibility to implement custom instantiation and
configuration logic.

Example:
Suppose the application needs to send notifications using different service providers (e.g., email, SMS, push notifications),
and the chosen provider is specified in an external properties file.

_NotificationServiceProvider and implementations_ : [NotificationService.java](NotificationServiceProvider.java)

```java
//imports

//An interface representing the various notification service providers (e.g., EmailProvider, SMSProvider, PushNotificationProvider).
public interface NotificationServiceProvider {
    void sendNotification(String recipient, String message);
}

//NotificationServiceProvider implementation for sending notifications via email.
@Component("emailProvider")
class EmailProvider implements NotificationServiceProvider {
    @Override
    public void sendNotification(String recipient, String message) {
        // Send notification via email
        System.out.println("Sending email notification to " + recipient + ": " + message);
    }
}

//NotificationServiceProvider implementation for sending notifications via sms.
@Component("smsProvider")
class SMSProvider implements NotificationServiceProvider {
    @Override
    public void sendNotification(String recipient, String message) {
        // Send notification via SMS
        System.out.println("Sending SMS notification to " + recipient + ": " + message);
    }
}

//NotificationServiceProvider implementation for sending notifications via push-notifications.
@Component("pushNotificationProvider")
class PushNotificationProvider implements NotificationServiceProvider {
    @Override
    public void sendNotification(String recipient, String message) {
        // Send notification via push notification
        System.out.println("Sending push notification to " + recipient + ": " + message);
    }
}
```

_NotificationService_ : [NotificationService.java](NotificationService.java) <br>

- The class responsible for sending notification through the service provider (determined by external properties
  file).

```java
/*
 * The `NotificationService` needs to initialize itself with the appropriate provider based on the configuration.
 * The class that implements `ApplicationContextAware` and dynamically configures its behavior based on external properties.
 */
//imports

@Component
public class NotificationService implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private NotificationServiceProvider notificationProvider;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        init(); // Perform custom initialization after obtaining the BeanFactory
    }

    private void init() {
        // Determine the notification provider based on external configuration
        // Accessing Environment properties
        String notificationProviderName = applicationContext.getEnvironment().getProperty("notification.provider");

        // Lookup the appropriate provider bean based on the configuration
        notificationProvider = applicationContext.getBean(notificationProviderName, NotificationServiceProvider.class);
    }

    public void sendNotification(String recipient, String message) {
        notificationProvider.sendNotification(recipient, message);
    }

    //...

}

```

- The `NotificationService` bean will dynamically configure itself based on the specified `notification.provider`
property in the `application.properties` file.
- The chosen provider will be used to send the notification using the `sendNotification()` method of `NotificationService`.

_application.properties (external properties file)_ : [application.properties](../../../../../../resources/com/refstash/aware_interfaces/applicationContextAware/application.properties)

```properties
   notification.provider=emailProvider
```

_App (Driver class)_ : [App.java](App.java)

```java
//imports
@PropertySource("classpath:com/refstash/aware_interfaces/applicationContextAware/application.properties")
@Configuration
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.refstash.aware_interfaces.applicationContextAware");
        context.refresh();

        NotificationService notificationService = context.getBean(NotificationService.class);
        notificationService.sendNotification("user@example.com", "Hello, this is a notification!");

        context.close();
    }
}
```

Output:

```shell
Sending email notification to user@example.com: Hello, this is a notification!
```

Although, a better approach would be directly injecting the `notification.provider` property value directly into the
constructor or a property of _NotificationService_.<br>
And then deciding the _NotificationServiceProvider_ based on that value.

```java
//imports
@Component
public class NotificationService {
    private final NotificationServiceProvider notificationProvider;

    public NotificationService(@Value("${notification.provider}") String notificationProviderName,
                               EmailProvider emailProvider,
                               SMSProvider smsProvider,
                               PushNotificationProvider pushNotificationProvider) {
        switch (notificationProviderName) {
            case "emailProvider":
                notificationProvider = emailProvider;
                break;
            case "smsProvider":
                notificationProvider = smsProvider;
                break;
            case "pushNotificationProvider":
                notificationProvider = pushNotificationProvider;
                break;
            default:
                throw new IllegalArgumentException("Unsupported notification provider: " + notificationProviderName);
        }
    }

    public void sendNotification(String recipient, String message) {
        notificationProvider.sendNotification(recipient, message);
    }

    //...
}
```

- This alternative approach simplifies the code and eliminates the need for `ApplicationContextAware`.
It leverages Spring's powerful property injection mechanism, making the configuration more concise and easier to
maintain.
- While both approaches are valid, using @Value annotations or @ConfigurationProperties is generally the preferred
way for injecting external properties into beans;
as it aligns with the convention-over-configuration principle in Spring, where Spring tries to provide sensible
defaults and requires minimal configuration. 

## Conclusion

In conclusion, while `ApplicationContextAware` can be a powerful tool in certain situations, it should be used judiciously 
to avoid tight coupling with the framework, and to adhere to the principles of dependency injection. 


