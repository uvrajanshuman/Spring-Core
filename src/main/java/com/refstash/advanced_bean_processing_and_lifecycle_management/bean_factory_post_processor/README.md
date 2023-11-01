# BeanFactoryPostProcessor in Spring

The `BeanFactoryPostProcessor` interface is a powerful mechanism that allows to customize the Spring `BeanFactory` configuration before the beans are actually created and initialized. 
It provides a way to modify the bean definitions, property values, and other configurations at an early stage of the `ApplicationContext` initialization.


## Understanding BeanFactoryPostProcessor

The `BeanFactoryPostProcessor` interface provides a single method that you need to implement:

```java
public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
```

The `postProcessBeanFactory()` method is called by the Spring container just after the bean definitions are loaded but before any bean is instantiated. 
It receives the `ConfigurableListableBeanFactory`, which allows to manipulate bean definitions before the beans are created.

## Implementing BeanFactoryPostProcessor

To create a custom `BeanFactoryPostProcessor`, you can implement the `BeanFactoryPostProcessor` interface and override the `postProcessBeanFactory()` method.
And the implementing class needs to be registered as spring bean using any of the valid method (like: using `@Bean` or `@Component` annotation
or through xml declaration).

```java
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Custom bean factory post-processing logic goes here
        // You can modify bean definitions, property values, etc.
    }
}
```

## Use Cases and Examples

The `BeanFactoryPostProcessor` provides a wide range of use cases, such as:

1. **Property Override:** You can use this mechanism to override property values defined in external configuration sources.
2. **Custom Annotations:** Applying annotations to classes and altering their bean definitions can be achieved using a `BeanFactoryPostProcessor`.
3. **Conditional Bean Definition:** You can conditionally define beans based on certain runtime conditions.
4. **Integrating with Other Frameworks:** `BeanFactoryPostProcessor` can be utilized to integrate third-party frameworks or libraries into the Spring context.

**Example:**<br>
Here's a simple example demonstrating the usage of `BeanFactoryPostProcessor` to override the property values:

```java
//imports
@Configuration
public class AppConfig {

   @Bean
   public MyBean myBean() {
      return new MyBean();
   }

   //Registering the custom bean factory post processor that overrides property value
   @Bean
   public static CustomBeanDefinitionBeanFactoryPostProcessor customBeanDefinitionBeanFactoryPostProcessor() {
      return new CustomBeanDefinitionBeanFactoryPostProcessor();
   }

   public static void main(String[] args) {
      ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
      MyBean myBean = applicationContext.getBean("myBean", MyBean.class);
      System.out.println(myBean.getValue());
   }
}

class MyBean {
   String value;

   @Value("Hello") //can also be injected from external source
   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }
}

class CustomBeanDefinitionBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

   @Override
   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
      GenericBeanDefinition myBeanDefinition = new GenericBeanDefinition();
      myBeanDefinition.setBeanClass(MyAnotherBean.class);
      myBeanDefinition.getPropertyValues().add("value","Hi Buddy!");
      ((DefaultListableBeanFactory)beanFactory).registerBeanDefinition("myAnotherBean",myBeanDefinition);

   }
}
```
- Herein, the `value` property of `MyBean` is overridden using `CustomBeanFactoryPostProcessor`.
- "Hello" get overridden with "Hello World"

Output:
```shell
Hello World
```

**Example:**<br>
Here's another example demonstrating manual registration of a new `BeanDefinition` with `BeanFactory` using `BeanFactoryPostProcessor`.

```java
@@Configuration
public class AppConfig {

   //Registering the custom bean factory post processor that creates a custom BeanDefinition and registers it with BeanFactory
   @Bean
   public static CustomBeanDefinitionBeanFactoryPostProcessor customBeanDefinitionBeanFactoryPostProcessor() {
      return new CustomBeanDefinitionBeanFactoryPostProcessor();
   }

   public static void main(String[] args) {
      ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
      MyAnotherBean myAnotherBean = applicationContext.getBean("myAnotherBean", MyAnotherBean.class);
      System.out.println(myAnotherBean.getValue());
   }
}

// Class not declared as bean, will be explicitly registered as bean.
class MyAnotherBean {
   String value;

   @Value("Hello")
   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }
}

// BeanFactoryPostProcessor to create custom bean definition for claas MyAnotherBean
class CustomBeanDefinitionBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

   @Override
   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
      GenericBeanDefinition myBeanDefinition = new GenericBeanDefinition();
      myBeanDefinition.setBeanClass(MyAnotherBean.class);
      myBeanDefinition.getPropertyValues().add("value","Hi Buddy!");
      ((DefaultListableBeanFactory)beanFactory).registerBeanDefinition("myAnotherBean",myBeanDefinition);

   }
}

```
Output:
```shell
Hi Buddy!
```

## Core Framework Usage

Spring Framework uses `BeanFactoryPostProcessor` extensively for various core functionalities:

1. **Property Placeholder Resolution:** `PropertyPlaceholderConfigurer` processes property placeholders in bean definitions.
2. **Configuration Metadata Processing:** Spring's internal configuration processing involves multiple `BeanFactoryPostProcessor` implementations.
3. **Custom Scopes:** `CustomScopeConfigurer` registers custom scopes using `BeanFactoryPostProcessor`.
4. **Classpath Scanning:** The component scanning process is facilitated by `ClassPathBeanDefinitionScanner`, a `BeanFactoryPostProcessor` itself.


## Conclusion

The `BeanFactoryPostProcessor` interface provides a powerful extension point in the Spring container's initialization process. 
It allows to customize and modify bean definitions, property values, and other configurations before the beans are instantiated. 
