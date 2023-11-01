# BeanFactoryAware

The implementing Beans becomes aware of the `BeanFactory` that created them.

When a bean implements the `BeanFactoryAware` interface, Spring automatically detects it during the bean creation
process.
After creating the bean instance, Spring calls the `setBeanFactory()` method on that bean, passing the reference to
the `BeanFactory`.
The bean can then use this reference to interact with the Spring container and access other beans or perform additional
configuration.

## Implementing BeanFactoryAware

_BeanFactoryAware_
```java
public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
```

The `setBeanFactory()` method is automatically called by Spring, passing the `BeanFactory` reference.

## Obtaining BeanFactory through BeanFactoryAware interface

_Implementing BeanFactoryAware_
```java
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class BeanFactoryAwareBean implements BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    // ...
}
```

## Obtaining BeanFactory through Autowiring

Another way of obtaining the reference of `BeanFactory` would be through **Autowiring**.

A more concise approach to obtain the `BeanFactory` is by directly injecting it using `@Autowired`.

_Autowiring BeanFactory_

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

@Component
public class BeanFactoryAwareBean {
    @Autowired
    private BeanFactory beanFactory;

    // ...
}
```

> A bean can become aware of `BeanFactory` either by implementing the `BeanFactoryAware` interface or
> by directly autowiring the `BeanFactory` into it.

## Usages:

`BeanFactoryAware` can be useful in several scenarios:

### Accessing other beans (the programmatic retrieval of other beans) :

Similar to `ApplicationContext` aware bean, the `BeanFactory` aware bean can also retrieve other beans from the
container
and use them for its own purposes.
Sometimes this capacity is useful but in general it should be avoided as it couples the code to the Spring;
and violates the IOC style where the dependencies are injected by the framework itself.

Example: Retrieving prototype bean as a dependency of a singleton bean.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

@Component
public class MySingletonBean {
    private final MyPrototypeBean prototypeBean;

    @Autowired
    private BeanFactory beanFactory;

    public MySingletonBean() {
        // The prototypeBean will be different each time getBean() is called
        this.prototypeBean = beanFactory.getBean(MyPrototypeBean.class);
    }

    // ...
}
```

### Custom bean instantiation logic:

In some advanced scenarios, custom bean instantiation logic might be required.
Like a bean might need to be created programmatically with specific configurations before being registered with the
container.
In such cases, `BeanFactoryAware` can provide the flexibility to implement custom instantiation and configuration
logic.

Similar to: [ApplicationContextAware custom bean instantiation logic](../applicationContextAware/README.md#custom-bean-instantiation-logic)

## Conclusion

In conclusion, while `BeanFactoryAware` can be a powerful tool in certain situations, it should be used judiciously
to avoid tight coupling with framework, and to adhere to the principles of dependency injection. 

