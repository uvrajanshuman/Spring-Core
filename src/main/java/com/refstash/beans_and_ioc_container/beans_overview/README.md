# Bean Overview

The objects that the Spring IOC container manages are called Spring Beans. The beans are created through the configuration metadata
supplied to the container (ex: in form of XML `<beans>` definitions, or through `@Bean` annotated methods in `@Configuration` annotated class)

The container translates the configuration metadata, and internally represents the bean definitions as `BeanDefinition` objects.<br>
The `BeanDefinition` objects contain the following metadata:

1.  Package-qualified class name: The actual implementation class of the bean being defined.
    It tells the container which Java class to instantiate when creating the bean.

2. Bean Behavioral Configuration: This includes various settings that define how the bean should behave in the container. (like: scope, lifecycle callbacks etc).

3. Dependencies: References to other beans that are needed for the bean to do its work.
   The container ensures that the required beans are available and injected into the dependent bean.

4. Other Configuration Settings: Apart from class and dependencies, there might be other settings specific to the bean.
   For instance, a connection pool; bean may have settings for the pool size or number of connections to use.

These information in the bean definition translates to a set of properties that define each bean.<br>
These properties include:

| Property                 | Description                                                                                          |
|--------------------------|------------------------------------------------------------------------------------------------------|
| Class                    | The class name of the bean.                                                                          |
| Name                     | A unique name given to the bean for identification.                                                  |
| Scope                    | The scope of the bean, such as singleton, prototype, etc.                                            | 
| Constructor Arguments    | Dependencies that are passed to the constructor when creating the bean.                              |
| Properties               | Additional dependencies or configuration settings set on the bean after its creation.                |
| Autowiring Mode          | A way to automatically inject dependencies based on type or name matching.                           |
| Lazy Initialization Mode | Specifies whether the bean should be created only when needed (lazy) or right at the start (eager).  |
| Initialization Method    | A method that will be called after the bean is created to perform initialization tasks.              |
| Destruction Method       | A method that will be called before the bean is removed from the container to perform cleanup tasks. |

Each bean in the container is defined by these properties, and the IoC container uses this information to manage the beans' creation, lifecycle, and dependencies throughout the application.

## Registering existing objets as Spring Beans

The bean definitions are generally provided through XML or Java configuration files. However, the `ApplicationContext` also allows the 
registration of existing objects that are created outside the container by users.

This can be done by accessing the `ApplicationContext`'s `BeanFactory` using the `getAutowireCapableBeanFactory()` method, which returns `AutowireCapableBeanFactory` implementation.<br>
The `DefaultListableBeanFactory` is the child interface of `AutowireCapableBeanFactory`, and supports the registration of singleton instances through the `registerSingleton(..)` method and 
the registration of bean definitions through the `registerBeanDefinition(..)` method.

```java
//imports

public class ExistingSingleton {
    private final String message;

    public ExistingSingleton(String message) {
        this.message = message;
    }

    public void displayMessage() {
        System.out.println("Message from ExistingSingleton: " + message);
    }
}

@Configuration
class App{
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);

        //object created outside the container (manually created)
        ExistingSingleton existingSingleton = new ExistingSingleton("manually created singleton");
        //registering an existing singleton class
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        //listableBeanFactory.registerSingleton("beanName",beanInstance);
        listableBeanFactory.registerSingleton("existingSingleton",existingSingleton);

        applicationContext.getBean(ExistingSingleton.class).displayMessage();
    }

}
```
Output:
```shell
Message from ExistingSingleton: manually created singleton
```
**[Complete working code](./ExistingSingleton.java)**

> Registering Singleton means that there should be only one instance of that bean in the container. Registration of Prototype
> scoped bean in such a way is not allowed.

Manually registering beans at runtime should be done early in the application's lifecycle to ensure proper handling by Spring, 
during introspection steps like autowiring and dependency injection.

Best practice is to define and register most beans through regular bean definition metadata during the application's startup phase for a stable and predictable environment.