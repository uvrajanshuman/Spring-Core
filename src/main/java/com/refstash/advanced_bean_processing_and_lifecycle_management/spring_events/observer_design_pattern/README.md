# Observer Design Pattern

The Observer Design Pattern is a behavioral pattern that allows an object, called the subject/publisher, 
to notify multiple objects, called observers/subscribers, about changes in its state. 

It promotes loose coupling between the subject and its observers, enabling a one-to-many dependency relationship, 
where any change in the subject's state triggers updates in all registered observers. 

**Example Scenario:**<br>
When a broadcasting service like Amazon Prime starts broadcasting an episode of a specific show, all the subscribers of 
that show should be notified.

### Event/Change

[_Episode.java_](event/Episode.java)
```java
public interface Episode {
    String getShowName();
    String getEpisodeNo();
}
```
[_FamilyMan.java_](event/FamilyMan.java)
```java
public class FamilyMan implements Episode {

    public static String showName = "family-man";

    private final String episodeNo;

    public FamilyMan(String episodeNo) {
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
[_Panchayat.java_](event/Panchayat.java)
```java
public class Panchayat implements Episode {

    public static String showName = "panchayat";

    private String episodeNo;

    public Panchayat(String episodeNo) {
        this.episodeNo = episodeNo;
    }

    @Override
    public String getShowName() {
        return showName;
    }

    public String getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(String episodeNo) {
        this.episodeNo = episodeNo;
    }
}
```
### Publisher/Subject

[_Broadcaster.java_](publisher/Broadcaster.java)
```java
public interface Broadcaster {

    void addSubscriber(String show, Subscriber subsciber);

    void removeSubscriber(String show, Subscriber subsciber);

    void broadcast(Episode episode);
}
```
[_AmazonPrime.java_](publisher/AmazonPrime.java)
```java
public class AmazonPrime implements Broadcaster{
    
    private Map<String, List<Subscriber>> subscribersByShow = new HashMap<>();

    public AmazonPrime() {
        //initializing subscriberByShow for available shows
        subscribersByShow.put("family-man", new ArrayList<>());
        subscribersByShow.put("panchayat", new ArrayList<>());
    }

    @Override
    public void addSubscriber(String show, Subscriber subscriber) {
        List<Subscriber> subscribers = subscribersByShow.get(show);
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscriber(String show, Subscriber subscriber) {
        List<Subscriber> subscribers = subscribersByShow.get(show);
        subscribers.remove(subscriber);
    }

    @Override
    public void broadcast(Episode episode) {
        List<Subscriber> subscribers = subscribersByShow.get(episode.getShowName());
        for (Subscriber subscriber: subscribers) {
            subscriber.update(episode);
        }
    }
}
```

### Observer/Subscriber

[_Subscriber.java_](subscriber/Subscriber.java)
```java
public interface Subscriber {
    void update(Episode episode);
}
```

[_PrimeSubscriber.java_](subscriber/PrimeSubscriber.java)
```java
public class PrimeSubscriber implements Subscriber {

    private String name;

    public PrimeSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void update(Episode episode) {
        System.out.println("Hi "+ name
                + ": New episode of " + episode.getShowName()
                + " is available (Episode " + episode.getEpisodeNo() + ")");
    }
}
```

### Driver class:
[_App.java_](./App.java)
```java
public class App {
    public static void main(String[] args) {
        Broadcaster broadcaster = new AmazonPrime();

        Subscriber subscriber1 = new PrimeSubscriber("Alice");
        Subscriber subscriber2 = new PrimeSubscriber("Bob");

        broadcaster.addSubscriber("family-man",subscriber1);
        broadcaster.addSubscriber("panchayat",subscriber1);
        broadcaster.addSubscriber("panchayat",subscriber2);

        Episode panhayat = new Panchayat("S2:05");
        broadcaster.broadcast(panhayat);

        Episode familyMan = new FamilyMan("S2:10");
        broadcaster.broadcast(familyMan);

    }
}
```
Output:
```shell
Hi Alice: New episode of panchayat is available (Episode S2:05)
Hi Bob: New episode of panchayat is available (Episode S2:05)
Hi Alice: New episode of family-man is available (Episode S2:10)
```