# Spring Dependency Injection using XML based configuration

Herein, all the metadata is provided to the Spring IOC container through xml file(s).

This is the traditional way of configuring beans in Spring.<br>
In this approach, we do all bean mappings in XML configuration file(s).
## Sample bean declaration

_config.xml_
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- bean definitions here -->
    <bean class="com.example.Spring_core..." name="dependency1">
        <property name="..." value="..."/>
    </bean>

</beans>
```

And `ClassPathXmlApplicationContext` or `FileSystemXMLApplicationContext` is used as an implementation of ApplicationContext;
based on the location of Configuration xml file.

| Properties      | Description                                                                                                                                                                                                                                                                                                      |
|-----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| class           | Mandatory attribute, specifies the bean class to be used to create the bean.                                                                                                                                                                                                                                     |
| id              | The id attribute is the primary and recommended way to specify the identifier for a bean.<br> It must be unique within the application context.                                                                                                                                                                  |
| name            | The name attribute allows to specify multiple names or aliases for a bean. comma/semicolon/whitespace separated list of names can be provided for the bean.<br> These names can be used as alternative identifiers to refer to the bean.<br>id and/or name attributes can be used specify the bean identifier(s) |
| property        | used to inject the dependencies through setter injection                                                                                                                                                                                                                                                         |
| constructor-arg | used to inject the dependencies through constructor injection                                                                                                                                                                                                                                                    |

>**Note:**<br>
>It is not mandatory to supply name or an id for a bean. 
>The container itself generates a unique name for the bean, following the rule: taking the simple class name and turning its initial character to lower-case.
>However, if you want to refer to that bean by name, through the use of the ref element, you must provide a name.

## Instantiating the container

```java
ApplicationContext context = new ClassPathXmlApplicationContext("resources/services.xml", "resources/daos.xml");
```
Herein, `service.xml` and `daos.xml` are separate xml configuration files

## Instantiation of Beans
The beans can be instantiated in following three ways:

### 1. Instantiation with the Constructor:

Generally beans are instantiated using their constructor.
The `<bean>` element in XML configuration can define the class of the bean, and constructor arguments can be provided using `<constructor-arg>` elements.
```xml
<beans>
    <bean id="exampleBean" class="examples.ExampleBean"/>
    <bean name="anotherExample" class="examples.ExampleBeanTwo"/>
</beans>
```
### 2. Instantiation with Static Factory Method:

Another way of instantiating the bean is using a static factory method.
The `class` attribute in the `<bean>` element specifies the class containing the factory method, 
and the `factory-method` attribute provides the name of the factory method itself. 

This method is useful for calling static factories in legacy code. Arguments for the factory method can be passed using `<constructor-arg>` elements.

Example:
```xml
<beans>
    <!-- beans instantiated through constructor -->
    <bean id="anotherExampleBean" class="examples.AnotherBean"/>
    <bean id="yetAnotherBean" class="examples.YetAnotherBean"/>
    
    <!-- bean instantiation using static factory method -->
    <bean id="exampleBean" class="examples.ExampleBean" factory-method="createInstance">
        <constructor-arg ref="anotherExampleBean"/>
        <constructor-arg ref="yetAnotherBean"/>
        <constructor-arg value="1"/>
    </bean>
</beans>
```
- The definition does not specify the type (class) of the returned object, but rather the class containing the factory method.

```java
public class ExampleBean {

	// a private constructor
    private ExampleBean(AnotherBean anotherBean, YetAnotherBean yetAnotherBean, int i) {
		//...
	}

	// static factory method
    public static ExampleBean createInstance (AnotherBean anotherBean, YetAnotherBean yetAnotherBean, int i) {
        ExampleBean eb = new ExampleBean (anotherBean, yetAnotherBean, i);
		// ...
		return eb;
    }
}
```

### 3. Instantiation with Instance Factory Method:
Similar to static factory methods, instance factory methods can also be used for bean instantiation.

In this case, the `class` attribute in the target `<bean>` element is left empty. 
Instead, the `factory-bean` attribute specifies the name of a bean in the container that contains the instance method to be invoked for creating the new target bean. 
And, the `factory-method` attribute provides the name of the instance factory method of that factory bean.

Example:
```xml
<beans>
    <!-- the factory bean, which contains a method called createAnotherBeanExampleInstance() -->
    <bean id="exampleBean" class="examples.ExampleBean">
        <!-- inject any dependencies required by this locator bean -->
    </bean>
    
    <!-- bean instantiated via the factory bean's factory method -->
    <bean id="anotherExampleBean"
        factory-bean="exampleBean"
        factory-method="createAnotherBeanExampleInstance"/>
</beans>
```

```java
public class ExampleBean {

    // a private constructor
    public ExampleBean() {
        //...
    }

    // instance factory method to create AnotherExampleBean
    public static AnotherExampleBean createAnotherBeanExampleInstance() {
        AnotherExampleBean anotherExampleBean = new AnotherExampleBean();
        // ...
        return anotherExampleBean;
    }
}
```

>**Note:**<br>
>The actual runtime type of particular bean can be accurately found using the `BeanFactory.getType` method for the specified bean name.

## Dependency Injection
In XML based configuration, there are majorly two type of dependency injection: Constructor-based dependency injection and Setter-based dependency injection.

[**Constructor Injection**](./constructor_injection/README.md)

[**Property/Setter Injection**](./setter_injection/README.md)

## Autowiring
Autowiring enables automatic dependency injection without the need of explicit configuration.<br>
Instead of manually wiring dependencies using setter methods or constructor arguments, 
Spring can automatically discover the required beans and wire them together based on certain rules.

[**Autowiring in XML based configuration**](./autowiring/README.md)

## Composing XML based configuration metadata

It can be useful to have bean definitions span multiple XML files,
as each individual XML configuration file can represent a logical layer or module in the architecture.

`<import/>` element allows to import one or more configuration files into another configuration file. <br>
It provides a way to modularize and organize the configuration by combining multiple configuration files into a single
configuration or allowing one configuration class to depend on others.

Example:

_config_a.xml_
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- ban declarations here-->
    <bean id="beanA" class="BeanA"/>

</beans>
```

_config_b.xml_
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- importing configuration -->
    <import resource="resources/configA.xml"/>
    
    <!-- ban declarations here-->
    <bean id="beanB" class="BeanB"/>

</beans>
```
Now, instead of specifying **config_a** and **config_b** while instantiating the context, only **config_b** can be
specified (as it already imports the **config_a**)

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("resources/config_b.xml");
        // both beanA and beanB are available
        BeanA = context.getBean("beanA", BeanA.class);
        BeanB = context.getBean("beanB", BeanB.class);
    }
}
```

## Bean definition inheritance
A bean definition can contain various configuration details (like constructor arguments, property values, scopes, initialization method etc.) 
that may be common for a lot of other bean definitions as well.<br>
To avoid duplicating configuration across similar beans, Spring provides a powerful feature called "Bean Definition Inheritance".  
This mechanism allows to create parent and child bean definitions, where the child inherits configuration data from the parent, with the ability 
to override or add new values as needed. 

**Defining Parent and Child Bean Definitions**<br>
The `parent` attribute of a `<bean>` element can be used to specify the parent template, from where this bean will inherit all the configurations.
```xml
<bean id="parentBean" class="com.refstash.ParentBean">
    <property name="property1" value="value1" />
    <property name="property2" value="value2" />
</bean>

<bean id="childBean" class="com.refstash.ChildBean" parent="parentBean">
    <property name="property2" value="childValue2" /> <!-- Overrides property2 from the parent -->
    <property name="property3" value="value3" /> <!-- New property in the child bean -->
</bean>
```
- In this example, the childBean inherits the property1 and property2 from the parentBean and overrides property2 with a new value.
- Additionally, it introduces a new property, property3, that is specific to the childBean.

Additionally, a bean can be marked as abstract by setting the `abstract` attribute of `<bean>` element to **true**. 
The `class` attribute of such beans should be left blank.<br>
Such abstract beans can not be instantiated, and serves purely as template to child beans.

**Inheritance in Collection Types**<br>
Bean definition inheritance can also be applied to collection types, such as `<list>`, `<map>`, `<set>`, or `<props>`. 
This allows to define a parent collection and have child collections inherit and override values from the parent.

While merging the list the ordering is maintained, the parent’s values precede all the child list’s values. 
In the case of the Map, Set, and Properties collection types, no ordering exists.

```xml
<beans>
    <!-- Parent bean -->
    <bean id="parentToppings" abstract="true" class="com.example.Toppings">
        <property name="toppings">
            <list>
                <value>Pepperoni</value>
                <value>Mushrooms</value>
                <value>Onions</value>
            </list>
        </property>
    </bean>

    <!-- Child bean -->
    <bean id="childToppings" parent="parentToppings">
        <property name="toppings">
            <list merge="true"> <!-- Merge with parent collection -->
                <value>Extra Cheese</value> <!-- Add new topping -->
                <value>Olives</value> <!-- Add new topping -->
            </list>
        </property>
    </bean>
</beans>

```
Due to `merge=true` attribute on the toppings property (`<list>` element) in child definition,
the child bean will have toppings List containing the result of merging the child's toppings list with the parent's toppings list

The child topping list will be following:
```shell
Pepperoni
Mushrooms
Onions
Extra Cheese
Olives
```
> If the `merge=true` was not set, the child bean topping list would have just overridden the parent one.