# Aware interfaces

The Aware interfaces are a set of marker interfaces that components can implement to gain awareness about certain runtime contexts 
or environment settings. 
They allow to hook into the inner workings of the Framework.

The Spring beans might require access to framework objects, such as `ApplicationContext`, `BeanFactory`, `ResourceLoader` etc.
To gain such accesses, these Aware interfaces can be implemented.

By implementing Aware interfaces, components can receive specific callbacks and obtain relevant resources (like `ApplicationContext`, `BeanFactory` etc.) from the container during initialization.

There are various Aware interfaces available in Spring, each focusing on a different aspect of the application context or environment. <br>
Some common ones are:

1. **ApplicationContextAware**: Components implementing this interface gain access to the ApplicationContext, allowing them to interact with the container, retrieve other beans, and access application-related resources.<br>
    Read more: [_ApplicationContextAware_](./applicationContextAware/README.md)

2. **BeanFactoryAware**: Components implementing this interface receive the BeanFactory instance, providing access to bean creation and configuration mechanisms.<br>
    Read more: [_BeanFactoryAware_](./beanFactoryAware/README.md)

3. **BeanNameAware**: Components implementing this interface become aware about their bean name.<br>
    Read more: [_BeanNameAware_](./beanNameAware/README.md)
4. **EnvironmentAware**: This interface enables components to access the application's environment configuration, such as properties and profiles.

5. **MessageSourceAware**: Components implementing this interface can access the message source for internationalization purposes.

6. **ResourceLoaderAware**: Components implementing this interface can obtain resource loading capabilities for accessing application resources like files.

7. **ApplicationEventPublisherAware**: Components implementing this interface can publish application events programmatically.


>The Aware interface methods are called after populating the bean properties and just before pre initialization with BeanPostProcessor.

