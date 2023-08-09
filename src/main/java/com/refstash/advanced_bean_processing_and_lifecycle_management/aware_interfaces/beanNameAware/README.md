# BeanNameAware

The implementing Bean becomes aware of their own bean names.

The `BeanNameAware` interface provides a way for beans to be aware of their own bean names within the ApplicationContext. 
When a bean implements the `BeanNameAware` interface, Spring automatically detects it during the bean initialization process. 
After creating the bean instance, Spring calls the `setBeanName()` method on that bean, passing the name of the bean as an argument. 
The bean can then utilize this bean name for various purposes within its own logic.

## Implementing BeanNameAware

_BeanNameAware_
```java
public interface BeanNameAware {
    void setBeanName(String name);
}
```
The `setBeanName()` method is automatically called by Spring, providing the bean's name as the argument.

## Obtaining Bean Name through BeanNameAware interface

_Implementing BeanNameAware_
```java
import org.springframework.beans.factory.BeanNameAware;

public class BeanNameAwareBean implements BeanNameAware {
    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    // ...
}
```

## Usage:

`BeanNameAware` can be beneficial in various scenarios:

1. **Self-Identification:**\
   The most straightforward use of the bean name is for the bean to identify itself within its own logic. 
   The bean can use the `beanName` field to reference itself or include its name in logging or custom messages.

2. **Integration with External Systems:**\
   In some cases, external systems may require the bean name as part of their configuration or communication protocols. 
   By being aware of its own name, the bean can provide this information when interacting with external systems.

3. **Custom Initialization based on Bean Name:**\
   A bean may require custom initialization or configuration based on its name. For example, if there are multiple instances of the same bean with different names, 
   each instance can customize its behavior based on its unique name.

## Conclusion:
In conclusion, the BeanNameAware interface provides a simple yet valuable tool for beans to know their own names within the application context. By using BeanNameAware, beans can better understand their context and perform specialized operations based on their names. However, like other awareness interfaces, it should be used with care to avoid introducing unnecessary dependencies and to maintain clean separation of concerns in the application.

