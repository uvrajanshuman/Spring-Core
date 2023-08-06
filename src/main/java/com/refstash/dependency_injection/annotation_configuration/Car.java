package com.refstash.dependency_injection.annotation_configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component("tataHarrier")
public class Car {
    @Value("${car.brand}")
    private String brand;

    @Value("${car.model}")
    private String model;

    @Autowired
    private Engine engine;

    @Value("#{${car.specifications}}")
    private Map<String,String> specifications;

    @Value("${car.features}")
    private List<String> features;

    @Value("#{'${car.colors}'.split(',')}")
    private Set<String> colors;

    public Car(){}

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
