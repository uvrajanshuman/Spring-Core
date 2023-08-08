# Asynchronous processing in Spring

`@EnableAsync` and `@Async` annotations enables and facilitates asynchronous method execution in Spring.<br>
They allow asynchronous method execution, meaning the calling thread does not have to wait for the method to complete but continues with its execution.
Instead, the method runs in a separate thread, improving application responsiveness and concurrency.

**@EnableAsync:**<br>
`@EnableAsync` is a meta-annotation that enables the asynchronous execution of methods within the Spring application context.
When `@EnableAsync` is applied to a configuration class, Spring sets up the necessary infrastructure to support asynchronous method invocation,
including creation of the necessary `TaskExecutor` bean.

Example:
```java
@Configuration
@EnableAsync
public class AppConfig {
    // Configuration code
}
```

**@Async:**<br>
`@Async` is a method-level annotation that indicates that a specific method should be executed asynchronously.
When `@Async` is applied to a method, Spring wraps the method's invocation in a separate thread, allowing the method to be executed asynchronously.

Example:
```java
@Service
public class MyService {

    @Async
    public void asyncMethod() {
        // Asynchronous method logic
    }
}
```

### **Requirements for @Async:**
To use `@Async`, the following requirements must be met:

1. **Enable Asynchronous Support:** `@EnableAsync` annotation must be applied on the configuration class to enable
   asynchronous support in the Spring application context.

2. **TaskExecutor Bean:** Spring requires a `TaskExecutor` bean to handle the asynchronous execution of methods.<br>
   If you don't explicitly define one, Spring creates a default `SimpleAsyncTaskExecutor` bean, which is suitable for basic
   asynchronous execution but may not be ideal for production environments.

For more control and flexibility, a custom `TaskExecutor` bean should be defined.

**Configuring a Custom TaskExecutor:**<br>
custom `TaskExecutor` bean can be defined in the configuration class and used along with the `@Async` annotation. <br>
For example:

```java
@Configuration
@EnableAsync
public class AppConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        return executor;
    }
}
```

In this example, custom `ThreadPoolTaskExecutor` bean with specific core pool size, maximum pool size, and queue capacity settings is defined.
The `@EnableAsync` annotation enables asynchronous support, and the `@Async` annotation on a method indicates that it
should be executed asynchronously using the custom `ThreadPoolTaskExecutor` bean.

>**Note:** <br>
> ThreadPoolTaskExecutor
> 
>**1. corePoolSize:** This parameter sets the initial and minimum number of threads in the thread pool.<br> 
   Threads up to this core size are created immediately upon pool initialization and are kept alive even when idle.<br> 
   This ensures there's a responsive pool ready to handle tasks without delay.
>
>**2. maxPoolSize:** This parameter defines the upper limit on the number of threads in the pool.<br> 
   If the workload increases and the queue is full, new threads are created up to this maximum pool size.<br> 
   These threads are temporary and will be released when they become idle for a certain period.<br>
   Once, maxPoolSize limit is reached, no more tasks are accepted.
>
>**3. queueCapacity:** The queue acts as a buffer for tasks when the thread pool is fully utilized.<br> 
   When all threads are busy, new tasks are enqueued in this buffer. <br>
   The queueCapacity specifies the maximum number of tasks that can be held in the queue. <br>
   If this limit is reached, and the thread pool is at its maximum size, new tasks might trigger temporary thread creation until the queue has space.

>The corePoolSize ensures the initial availability of threads, the maxPoolSize caps the total thread count, and the queueCapacity regulates the task buffering when the thread pool is saturated. 
>This combination optimizes resource usage and responsiveness in handling tasks.


[Complete sample example for asynchronous execution can be found here](../asynchronous_processing)

>**Note:** <br>
It's important to avoid using `@Async` on methods that have internal dependencies within the same class,
as asynchronous execution can lead to unexpected behavior due to the method's state being accessed by different threads simultaneously.
Additionally, the return type of an asynchronous method should generally be `void` or `Future<T>`, where `T` is the result type of the asynchronous operation.
