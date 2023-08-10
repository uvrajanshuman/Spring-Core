# Spring Expression Language

SpEL helps in dependency injection dynamically at runtime

SpEL expressions begin with the `#` symbol and are wrapped in braces: `#{expression}`.

Using Spring Expression Language, we can:

1. Refer to other beans by id attribute
2. Refer to the properties and invoke methods defined in other beans.
3. Refer to the static constants and invoke static methods
4. Perform mathematical operation on values
5. Perform relational and logical comparisions
6. Perform Regular Expressions Matching
7. Accessing Collections

```java
@Component
public class SpElExample {
    
    /* Arithmetic Operators */
    @Value("#{10 + 10}") //20
    private int sum;

    @Value("#{'ABC' + 'DEF'}") //"ABCDEF"
    private String concatString;

    @Value("#{10 - 5}") //5
    private int difference;

    @Value("#{5 * 2}") //10.0
    private double product;

    @Value("#{30 / 2}") //15.0
    private double division;

    @Value("#{11 % 2}") //1.0
    //@Value("#{11 mod 2}") //1.0
    private double modulo;

    @Value("#{2 ^ 2}") //4.0
    private double powerOf;

    @Value("#{(2 + 2) * 2 + 2}") //10.0
    private double brackets;

    /* Relational Operators */
    @Value("#{5 == 5}") //true
    //@Value("#{5 eq 5}) //true
    private boolean equal;

    @Value("#{5 != 5}") //false
    //@Value("#{5 ne 5}") //false
    private boolean notEqual;

    @Value("#{2 < 4}") //true
    //@Value("#{2 lt 4}") //true
    private boolean lessThan;

    @Value("#{5 <= 5}") //true
    //@Value("#{5 le 5}") //true
    private boolean lessThanOrEqual;

    @Value("#{5 > 4}") //true
    //@Value("#{5 gt 4}") //true
    private boolean greaterThan;

    @Value("#{5 >= 5}") //true
    //@Value("#{5 ge 5}") //true
    private boolean greaterThanOrEqual;

    /* Logical Operators */
    @Value("#{200 > 100 && 200 < 400}") //true
    //@Value("#{200 > 100 and 200 < 400}") //true
    private boolean and;

    @Value("#{200 > 100 || 200 < 100}") //true
    //@Value("#{200 > 100 or 200 < 100}") //true
    private boolean or;

    @Value("#{!true}")  //false
    //@Value("#{not true}") //false
    private boolean not;

    /* Ternary Operation */
    @Value("#{5 > 4 ? 5 : 4}") //5
    private int ternary;

    @Value("#{someBean.someProperty != null ? someBean.someProperty : 'default'}") //'default'
    private String anotherTernary;

    // Above example can be shortened using elvis operator
    /* Elvis Operator */ //will inject 'default' if someProperty is null
    @Value("#{someBean.someProperty ?: 'default'}") //'default' 
    private String elvis;

    /* Regular Expression */
    @Value("#{'John Doe' matches '[a-zA-Z\\s]+' }") //true
    private boolean validName;
    
    //...
}
```

- Some operators have alphabetic aliases as well. For example, `<` has alias `lt`. 
This is because XML-based configuration can't use operators having angle brackets. So, aliases can be used instead.

| Type               | Operators                                     |
|--------------------|-----------------------------------------------|
| Arithmetic         | 	+, -, *, /, %, ^, div, mod                   |
| Relational         | 	<, >, ==, !=, <=, >=, lt, gt, eq, ne, le, ge |
| Logical            | and, or, not, &&, \|\|, !                     |
| Ternary            | condition ? value1 : value2                   |
| Elvis              | property ?: defaultValue                      |
| Regular Expression | matches                                       |

## Accessing static variables and methods of a Class
- The static variables and methods of a class can be accessed using dot (`.`) notation. 
- `T(fullyQualifiedClassName).staticVariable/staticMethod`
```java
@Component
public class SpElExample {
    // Referencing static variable of a Math class
    @Value("#{T(java.lang.Math).PI}") //3.141592653589793
    private double pi;

    // Referencing static method of Math class
    @Value("#{T(java.lang.Math).random()}") //0.35702526638036214
    private String random;
    //...
}
```

## Referring other Bean's properties and methods
consider the following bean, and it's property to be referred in upcoming examples.
```java
@Component
public class SomeBean {
    
    private String someProperty; //null

    private String anotherProperty;

    private int[] someArray;

    private List<Integer> someList;

    private Map<Character,String> someMap;

    //instance initialization block
    {
        anotherProperty = "someBean.anotherProperty";
        someArray = new int[]{3,6,9,12,15,18};
        someList = Arrays.asList(1,2,3,4,5,6);
        someMap = new HashMap<>();
        someMap.put('A', "Apple");
        someMap.put('B', "Ball");
        someMap.put('C', "Cat");
    }

    public String someMethod(){
        return "someBean.someMethod()";
    }

    //constructor & getters, setters
}
```
>Other Bean's properties are accessed through getter methods.

**Referring other beans:**
- Other beans can be referred directly using their identifier.
```java
@Component
public class SpElExample {
    /* Referring other bean */
    @Value("#{someBean}")   //com.refstash.externalizing_configurations.spring_expression_language.SomeBean@55a147cc
    private SomeBean someBean;
    
    //...
}
```
**Referring other Bean's property:**
- Other bean's property can be accessed by using dot(`.`) notation. 
- `beanName.property`
```java
@Component
public class SpElExample {
    /* Referencing other bean's property */
    
    // Ternary Operator
    @Value("#{someBean.someProperty != null ? someBean.someProperty : 'default'}")
    private String anotherTernary; // 'default'

    // Above example can be shortened using elvis operator
    // Elvis Operator
    @Value("#{someBean.someProperty ?: 'default'}") 
    private String elvis; // 'default'

    @Value("#{someBean.anotherProperty}") //'someBean.anotherProperty'
    private String someBeanAnotherProperty;
    
    //...
}
```
**Referring other Bean's method:**
- Other bean's methods can be accessed by using dot(`.`) notation. 
- `beanName.method()`
```java
@Component
public class SpElExample {
    /* Referring other bean's method */
    @Value("#{someBean.someMethod()}") //'someBean.someMethod()'
    private String someBeanMethod;
    
    //...
}
```

## Referring and Accessing Collections
**Referring Collections:**
```java
@Component
public class SpElExample {
    /* Referring Collections */
    @Value("#{someBean.someArray}")
    private int[] referredArray; //[3, 6, 9, 12, 15, 18]
    
    @Value("#{someBean.someList}")  //[1, 2, 3, 4, 5, 6]
    private List<Integer> referredList;

    @Value("#{someBean.someMap}")   //{A=Apple, B=Ball, C=Cat}
    private Map<Character, String> referredMap;
    
    //...
}
```
**Accessing Collections:**
```java
@Component
public class SpElExample {

    /* Accessing Collection */
    @Value("#{someBean.someArray[0]}")   //3
    private int accessedFirstIntFromArray;

    @Value("#{someBean.someList[0]}")   //1
    private int accessedFirstIntFromList;

    @Value("#{someBean.someList.size()}")   //6
    private int accessedListLength;

    @Value("#{someBean.someMap['A']}")  //'Apple'
    private String accessedStringFromMap;
    
    //...
}
```

### Collection selection/filtering

Selection in Spring Expression Language (SpEL) is a mechanism for transforming a source collection into an alternative collection by singling out/filtering out specific entries.

The syntax for selection is `.?[selectionExpression]`. <br>
This syntax filters the collection, generating a new collection that comprises a subset of the original elements. 

For instance, Let's say `studentList` is a List of Students, the following expression will filter out students with marks greater than 50.
```java
//...
@Value("#{studentList.?[marks > 50]}")
private List<Student> filteredStudents;
//...
```

Selection applies to arrays and any entities that implement `java.lang.Iterable` or `java.util.Map`. <br>
In the case of arrays or lists, the selection criteria are evaluated against each individual element. <br>
However, for maps, the criteria are applied to each map entry, where each entry comprises a key-value pair (Java's Map.Entry objects). 
These `keys` and `values` are accessible properties available for use in the selection process.

The following expression filters a map named `map`, and returns a new map named `newMap` that consists of those elements of the original map where the entry’s value is less than 27.
```java
//...
@Value("#{map.?[value<27]}")
private Map newMap;
//...
```

Example: Collection selection/filtering 
```java
@Component
public class SpElExample {

    // Collection filtering
    @Value("#{someBean.someList.?[#this % 2 == 0]}") //[2, 4, 6]
    private List<Integer> evenNumList;

    @Value("#{someBean.someArray.?[#this > 5]}") //[6, 9, 12, 15, 18]
    private int[] greaterThanFiveArr;

    @Value("#{someBean.someMap.?[value.contains('a')]}") //{B=Ball, C=Cat}
    private Map<Character,String> newMap;
    
    //...
}
```

>**Note:**<br>
> In Spring Expression Language (SpEL), `#this` is a predefined variable that refers to the current object being evaluated within an expression. 
> It's often used within collection selection and filtering expressions to refer to the individual elements of the collection being processed.

### Collection Projection

Projection involves deriving a new collection by allowing a collection to guide the assessment of a sub-expression. 

The syntax for projection is `.![projectionExpression]`.

Example: Let's say we have a list of books, and we want to create a list of titles from these books. This can be achieved using projection as shown below:

```java
//...
@Value("#{books.![title]}")
List<String> bookTitles;
//...
```

In this example, the projection expression `books.![title]` is used to iterate through each book in the list and extract its title. 
The result is a new list containing the titles of the books.

Projection works not only with lists but also with arrays, java.lang.Iterable, and java.util.Map implementations. <br>
When applied to a map, the projection expression is evaluated against each map entry (represented by a Java Map.Entry), 
and the result is a collection containing the evaluations of the projection expression for each map entry.


[**Complete working example of SpEL can be found here.**](../spring_expression_language)

### Using Spring Expression Language in XML configuration file
Spring Expresssion Language can also be used in XML configuration file.
Example:
```xml

<bean id="someCar" class="somepackage.Car">
   <!-- ... -->
   <property name="engine" value="#{engine}"/>
   <property name="horsePower" value="#{engine.horsePower}"/>
</bean>
```
