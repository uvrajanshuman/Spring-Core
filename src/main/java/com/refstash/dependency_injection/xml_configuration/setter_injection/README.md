# Setter/Property Injection

The dependencies are injected through the setter methods of the bean.<br>
The setter injection can be achieved using either `<property>` tag or `p` schema.

## Injecting primitive types
The `<property>` element is used to specify the property name and the `value` attribute is used to provide the actual value.

Ex:
```xml
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car">
    <!--injecting primitive type (string)-->
    <property name="brand" value="Tata"/>
</bean>
```
or
```xml
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car">
    <!--injecting primitive type (string)-->
    <property name="brand">
        <value>Tata</value>
    </property>
</bean>
```
The above example specifies **Tata** as value for the **brand** property of the bean (tataHarrier) of Car class.

## Injecting reference types
The `<property>` element can be used to specify the property name along with the `ref` attribute to refer to another bean by its identifier (`id` or `name`).

Ex:
```xml
<!--Engine bean-->
<bean id="harrierEngine" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Engine">
    <!--engine bean property declarations-->
</bean>

<!--Car bean-->
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car">
    <!--injecting reference type-->
    <property name="engine" ref="harrierEngine"/>
    <!--car bean other properties declarations-->
</bean>
```
or
```xml
<!--Engine bean-->
<bean id="harrierEngine" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Engine">
    <!--engine bean property declarations-->
</bean>

<!--Car bean-->
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car">
    <!--injecting reference type-->
    <property name="engine">
        <ref bean="harrierEngine"/>
    </property>
    <!--car bean other properties declarations-->
</bean>
```
The above example shows reference injection of a bean (harrierEngine) of Engine class into the property (engine) of another bean (tataHarrier) of Car class.

Another way would be inline bean declaration of dependency references.
### Inner Beans (inline declaration of beans)
A `<bean/>` element inside the `<property/>` or `<constructor-arg/>` elements defines an inner bean.

Example:
```xml
<bean id="outer" class="...">
  <!-- instead of using a reference to a target bean, simply define the target bean inline -->
  <property name="target">
    <!-- this is the inner bean -->
    <bean class="com.example.Person"> 
      <property name="name" value="Fiona Apple"/>
      <property name="age" value="25"/>
    </bean>
  </property>
</bean>
```
- An inner bean definition does not require a defined ID or name. 
If specified, the container does not use such a value as an identifier. 
- The container also ignores the scope flag on creation, because inner beans are always anonymous and are always created with the outer bean. 
- It is not possible to access inner beans independently or to inject them into collaborating beans other than into the enclosing bean.

## Injecting collections
For injecting collections, such as lists, sets, or maps, `<list>`, `<set>`, or `<map>` elements can be used respectively within the `<property>` element.

**List:**

```xml
<!--Car bean-->
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car">
    <!--injecting list-->
    <property name="features">
        <list>
            <value>Power Steering</value>
            <value>Power Windows</value>
            <value>Air Conditioner</value>
            <value>Heater</value>
            <value>Driver and Passenger Airbag</value>
            <value>Automatic climate control</value>
        </list>
    </property>
    <!--car bean other properties declarations-->
</bean>
```
The above example shows injection of list specified using `<list>` tag into **features** property of bean (tataHarrier) of Car class.

**Set:**

```xml
<!--Car bean-->
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car">
    <!--injecting set-->
    <property name="colors">
        <set>
            <value>Orcus White</value>
            <value>Calypso Red</value>
            <value>Daytona Grey</value>
            <value>Oberon Black</value>
        </set>
    </property>
    <!--car bean other properties declarations-->
</bean>
```
The above example shows injection of set specified using `<set>` tag into **colors** property of bean (tataHarrier) of Car class.


**Map:**

```xml
<!--Car bean-->
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car">
    <!--injecting map-->
    <property name="specifications">
        <map>
            <entry key="Mileage" value="14.6kmpl"/>
            <entry key="Fuel Type" value="Diesel"/>
            <entry key="Fuel Tank Capacity" value="50.0"/>
            <entry key="Seating Capacity" value="5"/>
            <entry key="Body Type" value="SUV"/>
        </map>
    </property>
    <!--car bean other properties declarations-->
</bean>
```
The above example shows injection of map specified using `<map>` tag into **specifications** property of bean (tatHarrier) of Car class.


## Complete Example demonstrating setter injection of different types of values:

[_Car.java_](./Car.java)
```java
//imports
public class Car {
    
    private String brand;
    private String model;
    private Engine engine;
    private Map<String,String> specifications;
    private List<String> features;
    private Set<String> colors;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Map<String, String> specifications) {
        this.specifications = specifications;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public Set<String> getColors() {
        return colors;
    }

    public void setColors(Set<String> colors) {
        this.colors = colors;
    }

    @Override
    public String toString() {
        return "Car{" +
                "\n brand='" + brand + '\'' +
                "\n model='" + model + '\'' +
                "\n engine=" + engine +
                "\n specifications=" + specifications +
                "\n features=" + features +
                "\n colors=" + colors +
                "\n}";
    }
}
```

[_Engine.java_](./Engine.java)
```java
public class Engine {
    
    private String type;
    private String displacement;
    private int cylinder;
    private String transmissionType;
    private String gearBox;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public int getCylinder() {
        return cylinder;
    }

    public void setCylinder(int cylinder) {
        this.cylinder = cylinder;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public String getGearBox() {
        return gearBox;
    }

    public void setGearBox(String gearBox) {
        this.gearBox = gearBox;
    }

    @Override
    public String toString() {
        return "Engine{" +
                "type='" + type + '\'' +
                ", displacement='" + displacement + '\'' +
                ", cylinder=" + cylinder +
                ", transmissionType='" + transmissionType + '\'' +
                ", gearBox='" + gearBox + '\'' +
                '}';
    }
}
```

[_config.xml_](../../../../../../../main/resources/com/refstash/dependency_injection/xml_configuration/setter_injection/config.xml)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:p="http://www.springframework.org/schema/p" 
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd">

    <!--bean declarations-->
    <!--Engine bean-->
    <bean id="harrierEngine" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Engine">
        <property name="type" value="Kryotec 2.0 L Turbocharged Engine"/>
        <property name="displacement" value="1956cc"/>
        <property name="cylinder" value="4"/>
        <property name="transmissionType" value="Automatic"/>
        <property name="gearBox" value="6-Speed"/>
    </bean>

    <!-- property injection using p schema -->
    <!--
    <bean id="harrierEngine" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Engine"
          p:type="Kryotec 2.0 L Turbocharged Engine"
          p:displacement="1956cc"
          p:cylinder="4"
          p:transmissionType="Automatic"
          p:gearBox="6-Speed"/>
    -->
  
    <!--Car bean-->
    <bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car">
        <property name="brand" value="Tata"/>
        <property name="model" value="Harrier"/>
        <property name="engine" ref="harrierEngine"/>
        <property name="specifications">
            <map>
                <entry key="Mileage" value="14.6kmpl"/>
                <entry key="Fuel Type" value="Diesel"/>
                <entry key="Fuel Tank Capacity" value="50.0"/>
                <entry key="Seating Capacity" value="5"/>
                <entry key="Body Type" value="SUV"/>
            </map>
        </property>
        <property name="features">
            <list>
                <value>Power Steering</value>
                <value>Power Windows </value>
                <value>Air Conditioner</value>
                <value>Heater</value>
                <value>Driver and Passenger Airbag</value>
                <value>Automatic climate control</value>
            </list>
        </property>
        <property name="colors">
            <set>
                <value>Orcus White</value>
                <value>Calypso Red</value>
                <value>Daytona Grey</value>
                <value>Oberon Black</value>
            </set>
        </property>
    </bean>
</beans>
```
- The constructor injection can also be done using the `p` schema instead of `<property>` tag.
  (Ex: commented `harrierEngine` bean declaration in above example).

[_App.java_](./App.java)
```java
//imports

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/com/refstash/dependency_injection/xml_configuration/setter_injection/config.xml");
        Car tataHarrier = context.getBean("tataHarrier",Car.class);
        System.out.println(tataHarrier);
    }
}
```

Output:
```shell
Car{
 brand='Tata'
 model='Harrier'
 engine=Engine{type='Kryotec 2.0 L Turbocharged Engine', displacement='1956cc', cylinder=4, transmissionType='Automatic', gearBox='6-Speed'}
 specifications={Mileage=14.6kmpl, Fuel Type=Diesel, Fuel Tank Capacity=50.0, Seating Capacity=5, Body Type=SUV}
 features=[Power Steering, Power Windows , Air Conditioner, Heater, Driver and Passenger Airbag, Automatic climate control]
 colors=[Orcus White, Calypso Red, Daytona Grey, Oberon Black]
}
```


## Spring standalone collections:
When a collection is defined inside a bean declaration, it's scope is restricted to the bean of it's declaration.
But, by using standalone collections, same collection can be reused for injecting in multiple beans.<br>
It's scope is not limited to a single bean and can be reused as per need.

To make standalone collection `util` schema must be added to XML configuration.

The xml configuration used in above example can be modified to use standalone collections:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:p="http://www.springframework.org/schema/p"
    xmlns:util="http://www.springframework.org/schema/util"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd
    http://www.springframework.org/schema/util
    http://www.springframework.org/schema/util/spring-util.xsd">
  
    <!-- bean definitions here -->

    <!-- Engine bean declaration using p schema -->
    <bean id="harrierEngine" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Engine"
        p:type="Kryotec 2.0 L Turbocharged Engine"
        p:displacement="1956cc"
        p:cylinder="4"
        p:transmissionType="Automatic"
        p:gearBox="6-Speed"/>

    <!-- Car properties declaration -->
    <!-- Map for Car specifications -->
    <util:map id="carSpecifications">
        <entry key="Mileage" value="14.6kmpl"/>
        <entry key="Fuel Type" value="Diesel"/>
        <entry key="Fuel Tank Capacity" value="50.0"/>
        <entry key="Seating Capacity" value="5"/>
        <entry key="Body Type" value="SUV"/>
    </util:map>

    <!-- List for Car features -->
    <util:list id="carFeatures">
        <value>Power Steering</value>
        <value>Power Windows</value>
        <value>Air Conditioner</value>
        <value>Heater</value>
        <value>Driver and Passenger Airbag</value>
        <value>Automatic climate control</value>
    </util:list>

    <!-- Set for Car colors -->
    <util:set id="carColors">
        <value>Orcus White</value>
        <value>Calypso Red</value>
        <value>Daytona Grey</value>
        <value>Oberon Black</value>
    </util:set>

    <!-- Car bean declaration -->
    <!-- 
    <bean id="harrierEngine" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Engine">
        <property name="brand" value="Tata"/>
        <property name="model" ref="Harrier"/>
        <property name="engine" ref="harrierEngine"/>
        <property name="specifications" ref="carSpecifications"/>
        <property name="features" ref="carFeatures"/>
        <property name="colors" ref="carColors"/>
    </bean>
    -->

    <!--Car bean declaration using p schema-->
    <bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car"
          p:brand="Tata"
          p:model="Harrier"
          p:engine-ref="harrierEngine"
          p:specifications-ref="carSpecifications"
          p:features-ref="carFeatures"
          p:colors-ref="carColors"/>
  
</beans>
```