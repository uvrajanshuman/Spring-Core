# Autowiring in Annotation based configuration

Feature of Spring framework by which the spring container injects the dependencies automatically.<br>
Autowiring can be done either using xml or using annotation.

In annotation-based configuration, autowiring is achieved using the `@Autowired` annotation. 
This annotation allows Spring to automatically wire beans together by detecting the dependencies between them.

To use autowiring through `@Autowired`, the component scan must be enabled.
- [**Enabling Component Scanning**](../README.md#enabling-component-scanning)

It can be used to annotate fields or setter methods or constructors.
And based on this it will be either field injection or setter injection or constructor injection.

## Autowiring modes supported for `@Autowired`


1. **By Type (Default):** This is the default autowiring mode. Spring attempts to find a single bean of the same type as the dependency being autowired.
    If exactly one matching bean is found, it is autowired. If multiple matching beans are found, an exception is thrown.

2. **By Name:** This autowiring mode allows to specify the name of the bean to be autowired.
   It can be used in combination with `@Qualifier` annotations. 
   The `@Qualifier` annotation specifies the name of the bean to be autowired when multiple beans of the same type are present.

## Limitations:
- The dependencies can still be defined using `<constructor-arg>` and `<property>`, and it will always override autowiring.
- Autowiring can not inject primitive and string values.
- It works with References only.

## @Autowired and Optional Dependencies

The default behavior of `@Autowired` is to treat the annotated methods, fields or constructors as required dependencies.
When a bean is being constructed, the `@Autowired` dependencies should be available.
If Spring cannot resolve a corresponding bean for wiring, it will throw an exception.

This behaviour can be changed by setting the `required` attribute in `@Autowired` annotation to `false`.
This will enable the framework to skip a non-satisfiable dependencies by marking it as non-required.

```java
//imports
@Component
public class Dependency{
    //...
}
```

```java
//imports
@Component
public class DependentClass {
    @Autowired(required=false)
    private Dependency dependency;
    
    public DependentClass(){
        System.out.println("In no-arg constructor");
    }

    public DependentClass(Dependency dependency){
        System.out.println("In one-arg constructor");
        this.dependency = dependency;
    }
    
    public void setDependency(Dependency dependency){
        System.out.println("In setter method");
        this.dependency = dependency;
    }
    //...
}
```
The dependency is marked as optional.


>**Note:**<br>
>In a similar fashion specific fields, constructor or methods can be marked non-required/optional for autowiring.
> - A non-required method will not be called at all if any of its dependency (method argument) is not available. <br>
> - A non-required field will not get populated at all in such cases, leaving its default value in place.

So, setting the required attribute to `false` in `@Autowired` marks those properties as optional.
This allows properties to be assigned default values that can be optionally overridden via dependency injection.

Alternatively a dependency can also be marked non-required by wrapping it in `java.util.Optional` or through `@Nullable` annotation.

## @Autowired on field
`@Autowired` can be used on fields for field injections. Spring injects the dependencies to the fields through reflection.
```java
//imports
@Component
public class Dependency{
    //...
}
```

```java
//imports
@Component
public class DependentClass {
    @Autowired
    private Dependency dependency;
    
    public DependentClass(){
        System.out.println("In no-arg constructor");
    }
    
    public void setDependency(Dependency dependency){
        System.out.println("In setter method");
        this.dependency = dependency;
    }
    //...
}
```
Output:
```java
In no-arg constructor
```

## @Autowired on setter methods
`@Autowired` can be used on setter methods for setter injection.

```java
//imports
@Component
public class Dependency{
    //...
}
```

```java
//imports
@Component
public class DependentClass {
    private Dependency dependency;
    
    public DependentClass(){
        System.out.println("In no-arg constructor");
    }

    @Autowired
    public void setDependency(Dependency dependency){
        System.out.println("In setter method");
        this.dependency = dependency;
    }
    //...
}
```
Output:
```java
In setter method
```

## @Autowired on constructor
`@Autowired` can be used on constructor for constructor injection.

> By default `@Autowired` is analogous to `@Autowired(required = true)`

Rules for using `@Autowired` on constructors:
1. **Single Constructor:** If a class has only one constructor, Spring will always use that constructor to create the bean, 
   even if it is not annotated with `@Autowired`. <br>So, `@Autowired` annotation is not necessary on that single constructor of the class.

   But if there are multiple constructors and none of there are `@Autowired` annotated, spring will fall back to the default constructor for 
   bean creation, if the default constructor is not available spring will throw and exception.

2. **Only One Autowired Constructor:** If a class has multiple constructors and more than one of them is annotated with `@Autowired` 
   with the required attribute set to `true` (the default value), Spring won't know which constructor to use for creating instance and autowiring, 
   and thus will throw an exception.<br> 
   So, in such cases, only one constructor can be annotated with `@Autowired` and `required=true` (the default value). 
   The other constructors can either remove `@Autowired` or put `required=false` on them (i.e: `@Autowired` on preferred one and `@Autowired(required=false)` on remaining).

3. **Multiple non-required Autowired Constructors:** If there are multiple constructors that can be autowired, all of them need to set the required attribute to `false`. 
   This allows Spring to consider all those constructors as candidates for autowiring, and it will choose the constructor with the most matching dependencies.
   (i.e: the constructor with most number of arguments that can be satisfied with the available beans).<br>
   And, if no such constructor can be satisfied, Spring will fall back to default constructor for instance creation; if the default constructor is also not present, Spring will throw an exception.


```java
//imports
@Component
public class Dependency{
    //...
}
```
**Single Constructor (Autowired not mandatory)**
```java
//imports
@Component
public class DependentClass {
    private Dependency dependency;

    //@Autowired 
    public DependentClass(Dependency dependency){
        System.out.println("In one-arg constructor");
        this.dependency = dependency;
    }
    
    public void setDependency(Dependency dependency){
        System.out.println("In setter method");
        this.dependency = dependency;
    }
    //...
}
```
Output:
```java
In one-arg constructor
```
- `@Autowired` annotation on such a constructor is not necessary if the target bean defines only one constructor to begin with. 
  However, if several constructors are available and there is no primary/default constructor, at least one of the constructors 
  must be annotated with `@Autowired` in order to instruct the container which one to use.

**Multiple Constructors**
```java
//imports
@Component
public class DependentClass {
    private Dependency dependency;
    
    public DependentClass(){
        System.out.println("In no-arg constructor");
    }

    @Autowired
    public DependentClass(Dependency dependency){
        System.out.println("In one-arg constructor");
        this.dependency = dependency;
    }
    
    public void setDependency(Dependency dependency){
        System.out.println("In setter method");
        this.dependency = dependency;
    }
    //...
}
```
Output:
```java
In one-arg constructor
```

## @Autowired on arbitrary method
`@Autowired` can also be used on arbitrary methods, the specified method arguments would get autowired.
```java
//imports
@Component
public class Dependency{
    //...
}
```

```java
//imports
@Component
public class DependentClass {
   private Dependency dependency;

   @Autowired
   public void injectDependency(Dependency dependency) {
      System.out.println("In injectDependency()");
      this.dependency = dependency;
   }
   //...
}
```
Output:
```java
In injectDependency()
```

## @Autowired on arrays/collections

`@Autowired` can be used to inject multiple available beans of a specific type.
This can be achieved by using arrays, collections, or maps to collect those injected beans.

_NotificationService and implementations_
```java
//imports

public interface NotificationService {
   void sendNotification(String message);
}

@Component
public class EmailNotificationService implements NotificationService {
   @Override
   public void sendNotification(String message) {
      System.out.println("Sending email notification: " + message);
   }
}

@Component
public class SMSNotificationService implements NotificationService {
   @Override
   public void sendNotification(String message) {
      System.out.println("Sending SMS notification: " + message);
   }
}

@Component
public class PostalLetterNotificationService implements NotificationService {
    @Override
    public void sendNotification(String message) {
        System.out.println("Sending Postal Letter notification: " + message);
    }
}
```
_NotificationProcessor (accepts all NotificationService as dependency)_
```java
//imports
@Component
public class NotificationProcessor {
    
   //collecting all the NotificationService implementations in array
   private NotificationService[] notificationServices;

   @Autowired
   public void setNotificationServices(NotificationService[] notificationServices) {
      this.notificationServices = notificationServices;
   }

   public void processNotifications(String message) {
      for (NotificationService service : notificationServices) {
         service.sendNotification(message);
      }
   }
}

```
or
```java
//imports
@Component
public class NotificationProcessor {

   //collecting all the NotificationService implementations in a Set
   @Autowired
   private Set<NotificationService> notificationServices;

   public void processNotifications(String message) {
      for (NotificationService service : notificationServices) {
         service.sendNotification(message);
      }
   }
}
```
- The target beans can implement the `org.springframework.core.Ordered` interface or use the `@Order` annotation,
if the array or list should be sorted in a specific order. Otherwise, the default order is the registration order of
the corresponding target bean definitions in the container.

Even typed Map instances can be autowired as long as the expected key type is String. 
The map values contain all beans of the expected type, and the keys contain the corresponding bean names, as the following example shows:
```java
@Component
public class NotificationProcessor {

   //collecting all the NotificationService implementations in a Map
   private Map<String, NotificationService> notificationServices;

    @Autowired
    public void setNotificationServices(Map<String, NotificationService> notificationServices) {
        this.notificationServices = notificationServices;
    }

    public void processNotifications(String message) {
        for (NotificationService service : notificationServices.values()) {
            service.sendNotification(message);
        }
    }
}
```
> **Note:**<br>
> By default, autowiring fails when no matching candidate beans are available for a given injection point.<br> 
> In the case of a declared array, collection, or map, at least one matching element is expected.

## Autowiring ambiguity

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

//imports
@Component
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

//imports
//@Component //intentionally commented to avoid ambiguity
public class HeapSort implements SortAlgorithm {

    @Override
    public void sort(int[] arr) {
        //HeapSort implementation
    }
}
```
- `@Component` annotation intentionally commented to avoid ambiguity

[_BinarSearch.java_](./BinarySearch.java)
```java
/*
 * BinarySearch Implementation,
 * depends on: SortAlgorithm
 */

//imports
@Component
public class BinarySearch {
    // Dependency of BinarySearch
    // Dependency injected by Spring
    @Autowired
    private SortAlgorithm sortAlgorithm;

    int search(int arr[], int x) {
        //Binary search implementation
    }
}
```

[_AppConfig.java_](./AppConfig.java)
```java
//imports
@Configuration
@ComponentScan
public class AppConfig {
}
```

[_App.java_](./App.java)
```java
class App {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

		BinarySearch binarySearch = applicationContext.getBean("binarySearch", BinarySearch.class);

		int[] arr = { 2, 3, 4, 10, 40 };
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
>In case the dependency of a bean can be resolved by more than one available bean, it leads to ambiguity and thus is not allowed.<br>
>Herein, **BinarySearch** bean's dependency **SortAlgorithm** can be resolved by two available beans **BubbleSort** and **HeapSort**;
>this leads to ambiguity and thus spring throws following exception:
>```shell
>Caused by: org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of 
>type 'com.refstash.dependency_injection.annotation_configuration.autowiring.SortAlgorithm' available: 
>expected single matching bean but found 2: bubbleSort,heapSort
>```
>If only one of these two classes are marked `@Component`, autowiring happens without any hassle.<br>
>To avoid this ambiguity, `@Component` annotation is intentionally commented in HeapSort class.


### Ambiguity resolution using `@Primary`

In case multiple beans are present that can satisfy the dependency, the default preferred one can be marked using `@Primary` annotation.

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

//imports
@Component
@Primary
public class BubbleSort implements SortAlgorithm {
    @Override
    public void sort(int[] arr) {
        //BubbleSort implementation
    }
}
```
- Bean marked as preferred one for dependency injection using `@Primary`.

[_HeapSort.java_](./HeapSort.java)
```java
/*
 * Implementation of SortAlgorithm: HeapSort
 */

//imports
@Component
public class HeapSort implements SortAlgorithm {

    @Override
    public void sort(int[] arr) {
        //HeapSort implementation
    }
}
```

[_BinarSearch.java_](./BinarySearch.java)
```java
/*
 * BinarySearch Implementation,
 * depends on: SortAlgorithm
 */

//imports
@Component
public class BinarySearch {
    // Dependency of BinarySearch
    // Dependency injected by Spring
    @Autowired
    private SortAlgorithm sortAlgorithm;

    int search(int arr[], int x) {
        //Binary search implementation
    }
}
```
[_AppConfig.java_](./AppConfig.java)
```java
//imports
@Configuration
@ComponentScan
public class AppConfig {
}
```

[_App.java_](./App.java)
```java
class App {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

		BinarySearch binarySearch = applicationContext.getBean("binarySearch", BinarySearch.class);

		int[] arr = { 2, 3, 4, 10, 40 };
		int result = binarySearch.search(arr, 40);
		System.out.println(result != -1 ? "Element found" : "Element not found");
	}
}
```
Output:
```shell
Element found
```

Herein, **BubbleSort** will be autowired in **BinarySearch** as it is marked as the default preferred one using the `@Primary` annotation.

### Ambiguity resolution using name of bean (Autowiring by name)
In case multiple beans are present that can satisfy the dependency, the dependency property can be named same as the preferred bean to avoid ambiguity.

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

//imports
@Component
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

//imports
@Component
public class HeapSort implements SortAlgorithm {

    @Override
    public void sort(int[] arr) {
        //HeapSort implementation
    }
}
```

[_BinarSearch.java_](./BinarySearch.java)
```java
/*
 * BinarySearch Implementation
 * depends on a SortAlgorithm
 */

//imports
@Component
public class BinarySearch {
    // Dependency of BinarySearch
    // property named same as the desired dependency
    @Autowired
    private SortAlgorithm heapSort;

    int search(int arr[], int x) {
        //Binary search implementation
    }
}
```
- The **SortAlgorithm** dependency instance explicitly named same as the desired dependency bean identifier (heapSort). 
  So, heapSort bean will be autowired.

[_AppConfig.java_](./AppConfig.java)
```java
//imports
@Configuration
@ComponentScan
public class AppConfig {
}
```

[_App.java_](./App.java)
```java
class App {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

		BinarySearch binarySearch = applicationContext.getBean("binarySearch", BinarySearch.class);

		int[] arr = { 2, 3, 4, 10, 40 };
		int result = binarySearch.search(arr, 40);
		System.out.println(result != -1 ? "Element found" : "Element not found");
	}
}
```
Output:
```shell
Element found
```

Herein, **HeapSort** will be autowired in **BinarySearch** as the dependency instance of **SortAlgorithm** is named same as the
**HeapSort** bean identifier (i.e: **heapSort**).


> Here the field injection is done so the property is named accordingly.<br> 
> If it was setter injection (@Autowired on setter) or constructor (@Autowired on constructor) then the 
> setter param or constructor param would need to be named same as desired resolution of dependency.

>**Note:**<br>
>If `@Primary` and Autowiring by name both are used at same time. Say, in previous example `@Primary` is applied on **BubbleSort** while
Autowiring by name is done for **HeapSort**.<br>
>In such cases, `@Primary` would be more preferred. So, the autowiring would happen using **BubbleSort**.
>`@Primary` has higher priority over the name of dependency instance.

### Ambiguity resolution using `@Qualifier`

As evident from previous example Spring uses the bean's name as a default qualifier value. It will inspect the container and look 
for a bean with the exact name as the property to autowire it.

But, the Qualifier for a bean can be explicitly specified as well.

[_SortAlgorithm.java_](./SortAlgorithm.java)
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

//imports
@Component
@Qualifier("bubble")
public class BubbleSort implements SortAlgorithm {
    @Override
    public void sort(int[] arr) {
        //BubbleSort implementation
    }
}
```
- A custom qualifier (**bubble**) specified for bean

[_HeapSort.java_](./HeapSort.java)
```java
/*
 * Implementation of SortAlgorithm: HeapSort
 */

//imports
@Component
@Qualifier("heap")
public class HeapSort implements SortAlgorithm {

    @Override
    public void sort(int[] arr) {
        //HeapSort implementation
    }
}
```
- A custom qualifier (**heap**) specified for bean

[_BinarySearch.java_](./BinarySearch.java)
```java
/*
 * BinarySearch Implementation
 * depends on a SortAlgorithm
 */

//imports
@Component
public class BinarySearch {
    // Dependency of BinarySearch
    // Dependency injected by Spring
    @Autowired
    @Qualifier("heap")
    private SortAlgorithm heapSort;

    int search(int arr[], int x) {
        //Binary search implementation
    }
}
```
- The desired dependency resolution candidate is specified through the qualifier value.

[_AppConfig.java_](./AppConfig.java)
```java
//imports
@Configuration
@ComponentScan
public class AppConfig {
}
```

[_App.java_](./App.java)
```java
class App {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

		BinarySearch binarySearch = applicationContext.getBean("binarySearch", BinarySearch.class);

		int[] arr = { 2, 3, 4, 10, 40 };
		int result = binarySearch.search(arr, 40);
		System.out.println(result != -1 ? "Element found" : "Element not found");
	}
}
```
Output:
```shell
Element found
```

Herein, **HeapSort** will be autowired in **BinarySearch** as its qualifier value is explicitly specified for **SortAlgorithm** dependency.

>@Qualifier annotation can also be specified on individual constructor arguments or method parameter or directly on @Autowired annotated fields.

>**Note:**<br>
>Qualifiers can also be applied to typed collection : <br>
>- Let's say we have three different `NotificationService`: `EmailNotificationService`, `SMSNotificationService` and `PostalLetterNotificationService`.
>- And out of these only `EmailNotificationService` and `SMSNotificationService` beans are annotated `@Qualifier("digital")`.
>  Then the `Set<NotificationService>` with same qualifier `digital` will be populated/autowired with these two beans only.
>
>Thus, qualifier need not be unique; they constitute filtering criteria.


**Summary:**<br>
If there is no other resolution indicator (such as a qualifier or a primary marker) for a non-unique dependency situation,
Spring matches the injection point name (field name/parameter name) against the available beans and chooses the same named candidate, if any exists.
`@Autowired` applies to fields, constructors, and multi-argument methods, allowing for narrowing through qualifier annotations at the parameter level.

So, there are 3 different ways of resolving multiple candidates available for autowiring:
1. `@Primary`
2. `@Qualifier`
3. beanName (default Qualifier)

If there is one clear favourite then `@Primary` should be used else autowiring by name or qualifier should be used.

## Java Generic types as implicit Autowirng qualifier

The Java generic types can be used as an implicit form of qualification.

Example: There are two different implementations of `Converter` interface accepting different generic values.
```java
public interface Converter<T, R> {
    R convert(T value);
}

@Component
public class StringToIntConverter implements Converter<String, Integer> {
    public Integer convert(String value) {
        return Integer.parseInt(value);
    }
}

@Component
public class DoubleToStringConverter implements Converter<Double, String> {
    public String convert(Double value) {
        return String.valueOf(value);
    }
}
```

Now, in another component, we want to use these converters based on their generic types:

```java
@Component
public class ConversionService {

    @Autowired
    private Converter<String, Integer> stringToIntConverter; // Injects the StringToIntConverter bean

    @Autowired
    private Converter<Double, String> doubleToStringConverter; // Injects the DoubleToStringConverter bean

    public Integer convertStringToInt(String value) {
        return stringToIntConverter.convert(value);
    }

    public String convertDoubleToString(Double value) {
        return doubleToStringConverter.convert(value);
    }
}
```

- When `@Autowired` is used to inject these converters into the `ConversionService`, Spring will use the generic types as implicit qualifiers to determine which bean to inject.

>**Note:**<br>
> Spring in built interfaces (like: BeanFactory, ApplicationContext, Environment, ResourceLoader, ApplicationEventPublisher, and MessageSource) can also be direclty autowired 
> and spring will internally provide the appropriate implementation (like ConfigurableApplicationContext or ResourcePatternResolver).