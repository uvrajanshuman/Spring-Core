# Need of Dependency Injection

Dependency injection is a design pattern used to manage dependencies between classes or components.<br>
It makes a class independent of its dependencies.

It plays a crucial role in achieving loose coupling, modularity, testability, and flexibility in an application. 

**1. Decoupling and Modularity:** <br>
Dependency injection helps to decouple the components in an application.
Instead of components directly creating their dependencies, they receive them from external sources.<br> 
This promotes modularity, as components can focus on their specific responsibilities without being tightly coupled to specific implementations of their dependencies.<br> 
Changes to one component or dependency can be made without affecting others, making the codebase more maintainable and flexible.

**2. Reusability:** <br>
Dependency injection enables the reuse of components and dependencies across different parts of an application or even across different applications.<br>
By providing dependencies externally, components can be easily configured and used in different contexts, promoting code reuse and reducing code duplication.

**3. Testability:** <br>
Dependency injection greatly simplifies unit testing and promotes better testability of individual components. <br>
By providing dependencies through interfaces or abstractions, it becomes easier to substitute real dependencies with mock or stub implementations during testing. 
This allows for isolated testing of components, ensuring that they function correctly without the need to involve or rely on complex dependencies.

**4. Flexibility and Maintainability:** <br>
Dependency injection makes it easier to change or switch dependencies without modifying the code that uses those dependencies. 
This flexibility allows for easier maintenance and adaptation of the application over time. <br>
New implementations or versions of dependencies can be introduced by simply updating the configuration, without requiring changes to the dependent components.

**5. Separation of Concerns:** <br>
Dependency injection helps to enforce the separation of concerns by clarifying the responsibility of each component.<br> 
Instead of a component creating or managing its dependencies, it focuses solely on its core functionality. 
This separation allows for better code organization, improves code readability, and enhances overall system design.

Overall, dependency injection is a powerful technique that promotes modularity, testability, maintainability, and flexibility in software development.
It reduces coupling between components, enables code reuse, and enhances the overall quality and scalability of an application.

## Dependency

A Dependency of a class is simply what are the things required by the class to perform its functionality.

```java
public class BinarySearch{ 
    private SortAlgorithm sortAlgorithm;
    //...
}
```

Here, The **BinarySearch** class depends on **SortAlgorithm**. So **SortAlgorithm** is a dependency of **BinarySearch**.

A complex application will have several dependency at different levels.

## Typical code without dependency injection 

```java
public class BinarySearch{
    // Direct instantiation of dependency
    private SortAlgorithm sortAlgorithm = new BubbleSortAlgorithm();
    //...
}
```

```java
public class BubbleSortAlgorithm implements SortAlgorithm{
    //...
}
```

Here in, the dependency is directly instantiated in the class (**BinarySearch**) itself.

But what if a different sortAlgorithm is required, lets say now **HeapSort** is required instead of **BubbleSort**. <br>
In that case following changes would be required to the **BinarySearch** class :
```java
SortAlgorithm sortAlgorithm = new HeapSort();
```
- The `sortAlgorithm` needs be re-instantiated with the desired implementation of `SortAlgorithm`; and the entire code needs to be re-compiled.
- This is an example of tight-coupling as dependencies are directly being instantiated in the class.
- `BinarySearch` and `SortAlgorithm` are tightly-coupled. Hence, `BinarySearch` is absolutely dependent on a specific `SortAlgorithm`.

>A good code is loosely coupled. The way to remove tight coupling is by removing the direct instantiation of the dependencies.

[Complete working code for this approach](./without_dependency_injection)

## Removing Tight-coupling through Dependency Injection

```java
public class BinarySearch{
    private SortAlgorithm sortAlgorithm;

    //Instantiation of sortAlgorithm through constructor.
    public BinarySearch(SortAlgorithm sortAlgorithm){
        this.sortAlgorithm = sortAlgorithm;
    }
    
    //...
}
```
```java
public class BubbleSort implements SortAlgorithm{
    //...
}
```

```java 
public class App{
    public static void main(String []args){
        //Instance of desired SortAlgorithm.
        SortAlgorithm sortAlgorithm = new BubbleSort();
        //Injecting dependency while instantiating.
        BinarySearch binarySearch = new BinarySearch(sortAlgorithm);
        //...
    }
}
```

- In this case whoever, wants to use `BinarySearch` can instantiate it by passing the `SortAlgorithm` implementation of their choice.
This helps to achieve loose-coupling.
- This can also be done by passing the dependency through setter method instead of constructor.
- `BinarySearch` and `SortAlgorithm` are no more tightly-coupled. Hence, `BinarySearch` is not dependent on a specific `SortAlgorithm`.
- The choice of `SortAlgorithm` now depends on the user of `BinarySearch`.

>Spring framework does the same. It instantiates the objects and populates their dependencies.

[Complete working code for this approach](./dependency_injection)

## Removing Tight-coupling through Spring's Dependency Injection

Spring can also be used for dependency injection to achieve loose-coupling.<br>
However, the objects that the spring framework needs to manage and the dependencies of those objects, needs to be specified to the spring.

For Example: In Spring's Annotation Based configuration
- `@Component`: specifies the classes that Spring needs to manage.<br>
    Spring would start creating and managing instance of these classes.
- `@Autowired`: specifies the dependencies of the managed classes.<br>
    Spring would look for these dependencies among the classes it manages and would inject it wherever required.

```java
@Component
public class BinarySearch {
    private SortAlgorithm sortAlgorithm;

    @Autowired
    public BinarySerach(SortAlgorithm sortAlgorithm) {
        this.sortAlgorithm = sortAlgorithm;
    }
    
    //...
}
```
```java
@Component
public class BubbleSortAlgorithm implements SortAlgorithm{
    //...
}
```

Spring would internally do this:
```java 
SortAlgorithm sortAlgorithm = new BubbleSortAlgorithm();
//Injecting dependency while instantiating.
BinarySearch binarySearch = new BinarySearch(sortAlgorithm);
```
- It would create an instance of **BubbleSortAlgorithm** and pass it to the setter of **BinarySearch** after instantiating it.
  
- Alternatively, Spring can also set the **SortAlgorithm** property of **BinarySearch** using constructor or through Java reflection API
  (depending on the type of _Autowiring_).

>Spring would make sure it creates the instances of all the objects it manages with their dependencies properly populated.

[Complete working code for this approach](./dependency_injection_with_spring)