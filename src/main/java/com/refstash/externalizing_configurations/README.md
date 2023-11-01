# Externalizing the Configurations in Spring

In Spring applications, externalizing configurations is a crucial practice to keep application settings separate from the codebase. <br>
This approach enables you to modify configuration values without altering the application's source code, allowing for greater flexibility and easier maintenance.

## _@Value_ Annotation

The `@Value` annotation allows to inject values from properties files, environment variables, or command-line arguments directly into Spring bean's fields.
And it can be applied at the field or constructor/method parameter level.
This is a convenient way to externalize configurations on a per-bean basis.

It can be used to inject values directly:
```java
@Value("Hello, Spring!")
private String greeting;
```
Or fetch values from system properties or external property sources (These properties can be accessed using `${propertyKey}` notion.):
```java
@Value("${app.name}")
private String appName;
```

Additionally, default values can also be set to be applied when a specified property is undefined:
```java
@Value("${timeout:3000}")
private int connectionTimeout;
```
> The external properties file should be specified either through `@PropertySource` or xml equivalent `<context:property-placeholder/>`.

When `@Value` contains a SpEL expression the value will be dynamically computed at runtime.

**Collection declaration and injection:**
1. **Injecting List/Array:**<br>
   The lists or array can be expressed directly in the properties file separated by commas (`,`). <br>
   Consider the following list declaration and its injection.
   ```properties
   car.features = Power Steering, Power Windows, Air Conditioner, Heater \
     Driver and Passenger Airbag, Automatic climate control
   ```
   The list can be injected directly
   ```java
   @Value("${car.features}")
   private List<String> features;
   ```

2. **Injecting Map:**<br>
   The Maps can be expressed directly in the properties file using `{key:value}` notation.<br>
   Consider the following map declaration and its injection.
   ```properties
   ##Map
   car.specifications ={'Mileage': '14.6kmpl', 'Fuel Type': 'Diesel', 'Fuel Tank Capacity': 50.0, \
     'Seating Capacity': 5, 'Body Type': 'SUV'}
   ```
   The Map can be injected through SpEL.
   ```java
   @Value("#{${car.specifications}}")
   private Map<String,String> specifications;
   ```

3. **Injecting Set:**<br>
   Consider the following set declaration and its injection.
   ```properties
   ##set
   car.colors = Orcus White, Calypso Red, Daytona Grey, Oberon Black
   ```
   The set can be injected through SpEL
   ```java
   @Value("#{'${car.colors}'.split(',')}")
   private Set<String> colors;
   ```

[A complete working example can be found here](../dependency_injection/annotation_configuration/README.md#complete-example-demonstrating-annotation-based-bean-definition)

## Spring Expression Language (SpEL)


SpEL (Spring Expression Language) is a powerful expression language that provides a way to evaluate and process expressions at runtime in the Spring Framework. 
It is widely used for querying and manipulating object graphs within the Spring ecosystem. 

SpEL allows you to access properties, methods, and fields of objects, perform mathematical calculations, and evaluate conditional expressions.

[Spring Expression Language](./spring_expression_language/README.md)

## Environment

The `Environment` interface provides a powerful way to access properties and profiles. 
It allows you to programmatically access configuration values, which can be particularly useful for dynamic scenarios.

Example: Fetching database configurations from the **app.properties** file and linking them to the DataSourceConfig class through the Environment interface.
```java
@Configuration
@PropertySource("classpath:app.properties")
public class DatabaseConfig implements InitializingBean {

    @Autowired
    Environment env;

    @Override
    public void afterPropertiesSet() throws Exception {
        setDatabaseConfig();
    }

    private void setDatabaseConfig() {
        DataSourceConfig config = new DataSourceConfig();
        config.setDriver(env.getProperty("jdbc.driver"));
        config.setUrl(env.getProperty("jdbc.url"));
        config.setUsername(env.getProperty("jdbc.username"));
        config.setPassword(env.getProperty("jdbc.password"));
    }
}
```

## Profiles
The Profiles allow conditional bean registration based on specific environments.

[Profiles](./profiles/README.md)

## _@PropertySource_ Annotation

The `@PropertySource` annotation is used to declare the source of externalized configuration properties. 
It's often used in conjunction with Java configuration classes.

```java
@Configuration
@PropertySource("classpath:app.properties")
public class AppConfig {

    @Autowired
    private Environment environment;

    @Bean
    public MyBean myBean() {
        MyBean bean = new MyBean();
        bean.setAppTitle(environment.getProperty("app.title"));
        return bean;
    }
}
```
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

[A complete working example can be found here](../dependency_injection/java_configuration/README.md#externalizing-the-configuration-to-a-properties-file)

## _@ImportResource_ Annotation

The `@ImportResource` annotation, when applied to a configuration class, indicates that additional bean definitions will be loaded from the specified XML configuration file.<br> 
The imported beans can then be accessed and utilized in the Java code.

It helps to seamlessly integrate XML-based bean definitions into a Java-based Spring configuration, 
allowing you to utilize both configuration styles within a single application.

[@ImportResource](../dependency_injection/java_configuration/README.md#integrating-xml-based-bean-definitions-into-a-java-based-configuration-using-importresource-annotation)
