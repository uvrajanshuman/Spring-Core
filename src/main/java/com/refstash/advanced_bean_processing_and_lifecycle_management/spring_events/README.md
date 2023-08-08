# Spring Events

Events are occurrences or changes that can happen within the application, such as user actions, system events, 
or custom events triggered by the application's components.
Event handling is a crucial aspect of software development, allowing applications to respond to specific occurrences or 
changes within their environment. 

The Spring Framework, provides robust support for event-driven programming. 


## Why Event Handling?
Event handling is essential for building responsive and loosely-coupled applications. 
It enables components to communicate and react to changes efficiently, making code more maintainable and extensible. 

By using events, developers can effectively implement the Observer pattern, where multiple observers (listeners) react 
to changes in a single subject (event publisher). 
This separation of concerns enhances code modularity and encourages best practices like the Single Responsibility Principle.

[**Handling Events Manually (Without a Framework) using the observer design pattern**](observer_design_pattern/README.md)

While this approach works for small applications, it becomes complex as the application grows. 
Adding new listeners or removing existing ones requires modifying the subject/publisher class, that leads to tight coupling.

## Spring Framework Event Handling
Spring Framework offers a more elegant and robust approach to event handling. 
It provides an event system where components can publish events, and other components can listen to and react accordingly. 

Herein, the publisher does not need handle the subscribers/observers. It just publishes the event and all the concerned
observers are notified automatically.

The custom event handling in spring can be of two types based on the listener/observer implementation:
1. ApplicationListener
2. @EventListener

### Listeners/Observers implementing ***ApplicationListener***

1. Create the Event Class:
   Define the custom event class `FamilyMan` that extends `ApplicationEvent`, representing the change event.
   <br><br>
   _FamilyMan (Event)_ : [FamilyMan.java](applicationListener_implementation/event/FamilyMan.java)
   ```java
   //imports
   
   public class FamilyMan extends ApplicationEvent {
   
      public static String showName = "family-man";
   
      private String episodeNo;
   
      public FamilyMan(Object source) {
         super(source);
      }
   
      public FamilyMan(Object source, String episodeNo) {
         super(source);
         this.episodeNo = episodeNo;
      }
   
      public String getShowName() {
         return showName;
      }
   
      public String getEpisodeNo() {
         return episodeNo;
      }
   
   }
   ```
   > Here the `source` parameter in constructor represents the source of the event (the source/creator of that event).

2. Create the Event Publisher:
   Next, the `AmazonPrime` will act as the event publisher, and the `ApplicationEventPublisher` needs to be injected to publish the events.
<br><br>
   _AmazonPrime (Event Publisher)_ : [AmazonPrime.java](applicationListener_implementation/publisher/AmazonPrime.java)
   ```java
   //imports 
   
   @Service
   public class AmazonPrime {
   
      @Autowired
      private ApplicationEventPublisher applicationEventPublisher;
   
      public AmazonPrime() {
      }
   
      public void broadcast(FamilyMan episode) {
         applicationEventPublisher.publishEvent(episode);
      }
   }
   ```

3. Implement Event Listeners:
   Now, Create event listener beans that implements `ApplicationListener<FamilyMan> ` to respond to the `FamilyMan` event.
   <br><br>
   _PrimeSubscriber (Event Listener)_ : [PrimeSubscriber.java](applicationListener_implementation/subscriber/PrimeSubscriber.java)
   ```java
   //imports 
   
   public class PrimeSubscriber implements ApplicationListener<FamilyMan> {
      private String name;
   
      public void setName(String name) {
         this.name = name;
      }
   
      public String getName() {
         return this.name;
      }
   
      @Override
      public void onApplicationEvent(FamilyMan event) {
         System.out.println("Hi "+ name
                 + ": New episode of " + event.getShowName()
                 + " is available (Episode " + event.getEpisodeNo() + ")");
      }
   
   }
   ```

   _AppConfig (Configuration and Subcriber Beans declarations)_ : [AppConfig.java](applicationListener_implementation/AppConfig.java)
   ```java
   @Configuration
   @ComponentScan
   public class AppConfig {
       
       //Event Listener Beans
      
       @Bean("alice")
       public PrimeSubscriber aliceSubscriber() {
           PrimeSubscriber primeSubscriber = new PrimeSubscriber();
           primeSubscriber.setName("Alice");
           return primeSubscriber;
       }
   
       @Bean("bob")
       public PrimeSubscriber bobSubscriber() {
           PrimeSubscriber primeSubscriber = new PrimeSubscriber();
           primeSubscriber.setName("Bob");
           return primeSubscriber;
       }
   }
   ```

4. Create and Publish the event:
   Now, create the `FamilyMan` event and publish it through `AmazonPrime` publisher. All the concerned Observers (`Alice`,`Bob`)
   will be notified automatically.
   <br><br>
   _App (Driver class)_ : [App.java](applicationListener_implementation/App.java)
   ```java
   public class App {
       public static void main(String[] args) {
           AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
           AmazonPrime prime = applicationContext.getBean("amazonPrime",AmazonPrime.class);
           prime.broadcast(new FamilyMan(applicationContext,"S2 : EP11"));
       }
   }
   ```

   Output:
   ```shell
   Hi Alice: New episode of family-man is available (Episode S2 : EP11)
   Hi Bob: New episode of family-man is available (Episode S2 : EP11)
   ```

One major drawback of this approach is it does not directly support listening to multiple events, and the listeners can't
directly choose to listen to a certain event and not listen to another event published by the same publisher.

[Complete working code for this approach can be found here](./applicationListener_implementation)

### Modified ***ApplicationListener*** Approach to allow listening to multiple events

1. Create the Events:
   Define the custom event class `FamilyMan` and `Panchayat`.<br>
   But, instead of creating them as individual event, they must be generalized as a single event `Episode` that extends `ApplicationEvent`, 
   and represents the change event.

   This will ensure that the listeners can listen to both the events by observing the `Episode` event, and can further filter out
   based on the actual event instance.
   <br><br>
   _Episode (Generic Event)_ : [Episode.java](applicationListener_modified_implementation/event/Episode.java)
   ```java
   public abstract class Episode extends ApplicationEvent{
       
       public Episode(Object source) {
           super(source);
       }
       
       public abstract String getShowName();
       public abstract String getEpisodeNo();
   }
   ```

   _FamilyMan (Event)_ : [FamilyMan.java](applicationListener_modified_implementation/event/FamilyMan.java)
   ```java
   public class FamilyMan extends Episode {
   
       public static final String showName = "family-man";
   
       private final String episodeNo;
   
       public FamilyMan(Object source, String episodeNo) {
           super(source);
           this.episodeNo = episodeNo;
       }
   
       public String getShowName() {
           return showName;
       }
   
       public String getEpisodeNo() {
           return episodeNo;
       }
   
   }
   ```

   _Panchayat (Event)_ : [Panchayat.java](applicationListener_modified_implementation/event/Panchayat.java)
   ```java
   public class Panchayat extends Episode {
   
       public static final String showName = "panchayat";
   
       private final String episodeNo;
   
       public Panchayat(Object source, String episodeNo) {
           super(source);
           this.episodeNo = episodeNo;
       }
   
       @Override
       public String getShowName() {
           return showName;
       }
   
       public String getEpisodeNo() {
           return episodeNo;
       }
   
   }
   ```

2. Create the Event Publisher:
   Next, the `AmazonPrime` will act as the event publisher, and the `ApplicationEventPublisher` needs to be injected to publish the events.
   It will be publishing both the generic event `Episode` that may represent `FamilyMan` or `Panchayat`.
   <br><br>
   _Broadcaster (Generic Interface for Episode Publishers)_ : [Broadcaster.java](applicationListener_modified_implementation/publisher/Broadcaster.java)
   ```java
   public interface Broadcaster {
       void broadcast(Episode episode);
   }
   ```
   _AmazonPrime (Event Publisher)_ : [AmazonPrime.java](applicationListener_modified_implementation/publisher/AmazonPrime.java)
   ```java
   @Service
   public class AmazonPrime implements Broadcaster {
   
       @Autowired
       private ApplicationEventPublisher applicationEventPublisher;
   
       public AmazonPrime() {
       }
   
       @Override
       public void broadcast(Episode episode) {
           applicationEventPublisher.publishEvent(episode);
       }
   }
   ```

3. Implement the Event Listeners:
   Now, Create event listener beans that implements `ApplicationListener<Episode> ` to respond to the `Episode` event.

   Here, `ApplicationListener` is set to listen to the generic event `Episode`, this way it will be able to listen to 
   both the child events `FamilyMan` and `Panchayat`.<br>
   The published event is then filtered out based on it's identifier (name in this case).
   <br><br>
   _PrimeSubscriber (Event Listener)_ : [PrimeSubscriber.java](applicationListener_modified_implementation/subscriber/PrimeSubscriber.java)
   ```java
   public class PrimeSubscriber implements ApplicationListener<Episode>{
       private String name;
       private Set<String> subscribedShows;
   
       public void setName(String name) {
           this.name = name;
       }
   
       public String getName() {
           return this.name;
       }
   
       public void setSubscribedShows(Set<String> subscribedShows){
           this.subscribedShows = subscribedShows;
       }
   
       @Override
       public void onApplicationEvent(Episode event) {
           //filtering out the published event
           if(subscribedShows.contains(event.getShowName())) {
                System.out.println("Hi "+ name
                   + ": New episode of " + event.getShowName()
                   + " is available (Episode " + event.getEpisodeNo() + ")");
            }
       }
   
   }
   ```
   
   Creating two subscribers where one is attached to both the events, while the other is attached to only one event.
<br><br>
   _AppConfig (Configuration and Subscriber Beans declarations)_: [AppConfig.java](applicationListener_modified_implementation/AppConfig.java)
   ```java
   @Configuration
   @ComponentScan
   public class AppConfig {
   
       @Bean("alice")
       public PrimeSubscriber aliceSubscriber() {
           PrimeSubscriber primeSubscriber = new PrimeSubscriber();
           primeSubscriber.setName("Alice");
           Set<String> shows = new HashSet<>();
           shows.add("family-man");
           shows.add("panchayat");
           primeSubscriber.setSubscribedShows(shows);
           return primeSubscriber;
       }
   
       @Bean("bob")
       public PrimeSubscriber bobSubscriber() {
           PrimeSubscriber primeSubscriber = new PrimeSubscriber();
           primeSubscriber.setName("Bob");
           Set<String> shows = new HashSet<>();
           shows.add("family-man");
           primeSubscriber.setSubscribedShows(shows);
           return primeSubscriber;
       }
   }
   ```

4. Create and Publish the event:
   Now, create the `FamilyMan`  and `Panchayat` events and publish it through `AmazonPrime` subscriber. 

   All the subscribers/observers of those specified events will be notified automatically.
<br><br>
   _App (Driver class)_: [App.java](applicationListener_modified_implementation/App.java)
   ```java
   public class App {
       public static void main(String[] args) {
           AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
           AmazonPrime prime = applicationContext.getBean("amazonPrime",AmazonPrime.class);
   
           prime.broadcast(new Panchayat(applicationContext,"s1:01"));
           prime.broadcast(new FamilyMan(applicationContext,"s2:11"));
   
       }
   }
   ```

   Output:
   ```shell
   Hi Alice: New episode of panchayat is available (Episode s1:01)
   Hi Alice: New episode of family-man is available (Episode s2:11)
   Hi Bob: New episode of family-man is available (Episode s2:11)
   ```
   [Complete working code for this approach can be found here](./applicationListener_implementation)


### Annotation based Listeners/Observers _@EventListener_

1. Create the Events :
   Define the custom event class `FamilyMan` and `Panchayat`.
   These are plain Java classes, no need to extend `ApplicationEvent`.
   <br><br>
   _FamilyMan (Event)_ : [FamilyMan.java](annotation_based_listeners/approach1/event/FamilyMan.java)
   
   ```java
   public class FamilyMan {
   
      public static final String showName = "family-man";
   
      private final String episodeNo;
   
      public FamilyMan(String episodeNo) {
         this.episodeNo = episodeNo;
      }
   
      public String getShowName() {
         return showName;
      }
   
      public String getEpisodeNo() {
         return episodeNo;
      }
   
   }
   ```
   
   _Panchayat (Event)_ : [Panchayat.java](annotation_based_listeners/approach1/event/Panchayat.java)
   ```java
   public class Panchayat {
   
       public static final String showName = "panchayat";
   
       private final String episodeNo;
   
       public Panchayat(String episodeNo) {
           this.episodeNo = episodeNo;
       }
   
       public String getShowName() {
           return showName;
       }
   
       public String getEpisodeNo() {
           return episodeNo;
       }
   
   }
   ```
   
2. Create the Event Publisher:
   Next, the `AmazonPrime` will act as the event publisher, and the `ApplicationEventPublisher` needs to be injected to publish the events.
   <br><br>
   _AmazonPrime (Event Publisher)_ : [AmazonPrime.java](annotation_based_listeners/approach1/publisher/AmazonPrime.java)
   ```java
   @Service
   public class AmazonPrime {
   
       @Autowired
       private ApplicationEventPublisher applicationEventPublisher;
   
       public void broadcastFamilyMan(FamilyMan episode) {
           applicationEventPublisher.publishEvent(episode);
       }
   
       public void broadcastPanchayat(Panchayat episode) {
           applicationEventPublisher.publishEvent(episode);
       }
   }
   ```

3. Implement Event Listeners:
   Now, Create event listener beans to respond to the event.
   There is no need to implement the `ApplicationListener<Event>` in this case.

   The listener methods can directly be annotated with `@EventListener` annotation. They must have the desired event in
   their parameter. As soon as the publisher publishes the event the concerned listener with appropriate event parameter will
   be invoked automatically.
   <br><br>
   _AliceSubscriber (Panchayat and FamilyMan Event Listener)_ : [AliceSubscriber.java](annotation_based_listeners/approach1/subscriber/AliceSubscriber.java)
   ```java
   @Component
   public class AliceSubscriber {
       private String name = "Alice";
   
       @EventListener
       public void handlePanchayatEvent(Panchayat event) {
           System.out.println("Hi "+ name
                   + ": New episode of " + event.getShowName()
                   + " is available (Episode " + event.getEpisodeNo() + ")");
       }
   
       @EventListener
       public void hadleFamilyManEvent(FamilyMan event) {
           System.out.println("Hi "+ name
                   + ": New episode of " + event.getShowName()
                   + " is available (Episode " + event.getEpisodeNo() + ")");
       }
   
   }
   ```

   _BobSubscriber (Panchayat Event Listener)_ : [AliceSubscriber.java](annotation_based_listeners/approach1/subscriber/BobSubscriber.java)
   ```java
   @Component
   public class BobSubscriber {
       private String name = "Bob";
   
       @EventListener
       public void handlePanchayatEvent(Panchayat event) {
           System.out.println("Hi "+ name
                   + ": New episode of " + event.getShowName()
                   + " is available (Episode " + event.getEpisodeNo() + ")");
       }
   
   
   }
   ```
   _AppConfig (Configuration file)_ : [Appconfig.java](annotation_based_listeners/approach1/AppConfig.java)
   ```java
   @Configuration
   @ComponentScan
   public class AppConfig {
   }
   ```
4. Create and Publish the event:
   Now, create the `FamilyMan` and `Panchayat` event and publish it through `AmazonPrime` publisher.
   All the subscribers/observers of those specified events will be notified automatically.
   <br><br>
   _App (Driver class)_ : [App.java](applicationListener_implementation/App.java)
   ```java
   public class App {
       public static void main(String[] args) {
           ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
           AmazonPrime amazonPrime = applicationContext.getBean("amazonPrime", AmazonPrime.class);
   
           amazonPrime.broadcastFamilyMan(new FamilyMan("S1 : EP10"));
           amazonPrime.broadcastPanchayat(new Panchayat("S2 : EP05"));
       }
   }
   ```

   Output:
   ```shell
   Hi Alice: New episode of family-man is available (Episode S1 : EP10)
   Hi Alice: New episode of panchayat is available (Episode S2 : EP05)
   Hi Bob: New episode of panchayat is available (Episode S2 : EP05)
   ```
   
> So annotating a listener method with `@EventListener` is sufficient to respond to the event changes, as long as the 
> listener method has that event in its parameter.

[Complete working code for this approach can be found here](./annotation_based_listeners/approach1)

>**Note:**<br>
>Instead of creating separate classes for each of the listeners (Alice,Bob, etc.), multiple instances of a single Listener class
>(each representing separate listeners) can be created in the `@Configuration` class using the `@Bean` annotation 
>like previous [example](#modified-approach-to-allow-listening-to-multiple-events).<br>
>But, in that case instead of specific events (like: FamilyMan, Panchayat) a generic event (parent of all the events, Ex: Episode) 
>should be published and the `@EventListener` annotated listener methods should accept this generic event and further filter it out 
>like we did in one of the previous [example](#modified-approach-to-allow-listening-to-multiple-events).
> 
>[Complete working code for this approach can be found here](./annotation_based_listeners/approach2)


## Listening to multiple events using ***@EventListener***
If the listener method should listen to several events, all the event types should be specified on the annotation itself
and the method should not have any specific event as its parameter (no-argument).

In this case, the actual event instance is not accessible to the listener method. The listener method is invoked if any of the 
specified events occur.

Example: This listener will listen to both `FamilyMan` and `Panchayat` event but won't have access to any of those event instances.
```java
@EventListener({FamilyMan.class, Panchayat.class})
public void hadleEvent(){
    // listener implementation ...
}
```

## Ordering the listeners

In Spring, there is no specific order in which the event listeners are inked when an event is published.
However, Spring allows to control the order of execution of these listeners using `@Order` annotation.
It helps to define the listener execution sequence explicitly. 
Listeners with lower order values will be executed before those with higher order values. 
If multiple listeners have the same order value, their execution order is undefined.

Example:

```java
public class Listener1 {
   @Order(1)
   @EventListener
   public void handleEvent(FamilyMan event) {
      System.out.println("Hi " + name
              + ": New episode of " + event.getShowName()
              + " is available (Episode " + event.getEpisodeNo() + ")");
   }
}

public class Listener2 {
   @Order(2)
   @EventListener
   public void hadleEvent(FamilyMan event) {
      System.out.println("Hi " + name
              + ": New episode of " + event.getShowName()
              + " is available (Episode " + event.getEpisodeNo() + ")");
   }
}
```
**Listener2** `hadleEvent` method will be called before **Listener1** `handleEvent` method when FamilyMan even is published.


## Async Listeners

Asynchronous event listeners enables the processing of events in separate threads, providing benefits such as improved responsiveness, 
scalability, reduced blocking, handling long-running tasks, fault tolerance, resource utilization, and parallel event processing.

### Deep dive into default framework implementation

**AbstractApplicationContext**: Abstract implementation of the ApplicationContext interface. Contains a `ApplicationEventMulticaster` instance 
which is responsible for publishing the events and invoking the listeners.
```java
/*
 * Abstract implementation of the ApplicationContext interface      
 */
public abstract class AbstractApplicationContext {
    
   // Responsible for publishing events and invoking the listeners.
   private ApplicationEventMulticaster applicationEventMulticaster;

   /*
    * Initialize the ApplicationEventMulticaster       
    */
   protected void initApplicationEventMulticaster() {
       //...
      // check if beanFactory contains a custom ApplicationEventMulticaster bean named "applicationEventMulticaster"
      if (beanFactory.containsLocalBean("applicationEventMulticaster")) {
         // fetch and register the ApplicationEventMulticaster bean
         this.applicationEventMulticaster =
                 beanFactory.getBean("applicationEventMulticaster", ApplicationEventMulticaster.class);
      }
      else {
         // initialize and register SimpleApplicationEventMulticaster as default ApplicationEventMulticaster
         this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
         //..
      }
   }
}
```
So, `AbstractApplicationContext` first checks for a custom `ApplicationEventMulticaster` bean (named _applicationEventMulticaster_),
and if found registers it as the `ApplicationEventMulticaster`.<br>
But, if not such custom bean implementation is found it provides a default implementation to `ApplicationEventMulticaster`
i.e: `SimpleApplicationEventMulticaster` instance.

>So, if there is no custom bean declaration of `ApplicationEventMulticaster`, then only the default one is used.

**ApplicationEventMulticaster** : This is the interface responsible for managing the event listeners and dispatching the
events to those listeners.

```java
/*
 * Interface responsible for managing the event listeners and dispatching the events to those listeners.  
 */
public interface ApplicationEventMulticaster {
   /*
    * Multicast the given application event to appropriate listeners.        
    */
   void multicastEvent(ApplicationEvent event);
   //...
}
```

**SimpleApplicationEventMulticaster** : Implementation class of the ApplicationEventMulticaster.

```java
/*
 * Implementaion class of ApplicationEventMulticaster.
 * Multicasts all events to all the registered listeners.
 * By default, all listeners are invoked in calling thread only, which may block the entire application.
 * 
 * SyncTaskExecutor: default task executor which executes all the listeners in the calling thread.
 * Specify an alternative task executor to have listeners executed in different thread (ex. from a Thread pool).
 */
public class SimpleApplicationEventMulticaster implements ApplicationEventMulticaster{
    //..
   //from java.util.concurrent
   private Executor taskExecutor;
   //..

   /*
    * sets a custom executor to invoke each listener with.
    * SyncTaskExecutor: default implementation, which executes all listeners synchronously in the calling thread.
    * SimpleAsyncTaskExecutor: does not blocks the calling thread, and executes each of the listener in separate thread.
    */
   public void setTaskExecutor(@Nullable Executor taskExecutor) {
      this.taskExecutor = taskExecutor;
   }
   
   //...
   
}
```
So, the default implementation of `ApplicationEventMulticaster` is `SimpleApplicationEventMulticaster`,
whose `taskExecutor` property is by default set to a `SyncTaskExecutor` instance.<br>
And `SyncTaskExecutor` executes all the listeners synchronously in the calling thread only.

>Now, to change this behaviour we need custom implementation of `ApplicationEventMulticaster`, 
>i.e: a custom bean of `SimpleApplicationEventMulticaster` class where the `taskExecutor` property is not set to `SyncTaskExecutor` instance.<br>
>Instead, it is set to`SimpleAsyncTaskExecutor` instance or `ThreadPoolTaskExecutor` instance, which will execute all the
>listeners in a separate thread.
>
>Since, the framework by default first checks for custom implementation of `ApplicationEventMulticaster`. It will register
>this custom implementation and use it for casting the events to the listeners.

### Async listeners through custom `ApplicationEventMulticaster`

1. Create the Event Class:

   Define the custom event class representing the change event. This event will be same as the previous examples.
   <br>
   _FamilyMan (Event)_: [FamilyMan.java](async_listeners/event/FamilyMan.java)

2. Create the Event Publisher:

   Next, the `AmazonPrime` will act as the event publisher, and the `ApplicationEventPublisher` needs to be injected to publish the events.
   The Event Publisher implementation will also be same as the previous examples.
   <br>
   _AmazonPrime (Event Publisher)_: [AmazonPrime.java](async_listeners/publisher/AmazonPrime.java)

3. Implement Event Listeners:
 
   _AliceSubscriber (Subscriber1)_ : [AliceSubscriber.java](async_listeners/subscriber/AliceSubscriber.java)
   ```java
   @Component
   public class AliceSubscriber {
       private String name = "Alice";
   
       @EventListener
       public void hadleFamilyManEvent(FamilyMan event) throws InterruptedException {
           System.out.println("Handle method of Alice");
           Thread.sleep(4000);
           System.out.println("Hi "+ name
                   + ": New episode of " + event.getShowName()
                   + " is available (Episode " + event.getEpisodeNo() + ")");
       }
   
   }
   ```
   _BobSubscriber (Subscriber2)_ : [BobSubscriber.java](async_listeners/subscriber/BobSubscriber.java)<br>
   _CharlesSubscriber (Subscriber3)_ : [BobSubscriber.java](async_listeners/subscriber/CharlesSubscriber.java)

   The other two listeners have the same implementation as the _AliceSubscriber_. The listeners have a deliberate
   `Thread.sleep(4000)` to show the asynchronous execution of the listeners.

4. Provide custom `ApplicationEventMulticaster` implementation for supporting asynchronous execution of listeners.

   _AppConfig (Configuration class)_ : [AppConfig.java](async_listeners/AppConfig.java)
   ```java
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.ComponentScan;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.context.event.ApplicationEventMulticaster;
   import org.springframework.context.event.SimpleApplicationEventMulticaster;
   import org.springframework.core.task.SimpleAsyncTaskExecutor;
   
   @Configuration
   @ComponentScan
   public class AppConfig {
   
       // custom implementation of ApplicationEventMulticaster for Async Listeners
       @Bean("applicationEventMulticaster")
       public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
           SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = new SimpleApplicationEventMulticaster();
           //taskExecutor set to SimpleAsyncTaskExecutor instead of the default SyncTaskExecutor
           simpleApplicationEventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
           return simpleApplicationEventMulticaster;
       }
   }
   ```
5. Create and Publish the event:
   Now, create the `FamilyMan` event and publish it through `AmazonPrime` publisher.
   All the subscribers/observers of those specified events will be notified automatically, but this time the listeners 
   will be invoked in asynchronous fashion.
   <br><br>
   _App (Driver class)_ : [App.java](async_listeners/App.java)
   ```java
   public class App {
       public static void main(String[] args) {
           ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
           AmazonPrime amazonPrime = applicationContext.getBean("amazonPrime", AmazonPrime.class);
           //publishing the event
           System.out.println("Publishing the Event: Family Man");
           amazonPrime.broadcastFamilyMan(new FamilyMan("S2: EP10"));
           System.out.println("Event published!!!");
       }
   }
   ```
   Output: (may change for each execution)
   ```shell
   Publishing the Event: Family Man
   Event published!!!
   Listener method of Alice
   Listener method of Charles
   Listener method of Bob
   Hi Bob: New episode of family-man is available (Episode S2: EP10)
   Hi Alice: New episode of family-man is available (Episode S2: EP10)
   Hi Charles: New episode of family-man is available (Episode S2: EP10)
   ```

As evident from the output, the listeners were not invoked on the calling thread instead they were invoked on separate thread  
leading to parallel execution.
So, their invocation is independent of each other as well as the calling thread.


### Async listeners through `@Async` and `@EnableAsync`

The `@EnableAsync` and `@Async` annotations enable the [Asynchronous processing in Spring.](../asynchronous_processing/README.md)

1. Create the Event Class:
   Define the custom event class representing the change event. This event will be same as the previous examples.
   <br>
   _FamilyMan (Event)_: [FamilyMan.java](async_listeners/event/FamilyMan.java)

2. Create the Event Publisher:
   Next, the `AmazonPrime` will act as the event publisher, and the `ApplicationEventPublisher` needs to be injected to publish the events.
   The Event Publisher implementation will also be same as the previous examples.
   <br>
   _AmazonPrime (Event Publisher)_: [AmazonPrime.java](async_listeners/publisher/AmazonPrime.java)

3. Implement Event Listeners:
   <br>
   _AliceSubscriber (Subscriber1)_ : [AliceSubscriber.java](async_listeners/subscriber/AliceSubscriber.java)
   ```java
   @Component
   public class AliceSubscriber {
       private String name = "Alice";
   
       @Async
       @EventListener
       public void hadleFamilyManEvent(FamilyMan event) throws InterruptedException {
           System.out.println("Handle method of Alice");
           Thread.sleep(4000);
           System.out.println("Hi "+ name
                   + ": New episode of " + event.getShowName()
                   + " is available (Episode " + event.getEpisodeNo() + ")");
       }
   
   }
   ```
   _BobSubscriber (Subscriber2)_ : [BobSubscriber.java](async_listeners/subscriber/BobSubscriber.java)<br>
   _CharlesSubscriber (Subscriber3)_ : [BobSubscriber.java](async_listeners/subscriber/BobSubscriber.java)

   The listener methods are annotated with `@Async` which indicates that a specific method should be executed asynchronously. 
   When `@Async` is applied to a method, Spring wraps the method's invocation in a separate thread, allowing the method to be executed asynchronously.

   The other two listeners have the same implementation as the _AliceSubscriber_. The listeners have a deliberate
   `Thread.sleep(4000)` to show the asynchronous execution of the listeners.

4. Annotate the Configuration class with `@EnableAsync` for supporting asynchronous execution of listeners.<br>
   When`@EnableAsync` is applied to a configuration class, Spring sets up the necessary infrastructure to support asynchronous method invocation, 
   including creating the necessary `TaskExecutor` bean.
   If no `TaskExecutor` bean is defined explicitly, Spring creates a default `SimpleAsyncTaskExecutor` bean, 
   which is suitable for basic asynchronous execution but may not be ideal for production environments.

   _AppConfig (Configuration class)_ : [AppConfig.java](async_listeners/AppConfig.java)
   ```java
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.ComponentScan;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.context.event.ApplicationEventMulticaster;
   import org.springframework.context.event.SimpleApplicationEventMulticaster;
   import org.springframework.core.task.SimpleAsyncTaskExecutor;
   import org.springframework.scheduling.annotation.EnableAsync;
   
   @Configuration
   @ComponentScan
   @EnableAsync
   public class AppConfig {
       //custom TaskExecutor Bean can also be defined using @Bean
   }
   ```
5. Create and Publish the event:
   Now, create the `FamilyMan` event and publish it through `AmazonPrime` publisher.
   All the subscribers/observers of those specified events will be notified automatically, but this time the listeners
   will be invoked in asynchronous fashion.
   <br><br>
   _App (Driver class)_ : [App.java](async_listeners/App.java)
   ```java
   public class App {
       public static void main(String[] args) {
           ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
           AmazonPrime amazonPrime = applicationContext.getBean("amazonPrime", AmazonPrime.class);
           //publishing the event
           System.out.println("Publishing the Event: Family Man");
           amazonPrime.broadcastFamilyMan(new FamilyMan("S2: EP10"));
           System.out.println("Event published!!!");
       }
   }
   ```
   Output: (may change for each execution)
   ```shell
   Publishing the Event: Family Man
   Event published!!!
   Listener method of Alice
   Listener method of Charles
   Listener method of Bob
   Hi Bob: New episode of family-man is available (Episode S2: EP10)
   Hi Alice: New episode of family-man is available (Episode S2: EP10)
   Hi Charles: New episode of family-man is available (Episode S2: EP10)
   ```

## Built-in Events

1. `ContextRefreshedEvent`:
   - Published when ApplicationContext is initialized or refreshed. Here, "initialized" means that all the beans are loaded, post-processor beans are detected and activated,
   singletons are instantiated, and the ApplicationContext implementation is ready to use.
   - This can also be raised using the refresh() method on the ConfigurableApplicationContext interface.

2. `ContextStartedEvent`:
   - Published when the ApplicationContext is started using the start() method on ConfigurableApplicationContext interface.

3. `ContextStoppedEvent`: 
   - Published when ApplicationContext is stopped using the stop() method on ConfigurableApplicationContext interface.
   
4. `ContextClosedEvent`:
   - Published when the ApplicationContext is closed using the close() method on the ConfigurableApplicationContext interface. 
   - Once a context closes; it cannot be refreshed or restarted.

These built-in events can be listened just like custom events as demonstrated in previous examples.<br>
There are other built-in events as well.
