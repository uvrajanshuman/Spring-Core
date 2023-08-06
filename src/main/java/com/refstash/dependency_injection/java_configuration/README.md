# Spring Dependency Injection using Java based configuration

Java-based configuration refers to the approach of configuring the Spring application using Java code rather than XML files.<br> 
It allows to define and configure the beans, dependencies, and other Spring components using Java classes and annotations.

## Bean declaration using `@Bean` annotation and Java configuration file:

**@Bean:** This annotation is used to indicate that the annotated method is responsible for creating bean objects to be managed by the IOC container.<br>
It marks a method as a producer of a bean instance.
The return value of the method represents the bean, and the method name is used as the bean name by default.

It can be treated equivalent to the `<bean/>` element of XML configuration.

**@Configuration:** This annotation is used to indicate that a class serves as a configuration class.
Annotating a class with `@Configuration` indicates that it's primary purpose is bean declaration using `@Bean` annotation.
The `@Bean` definitions in this class can have dependencies on each other (directly call `@Bean` methods to get a dependency instance).


>Note:<br>
> The `@Bean` annotated methods can be declared in any `@Component` annotated class or `@Configuration` annotated class.
> And based on the place of declaration their behaviour differ.
> 
> 1. @Configuration : The declared beans are considered in a "full" mode.
>    - The @Bean methods can have dependencies on other @Bean methods within the same class. 
>    - Spring manages the lifecycle of these @Beans and ensures that they are initialized properly. 
>    - This is the recommended way.
> 
>2. @Component: The declared beans are considered in a "lite" mode.
>    - The @Bean methods can not have dependencies on other @Bean methods of the same class.
>    They can have dependencies on other beans (including @Autowired ones) but cannot refer to other @Bean methods within the same class.
>    - The lifecycle of such beans are not managed by Spring. These can be considered as normal regular object created through factory methods, Spring
>    does not handle their initialization, dependency injection, destruction or even autowiring. But these beans are be eligible for being autowired.

## Bean dependencies and Autowirng

In case Beans have dependencies on one another, expressing that dependency is as simple as having one `@Bean` method call another.<br>
Example: Let's consider BeanB has dependency on BeanA.

```java
//imports

@Configuration
public class AppConfig {

    @Bean
    public BeanA beanA() {
        return new BeanA();
    }

    @Bean
    public BeanB beanB() {
        BeanB beanB = new BeanB();
        // direct @Bean method call to get the desired dependency.
        BeanA dependency = beanA();
        beanB.setBeanA(dependency);
        return BeanB;
    }
}
```
- This way of declaring inter-bean dependencies works only when the `@Bean` methods are declared within a `@Configuration` class. Inter-bean dependencies can not be declared using plain `@Component` classes.

Another way of achieving this by providing the required dependencies as parameter to the `@Bean` methods directly.
Spring will automatically autowire those.<br>
Example:
```java
//imports

@Configuration
public class AppConfig {

    @Bean
    public BeanA beanA() {
        return new BeanA();
    }

    //specifying the dependencies directly as @Bean method params
    @Bean
    public BeanB beanB(BeanA beanA) {
        BeanB beanB = new BeanB();
        beanB.setBeanA(beanA);
        return BeanB;
    }
}
```

## Instantiating the Container
`AnnotationConfigApplicationContext` is used as implementation class for `ApplicationContext`, since the configuration 
file is a Java class.

**@Configuration classes as input:**
```java
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class );
```
AnnotationConfigApplicationContext is not limited to @Configuration classes, 
any @Component classes (or JSR-330 annotated classes) can bes supplied as well.

**Any @Component or equivalent class as input:**
```java
ApplicationContext ctx = new AnnotationConfigApplicationContext(MyServiceImpl.class, Dependency1.class, Dependency2.class);
```

**Programmatic registration of classes**
```java
AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
ctx.register(AppConfig.class, OtherConfig.class);
ctx.register(AdditionalConfig.class);
ctx.refresh();
```

## Bean Declaration in Interfaces
The default methods in interfaces can also be used to define beans.

Complex bean configurations can be created by implementing multiple interfaces with default methods.
And these interfaces can further be composed to create a bean with configuration from different sources.

```java
//imports

// Interface with a default method that defines a bean
public interface ConfigA {
    @Bean
    default BeanA beanA() {
        return new BeanA();
    }
}

// Interface with a default method that defines a bean
public interface ConfigB {
    default BeanB beanB() {
        return new BeanB();
    }
}

// Class implementing both interfaces to compose the bean configurations
@Configuration
public class AppConfig implements ConfigA, ConfigB {
    // No need to implement methods here, as default implementations are provided in the interfaces
}

// Driver class to run Spring context
public class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        
        BeanA beanA = applicationContext.getBean(BeanA.class);
        BeanB beanB = applicationContext.getBean(BeanB.class);
        
        beanA.doSomething();
        beanB.doSomething();
    }
}
```

- Using default methods in interfaces to define beans can help in creating modular and reusable configurations in Spring, promoting code organization and enhancing readability.

## Composing Annotation based configuration metadata

It can be useful to have bean definitions span multiple configuration classes,
as each individual configuration class can represent a logical layer or module in the architecture.

The `@Import` annotation allows to import one or more configuration classes into another configuration class. <br>
It provides a way to modularize and organize the configuration by combining multiple configuration classes into a single
configuration or allowing one configuration class to depend on others.

```java
//imports
@Configuration
public class ConfigA {

    @Bean
    public BeanA beanA() {
        return new BeanA();
    }
}
```
```java
//imports
@Configuration
@Import(ConfigA.class)
public class ConfigB {

    @Bean
    public BeanB beanB() {
        return new BeanB();
    }
}
```

Now, instead of specifying both **ConfigA** and **ConfigB** while instantiating the context, only **ConfigB** can be specified (as it already imports the **ConfigA**)

```java
//imports
public class App{
    public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(ConfigB.class);
    
        // both beansA and beanB are available...
        BeanA a = context.getBean("beanA",BeanA.class);
        BeanB b = context.getBean("beanB",BeanB.class);
    }
}
```

> A configuration class can import several configuration classes like `@Import(value = {ConfigA.class, ConfigB.class, ConfigC.class})`<br>
> And fully qualified class name can also be specified while importing like `@Import("com.refstash.OtherConfig")`

## Complete Example demonstrating Java based Bean and Container configuration:
[_Engine.java_](./Engine.java)
```java
/*
 * Bean class without annotations
 */

public class Engine {
    
    private String type;
    private String displacement;
    private int cylinder;
    private String transmissionType;
    private String gearBox;

    public Engine() {
    }

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

[_Car.java_](./Car.java)
```java
/*
 * Bean class without annotations
 */

//imports
public class Car {
    
    private String brand;
    private String model;
    private Engine engine;
    private Map<String,String> specifications;
    private List<String> features;
    private Set<String> colors;

    public Car() {
    }

    public Car(String brand, String model, Engine engine, Map<String, String> specifications, List<String> features, Set<String> colors) {
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

[_AppConfig.java_](./AppConfig.java)
```java
/*
 * Bean declaration through @Bean
 */

//imports
@Configuration
public class AppConfig {

    @Bean("harrierEngine")
    // @Bean(name = "harrierEngine")
    public Engine getHarrierEngineBean() {
        Engine engine = new Engine();
        engine.setType("Kryotec 2.0 L Turbocharged Engine");
        engine.setDisplacement("1956cc");
        engine.setCylinder(4);
        engine.setTransmissionType("Automatic");
        engine.setGearBox("6-Speed");
        return engine;
    }

    @Bean(name = {"tataHarrier","harrier"})
    public Car getHarrierBean() {
        Car car = new Car();
        car.setBrand("Tata");
        car.setModel("Harrier");
        car.setFeatures(Arrays.asList("Power Steering", "Power Windows", "Air Conditioner", "Heater",
                "Driver and Passenger Airbag", "Automatic climate control"));
        car.setColors(new HashSet<>(Arrays.asList("Orcus White", "Calypso Red", "Daytona Grey", "Oberon Black")));
        Map<String,String> specs = new HashMap<>();
        specs.put("Mileage", "14.6kmpl");
        specs.put("Fuel Type", "Diesel");
        specs.put("Fuel Tank Capacity", "50.0");
        specs.put("Seating Capacity", "5");
        specs.put("Body Type", "SUV");
        car.setSpecifications(specs);
        //injecting engine bean
        car.setEngine(getHarrierEngineBean());
        return car;
    }
}
```
- Beans defined through `@Bean`
- In case Beans have dependencies on one another, expressing that dependency is as simple as having one bean method call another

> By default, the bean name will be the same as the `@Bean` annotated method name.<br>
> But herein custom names and aliases can be provided like: `@Bean("harrierEngine")` or (`@Bean(name="harrierEngine")`) and `@Bean(name = {"tataHarrier","harrier"})`<br>
> The first one provides a custom name to the bean while the latter specifies
> multiple aliases or bean names separated by commas; the bean can be accessed using any one of these.

[_App.java_](./App.java)
```java
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
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

### Externalizing the configuration to a properties file

`@PropertySource` annotation is used to externalize the configurations to an external properties file.<br>
The properties from this external properties file can either be injected through `@Value` annotation or using the `Environment`.
The `Environment` interface in Spring provides a way to access and manage configuration properties. 
It represents the environment in which the application is running and offers methods to retrieve property values.

[AppConfig.java](./AppConfig.java)
```java
/*
 * Bean declaration through @Bean
 */

//imports
@Configuration
@PropertySource("classpath:com/refstash/dependency_injection/java_configuration/application.properties")
public class AppConfig {

    //Externalizing the configuration to a properties file
    @Autowired
    private Environment environment;

    //property injection from application.properties using @Value
    @Value("${engine.type}")
    private String type;
    @Value("${engine.displacement}")
    private String displacement;
    @Value("${engine.cylinder}")
    private int cylinder;
    @Value("${engine.transmissionType}")
    private String transmissionType;
    @Value("${engine.gearBox}")
    private String gearBox;

    @Bean("harrierEngine")
    public Engine getHarrierEngineBean() {
        Engine engine = new Engine();
        engine.setType(type);
        engine.setDisplacement(displacement);
        engine.setCylinder(cylinder);
        engine.setTransmissionType(transmissionType);
        engine.setGearBox(gearBox);
        return engine;
    }

    @Bean(name = {"tataHarrier", "harrier"})
    public Car getHarrierBean() {
        //property injection from application.properties using Environment
        Car car = new Car();
        car.setBrand(environment.getProperty("car.brand"));
        car.setModel(environment.getProperty("car.model"));
        //List
        car.setFeatures(environment.getProperty("car.specifications",ArrayList.class));
        //Set
        car.setColors(environment.getProperty("car.features",HashSet.class));
        //Map
        Map<String,String> specs = new HashMap<>();
        specs.put("Mileage", environment.getProperty("car.specification.Mileage"));
        specs.put("Fuel Type", environment.getProperty("car.specification.Fuel Type"));
        specs.put("Fuel Tank Capacity", environment.getProperty("car.specification.Fuel Tank Capacity"));
        specs.put("Seating Capacity", environment.getProperty("car.specification.Seating Capacity"));
        specs.put("Body Type", environment.getProperty("car.specification.Body Type"));
        car.setSpecifications(specs);
        //reference injection
        car.setEngine(getHarrierEngineBean());
        return car;
    }
}

```
Herein, both the ways are demonstrated: 
for **harrierEngine** bean the `@Value` annotation is used for injection values from properties file.
while in **tataHarrier** bean the `Environment` is used for the same.

[_application.properties_](../../../../../resources/com/refstash/dependency_injection/java_configuration/application.properties)
```properties
# Engine properties
engine.type = Kryotec 2.0 L Turbocharged Engine
engine.displacement = 1956cc
engine.cylinder = 4
engine.transmissionType = Automatic
engine.gearBox = 6-Speed

# Car properties
car.brand = Tata
car.model = Harrier
##list
car.features = Power Steering, Power Windows, Air Conditioner, Heater, \
  Driver and Passenger Airbag, Automatic climate control

##set
car.colors = Orcus White, Calypso Red, Daytona Grey, Oberon Black

car.specifications.Mileage: '14.6kmpl' 
car.specifications.FuelType: 'Diesel'
car.specifications.FuelTankCapacity: 50.0
car.specifications.SeatingCapacity: 5
car.specifications.BodyType: 'SUV'
```

- The output will remain same as previous one.

>**Note:**<br>
>Multiple properties can be included through `@PropertySources` annotation like:
>```java
>@PropertySources({
>  @PropertySource("classpath:configA.properties"),
>  @PropertySource("classpath:configB.properties")
> })
>```
>or multiple `@PropertySource` annotation can be used for the same (`@PropertySource` is repeatable)
>```java
>@PropertySource("classpath:configA.properties")
>@PropertySource("classpath:configB.properties")
>```

>If XML or other `non-@Configuration` bean definition resources needs to be imported, the `@ImportResource` annotation should be used instead.

