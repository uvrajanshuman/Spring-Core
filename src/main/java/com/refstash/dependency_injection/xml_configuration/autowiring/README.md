# Autowiring in XML based configuration

Feature of Spring framework by which the spring container injects the dependencies automatically.<br>
Autowiring can be done either using xml or using annotation.

## Autowiring Modes

| Mode        | Description                                                                                                                                                                                                            |
|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| no          | Default value.<br/>By default, autowiring is disabled. Explicit bean reference should be used for wiring.                                                                                                              |
| byName      | Autowiring by property name.<br/> Spring container looks for bean with the same name as the property that needs to be autowired.                                                                                       |
| byType      | Autowiring by property datatype.<br/> Spring container looks for bean of specified property type.<br> But, if two or more bean of matching type exists in config file, it results into ambiguity and throws exception. |
| constructor | Analogous to byType, but injected using constructor. If there is not exactly one bean of the constructor argument type in the container, ambiguity arises leading to exception.                                        |

> `byName` and `byType` modes uses setter injection.

### Limitations:
- The dependencies can still be defined using `<constructor-arg>` and `<property>`, and it will always override autowiring.
- Autowiring can not inject primitive type and string type values.
- It works with Reference types only. 

## Example demonstrating Autowiring through XML configuration

[_SortAlgorith.java_](./SortAlgorithm.java)
```java
public interface SortAlgorithm {
	void sort(int[] arr);
}
```

[_BubbleSort.java_](./BubbleSort.java)
```java
/*
 * Implementation of SortAlgorithm: BubbleSort
 */

public class BubbleSort implements SortAlgorithm {
    @Override
    public void sort(int[] arr) {
        //BubbleSort implementation
    }
}
```

[_HeapSort.java_](./HeapSort.java)
```java
/*
 * Implementation of SortAlgorithm: HeapSort
 */

public class HeapSort implements SortAlgorithm {

    @Override
    public void sort(int[] arr) {
        //HeapSort implementation
    }
}
```

### Autowiring byName

The `byName` mode injects the object dependency according to name of the bean. <br>
In such case, the **property name** and the desired **bean name** must be same. It internally calls setter method.

[_BinarySearch.java_](./BinarySearch.java)
```java
/*
 * BinarySearch Implementation
 * depends on a SortAlgorithm
 */

public class BinarySearch {
    // Dependency of BinarySearch
    // Property name matches the desired dependency
    private SortAlgorithm bubbleSort;

    public SortAlgorithm getBubbleSort() {
        return bubbleSort;
    }

    public void setBubbleSort(SortAlgorithm bubbleSort) {
        this.bubbleSort = bubbleSort;
    }

    int search(int arr[], int x) {
        //BinarySearch implementation
    }
}
```

[_config.xml_](../../../../../../../main/resources/com/refstash/dependency_injection/xml_configuration/autowiring/config.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd">

    <!--bean declarations-->
    <bean id="bubbleSort" 
          class="com.refstash.dependency_injection.xml_configuration.autowiring.BubbleSort"/>

    <bean id="heapSort" 
          class="com.refstash.dependency_injection.xml_configuration.autowiring.HeapSort"/>

    <bean id="binarySearch"
          class="com.refstash.dependency_injection.xml_configuration.autowiring.BinarySearch"
          autowire="byName"/>
</beans>
```

Herein, the property name **bubbleSort** of BinarySearch class matches a bean identifier, so it will automatically get autowired.

### Autowiring byType

The **byType** mode injects the object dependency according to type. <br>
So property name and bean name can be different. It internally calls the setter method.

In case two or more beans of matching type exists in the config file, then it causes ambiguity and leads to exception.

[_BinarySearch.java_](./BinarySearch.java)
```java
/*
 * BinarySearch Implementation
 * depends on a SortAlgorithm
 */

public class BinarySearch {
    // Dependency of BinarySearch
    // Dependency injected by Spring
    private SortAlgorithm sortAlgorithm;

    // Autowiring byName
    public void setBubbleSort(SortAlgorithm bubbleSort) {
        this.sortAlgorithm = bubbleSort;
    }
    
    public SortAlgorithm getBubbleSort() {
        return sortAlgorithm;
    }
    
    int search(int arr[], int x) {
        //BinarySearch implementation
    }
}
```

[_config.xml_](../../../../../../../main/resources/com/refstash/dependency_injection/xml_configuration/autowiring/config.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd">

    <!--bean declarations-->
    <bean id="bubbleSort" 
          class="com.refstash.dependency_injection.xml_configuration.autowiring.BubbleSort"/>

    <!-- intentionally commented to avoid ambiguity
    <bean id="heapSort" 
          class="com.refstash.dependency_injection.xml_configuration.autowiring.HeapSort"/>
          -->

    <bean id="binarySearch"
          class="com.refstash.dependency_injection.xml_configuration.autowiring.BinarySearch"
          autowire="byType"/>
</beans>
```
>**Note:**<br>
> In case the dependency of a bean can be resolved by more than one available bean, it leads to ambiguity and thus is not allowed.<br>
> Herein, **BinarySearch** bean's dependency **SortAlgorithm** can be resolved by two available beans **BubbleSort** and **HeapSort**;
> this leads to ambiguity and thus spring throws following exception:
>  ```shell
>  Caused by: org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of 
>  type 'com.refstash.dependency_injection.xml_configuration.autowiring.SortAlgorithm' available: 
>  expected single matching bean but found 2: bubbleSort,heapSort
>  ```
>To avoid this ambiguity **heapSort** bean declaration has been intentionally commented

### Autowiring by constructor:
The `constructor` mode injects the dependency by calling the constructor of the class.<br>
In case there are multiple constructors Spring will choose the constructor with most number of arguments that can be satisfied with the available beans.

Spring will try to find beans that match the constructor parameter types and inject them into the constructor at the time of bean instantiation.

[_BinarySearch.java_](./BinarySearch.java)
```java
/*
 * BinarySearch Implementation
 * depends on a SortAlgorithm
 */

public class BinarySearch {
    // Dependency of BinarySearch
    // Dependency injected by Spring
    private SortAlgorithm sortAlgorithm;

    public BinarySearch() {
        System.out.println("no arg constructor");
    }

    //Autowiring by constructor
    public BinarySearch(SortAlgorithm sortAlgorithm) {
        this.sortAlgorithm = sortAlgorithm;
    }

    int search(int arr[], int x) {
        //BinarySearch implementation
    }
}
```

[_config.xml_](../../../../../../../main/resources/com/refstash/dependency_injection/xml_configuration/autowiring/config.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd">

    <!--bean declarations-->
    <bean id="bubbleSort" 
          class="com.refstash.dependency_injection.xml_configuration.autowiring.BubbleSort"/>

    <!-- intentionally commented to avoid ambiguity
    <bean id="heapSort" 
          class="com.refstash.dependency_injection.xml_configuration.autowiring.HeapSort"/>
          -->

    <bean id="binarySearch"
          class="com.refstash.dependency_injection.xml_configuration.autowiring.BinarySearch"
          autowire="constructor"/>
</beans>
```

The wiring will happen through the one arg constructor of **BinarySearch**.

>**Note:**<br>
>Herein, two beans of type **SortAlgorithm** are present which will lead to ambiguity and thus spring will throw following exception:
>  ```shell
>  Caused by: org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of 
>  type 'com.refstash.dependency_injection.xml_configuration.autowiring.SortAlgorithm' available: 
>  expected single matching bean but found 2: bubbleSort,heapSort
>  ```
>To avoid this ambiguity **heapSort** bean has been intentionally commented.

[_App.java_](./App.java)
```java
//imports
class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("com/refstash/dependency_injection/xml_configuration/autowiring/config.xml");

        BinarySearch binarySearch = applicationContext.getBean("binarySearch", BinarySearch.class);

        int[] arr = {2, 3, 4, 10, 40};
        int result = binarySearch.search(arr, 40);
        System.out.println(result != -1 ? "Element found" : "Element not found");
    }
}
```
Output:
```shell
Element found
```

>**Note:**<br>
>XML-based autowiring does not provide built-in support for qualifiers.<br>
>If there are multiple beans of the same type, and you want to specify which one should be injected,
you need to resort to additional configuration through `@Qualifier` annotation.

## Excluding a bean from Autowiring

A specific bean can be excluded from Autowiring, by setting the `autowire-candidate` attribute of the `<bean/>` element to **false**.

Ex:
```xml
<bean id="beanId" class="package.className" autowire-candidate="false">
  <!-- injecting dependencies of the bean -->
</bean>
```
- The container will make such beans unavailable for autowiring (including annotation based autowiring using `@Autowired`).

> The `autowire-candidate` attribute is meant to affect only type-based autowiring.<br>
> It does not affect explicit references by name even though the bean is marked as non-autowiring candidate.
> Thus, Autowiring `byName` still works and injects the bean if the name matches with the target property.

The beans defined in an XML configuration file can also be restricted for autowiring based on some pattern matching.<br>
The top-level `<beans/>` element accepts one or more patterns within its `default-autowire-candidates` attribute.

Example: To limit autowire candidate status of any bean whose name ends with Repository, provide a value of `*Repository`.<br>
To provide multiple patterns, define them in a comma-separated list.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd"
       default-autowire="byName" default-autowire-candidates="*Repository">

    <!-- bean declarations -->
  
</beans>
```
  - `default-autowire="byName"` specifies the default autowire mode of the contained bean.
  - `default-autowire-candidates="*Repository"` specifies only beans names ending with Repository will be available for autowiring.
  - An explicit value of true or false for a specific bean definition’s `autowire-candidate` attribute always takes precedence. 
    For such beans, the pattern matching rules do not apply.

These techniques are useful to restrict a bean from being autowired. 
But, this does not mean that an excluded bean cannot itself be configured by using autowiring. Rather, the bean itself is not a candidate for autowiring in other beans.
