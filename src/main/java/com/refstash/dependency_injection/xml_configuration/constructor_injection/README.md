# Constructor Injection

The dependencies are injected through the constructor of the bean.<br>
The constructor injection can be achieved using either `<constructor-arg>` tag or `c` schema.

## Injecting primitive types
The `<constructor-arg>` element should be used to specify the value directly.

Attributes like `name`,`type`,`index` are optional, but can be used to be more specific and avoid ambiguity.
If these are not defined, the parameters can be passed sequentially as per the bean class constructor declaration.

Ex:
```xml
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.constructor_injection.Car">
    <!--injecting primitive type (string)-->
    <constructor-arg name="brand" value="Tata"/>
    <!--car bean other properties declarations-->
</bean>
```
or
```xml
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.constructor_injection.Car">
    <!--injecting primitive type (string)-->
    <constructor-arg>
        <value>Tata</value>
    </constructor-arg>
    <!--car bean other properties declarations-->
</bean>
```
The above example specifies **Tata** as value for the **brand** parameter of the bean's constructor of the Car class (tataHarrier).

## Injecting reference types
The `<constructor-arg>` element should be used to specify the constructor parameter and the `ref` attribute should be used to refer to another bean by its identifier (`id` or `name`).

Ex:
```xml
<!--Engine bean-->
<bean id="harrierEngine" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Engine">
    <!--engine bean property declarations-->
</bean>

<!--Car bean-->
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car">
    <!--injecting reference type-->
    <constructor-arg name="engine" ref="harrierEngine"/>
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
    <constructor-arg name="engine">
      <ref bean="harrierEngine"/>
    </constructor-arg>
    <!--car bean other properties declarations-->
</bean>
```
The above example shows constructor injection of a bean (harrierEngine) of Engine class into the constructor's parameter (engine) of another bean (tataHarrier) of car class.

Another way would be inline bean declaration of dependency references.

### Inner Beans (Inline declaration of Beans)
A `<bean/>` element inside the `<property/>` or `<constructor-arg/>` elements defines an inner bean.

Example:
```xml
<bean id="outer" class="...">
	<!-- instead of using a reference to a target bean, simply define the target bean inline -->
	<constructor-arg name="target">
		<bean class="com.example.Person"> <!-- this is the inner bean -->
			<property name="name" value="Fiona Apple"/>
			<property name="age" value="25"/>
		</bean>
	</constructor-arg>
</bean>
```
- An inner bean definition does not require a defined ID or name.
  If specified, the container does not use such a value as an identifier.
- The container also ignores the scope flag on creation, because inner beans are always anonymous and are always created with the outer bean.
- It is not possible to access inner beans independently or to inject them into collaborating beans other than into the enclosing bean.

## Injecting collections
For injecting collections, such as lists, sets, or maps, `<list>`, `<set>`, or `<map>` elements can be used respectively within the `<constructor-arg>` element.

**List:**

```xml
<!--Car bean-->
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car">
    <!--injecting list-->
    <constructor-arg name="features">
        <list>
            <value>Power Steering</value>
            <value>Power Windows</value>
            <value>Air Conditioner</value>
            <value>Heater</value>
            <value>Driver and Passenger Airbag</value>
            <value>Automatic climate control</value>
        </list>
    </constructor-arg>
  <!--car bean other properties declarations-->
</bean>
```
The above example shows injection of list specified using `<list>` tag into **features** parameter of bean's constructor of Car class (tataHarrier).

**Set:**

```xml
<!--Car bean-->
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car">
    <!--injecting set-->
    <constructor-arg name="colors">
        <set>
            <value>Orcus White</value>
            <value>Calypso Red</value>
            <value>Daytona Grey</value>
            <value>Oberon Black</value>
        </set>
    </constructor-arg>
    <!--car bean other properties declarations-->
</bean>
```
The above example shows injection of set specified using `<set>` tag into **colors** parameter of bean's constructor of Car class (tataHarrier).


**Map:**

```xml
<!--Car bean-->
<bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.setter_injection.Car">
    <!--injecting map-->
    <constructor-arg name="specifications">
        <map>
            <entry key="Mileage" value="14.6kmpl"/>
            <entry key="Fuel Type" value="Diesel"/>
            <entry key="Fuel Tank Capacity" value="50.0"/>
            <entry key="Seating Capacity" value="5"/>
            <entry key="Body Type" value="SUV"/>
        </map>
    </constructor-arg>
    <!--car bean other properties declarations-->
</bean>
```
The above example shows injection of map specified using `<map>` tag into **specifications** parameter of bean's constructor of Car class (tatHarrier).


## Complete Example demonstrating constructor injection of different types of values:

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

    public Car(String brand, String model, Engine engine, 
               Map<String, String> specifications, List<String> features, Set<String> colors) {
        this.brand = brand;
        this.model = model;
        this.engine = engine;
        this.specifications = specifications;
        this.features = features;
        this.colors = colors;
    }

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
//imports
public class Engine {
    
    private String type;
    private String displacement;
    private int cylinder;
    private String transmissionType;
    private String gearBox;

    public Engine(String type, String displacement, int cylinder, String transmissionType, String gearBox) {
        this.type = type;
        this.displacement = displacement;
        this.cylinder = cylinder;
        this.transmissionType = transmissionType;
        this.gearBox = gearBox;
    }

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

[_config.xml_](../../../../../../../main/resources/com/refstash/dependency_injection/xml_configuration/constructor_injection/config.xml)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:c="http://www.springframework.org/schema/c"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd">

    <!--bean declarations-->
    <!--Engine bean-->
    <bean id="harrierEngine" class="com.refstash.dependency_injection.xml_configuration.constructor_injection.Engine">
        <constructor-arg name="type" value="Kryotec 2.0 L Turbocharged Engine"/>
        <constructor-arg name="displacement" value="1956cc"/>
        <constructor-arg name="cylinder" value="4"/>
        <constructor-arg name="transmissionType" value="Automatic"/>
        <constructor-arg name="gearBox" value="6-Speed"/>
    </bean>

    <!-- constructor injection using c schema -->
    <!--
    <bean id="harrierEngine" class="com.refstash.dependency_injection.xml_configuration.constructor_injection.Engine"
          c:type="Kryotec 2.0 L Turbocharged Engine"
          c:displacement="1956cc"
          c:cylinder="4"
          c:transmissionType="Automatic"
          c:gearBox="6-Speed"/>
     -->

    <!--Car bean-->
    <bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.constructor_injection.Car">
        <constructor-arg name="brand" value="Tata"/>
        <constructor-arg name="model" value="Harrier"/>
        <constructor-arg name="engine" ref="harrierEngine"/>
        <constructor-arg name="specifications">
            <map>
                <entry key="Mileage" value="14.6kmpl"/>
                <entry key="Fuel Type" value="Diesel"/>
                <entry key="Fuel Tank Capacity" value="50.0"/>
                <entry key="Seating Capacity" value="5"/>
                <entry key="Body Type" value="SUV"/>
            </map>
        </constructor-arg>
        <constructor-arg name="features">
            <list>
                <value>Power Steering</value>
                <value>Power Windows </value>
                <value>Air Conditioner</value>
                <value>Heater</value>
                <value>Driver and Passenger Airbag</value>
                <value>Automatic climate control</value>
            </list>
        </constructor-arg>
        <constructor-arg name="colors">
            <set>
                <value>Orcus White</value>
                <value>Calypso Red</value>
                <value>Daytona Grey</value>
                <value>Oberon Black</value>
            </set>
        </constructor-arg>
    </bean>
</beans>
```
- The constructor injection can also be done using the `c` schema instead of `<constructor-arg>` tag. 
(Ex: commented `harrierEngine` bean declaration in above example).

[_App.java_](./App.java)
```java
//imports
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/refstash/dependency_injection/xml_configuration/constructor_injection/config.xml");
        Car hondaCivic = context.getBean("tataHarrier", Car.class);
        System.out.println(hondaCivic);
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

To make standalone collection util schema must be added to XML configuration.

The xml configuration used in above example can be modified to use standalone collections:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:c="http://www.springframework.org/schema/c"
    xmlns:util="http://www.springframework.org/schema/util"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd
    http://www.springframework.org/schema/util
    http://www.springframework.org/schema/util/spring-util.xsd">
    
    <!-- bean definitions here -->

    <!-- Engine bean declaration using p schema -->
    <bean id="harrierEngine" class="com.refstash.dependency_injection.xml_configuration.constructor_injection.Engine"
        c:type="Kryotec 2.0 L Turbocharged Engine"
        c:displacement="1956cc"
        c:cylinder="4"
        c:transmissionType="Automatic"
        c:gearBox="6-Speed"/>

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
    <bean id="harrierEngine" class="com.refstash.dependency_injection.xml_configuration.constructor_injection.Engine">
        <constructor-arg name="brand" value="Tata"/>
        <constructor-arg name="model" ref="Harrier"/>
        <constructor-arg name="engine" ref="harrierEngine"/>
        <constructor-arg name="specifications" ref="carSpecifications"/>
        <constructor-arg name="features" ref="carFeatures"/>
        <constructor-arg name="colors" ref="carColors"/>
    </bean>
    -->

    <!--Car bean declaration using c schema-->
    <bean id="tataHarrier" class="com.refstash.dependency_injection.xml_configuration.constructor_injection.Car"
          c:brand="Tata"
          c:model="Harrier"
          c:engine-ref="harrierEngine"
          c:specifications-ref="carSpecifications"
          c:features-ref="carFeatures"
          c:colors-ref="carColors"/>
</beans>
```