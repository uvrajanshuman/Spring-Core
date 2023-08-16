# Spring Profiles

In Spring, **Profiles** offer a versatile way to configure and manage beans based on different application contexts or environments. <br>
The `@Profile` annotation allows to define that certain beans should be registered and used only when specific profiles are active. 
This powerful feature enables to maintain separate configurations for different scenarios/environments,
such as development, testing, or production environments.

## Defining Profile-Specific Beans

Beans specific to certain profiles can be created using the `@Profile` annotation.

For instance, suppose there is a requirement to have database configuration that varies between development and production environments. 
Then separate configurations can be created for each profile:

```java
@Component("dataSource")
@Profile("development")
public class LocalDataSourceConfig {
    // Configuration for development profile
    // ...
    
}

@Component("dataSource")
@Profile("production")
public class CloudDataSourceConfig {
    // Configuration for production profile
    // ...
    
}
```

## Profile Expression Logic

Profiles can be specified using a simple profile name (ex: "production") or a more complex profile expression.

The expression can include logical operators such as `!` (logical NOT), `&` (logical AND), and `|` (logical OR). 
This allows for flexible conditions like `production & ind`. (In this case the specified bean will be active only when both the profiles are active)

It's important to note that when using `&` and `|` operators together, you need to enclose expressions in parentheses to ensure proper evaluation, 
for example, `production & (us-east | ind)`.

## Using Composed @Profile Annotation

Custom composed annotations can be created using the `@Profile` annotation. This simplifies the annotation usage and makes the code more readable. 
Here's an example where a custom `@Production` annotation is created as a meta-annotation:

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile("production")
public @interface Production {
}
```
> The `@Production` annotation will be equivalent to `@Profile("production")`

## Profile at Method Level

`@Profile` can also be applied at the method level within a configuration class. 
This allows to define beans that would be registered only when specific profiles are active.

Example: Alternative variants of a particular bean based on environment.

```java
@Configuration
public class AppConfig {

	@Bean("dataSource")
	@Profile("development") 
	public DataSource localDataSource() {
		// Configuration for development profile
		// ...
	}

	@Bean("dataSource")
	@Profile("production") 
	public DataSource cloudDataSourceConfig() {
		// Configuration for production profile
		// ...
	}
}
```
- The `localDataSource` method would be available only in the **development** profile.
- The `cloudDataSourceConfig` method would be available only in the **production** profile.

>**Note:**<br>
> When the entire `@Configuration` class is marked with `@Profile`, it means that the bean methods and `@Import` annotations within that class will
> only be processed and registered by Spring if the specified profiles are active in the application context.

## XML Configuration with Profiles

The XML counterpart would be using the `profile` attribute of the `<beans>` element. 

You can define separate XML files for different profiles, each containing their respective configurations. <br>
For example:

```xml
<beans profile="development">
	<!-- Development profile configuration -->
</beans>

<beans profile="production">
	<!-- Production profile configuration -->
</beans>
```

It is also possible to avoid this split declaration of beans and nest `<beans/>` elements within the same file.<br>
For example:

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="...">

	<!-- other bean definitions -->

    <!-- Profile specific bean declaration (must be last declarations of file) -->
	<beans profile="development">
      <!-- Development profile configuration -->
	</beans>

	<beans profile="production">
      <!-- Production profile configuration -->
	</beans>
</beans>
```
- But such profile based declaration in same file is allowed only as the last declaration in the file. (no beans should be declared after this, although declaring beans before this is allowed)
- The XML counterpart does not support the profile expressions described earlier. It is possible, however, to negate a profile by using the `!` operator.

## Activating Profiles

The profiles can be activated programatically using the `Environment` API or declaratively through system properties, environment variables etc.

```java
AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
ctx.getEnvironment().setActiveProfiles("development");
ctx.register(AppConfig.class);
ctx.refresh();
```
Multiple profiles can be activated at once.
Programmatically, multiple comma seperated profile names can be provided to the `setActiveProfiles()` method, which accepts `String ` varargs. 


## Default Profile
The `default` profile represents the profile that is enabled by default. 

```java
@Configuration
public class DefaultDataConfig {

  @Bean
  @Profile("default")
  public DataSource dataSource() {
      // Configuration for default profile
      // ...
	}
    
    // ...
}
```

If no profile is active, this dataSource will be created. 

This can be treated as a way to specify the default implementations/definitions. If any profile is active, the default profile does not apply.

> The default profile name can be changed programmatically using `setDefaultProfiles()` on the Environment API, or
> declaratively using the `spring.profiles.default` property


[Complete working example demonstrating the use of @Profile can be found here: ](../profiles)