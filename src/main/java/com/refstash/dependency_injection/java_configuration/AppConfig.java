package com.refstash.dependency_injection.java_configuration;

/*
 * Bean declaration through @Bean
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.*;

@Configuration
@PropertySource("classpath:com/refstash/dependency_injection/java_configuration/application.properties")
public class AppConfig {

//    @Bean("harrierEngine")
//    public Engine getHarrierEngineBean() {
//        Engine engine = new Engine();
//        engine.setType("Kryotec 2.0 L Turbocharged Engine");
//        engine.setDisplacement("1956cc");
//        engine.setCylinder(4);
//        engine.setTransmissionType("Automatic");
//        engine.setGearBox("6-Speed");
//        return engine;
//    }

//    @Bean(name = {"tataHarrier", "harrier"})
//    public Car getHarrierBean() {
//        Car car = new Car();
//        car.setBrand("Tata");
//        car.setModel("Harrier");
//        car.setFeatures(Arrays.asList("Power Steering", "Power Windows", "Air Conditioner", "Heater",
//                "Driver and Passenger Airbag", "Automatic climate control"));
//        car.setColors(new HashSet<>(Arrays.asList("Orcus White", "Calypso Red", "Daytona Grey", "Oberon Black")));
//        Map<String,String> specs = new HashMap<>();
//        specs.put("Mileage", "14.6kmpl");
//        specs.put("Fuel Type", "Diesel");
//        specs.put("Fuel Tank Capacity", "50.0");
//        specs.put("Seating Capacity", "5");
//        specs.put("Body Type", "SUV");
//        car.setSpecifications(specs);
//        car.setEngine(getHarrierEngineBean());
//        return car;
//    }

    //Externalizing the configuration to a properties file
    @Autowired
    private Environment environment;

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
        Car car = new Car();
        car.setBrand(environment.getProperty("car.brand"));
        car.setModel(environment.getProperty("car.model"));
        //List
        car.setFeatures(environment.getProperty("car.features",ArrayList.class));
        //Set
        car.setColors(environment.getProperty("car.colors",HashSet.class));
        //Map
        Map<String,String> specs = new HashMap<>();
        specs.put("Mileage", environment.getProperty("car.specifications.Mileage"));
        specs.put("Fuel Type", environment.getProperty("car.specifications.FuelType"));
        specs.put("Fuel Tank Capacity", environment.getProperty("car.specifications.FuelTankCapacity"));
        specs.put("Seating Capacity", environment.getProperty("car.specifications.SeatingCapacity"));
        specs.put("Body Type", environment.getProperty("car.specifications.BodyType"));
        car.setSpecifications(specs);
        //reference injection
        car.setEngine(getHarrierEngineBean());
        return car;
    }
}
