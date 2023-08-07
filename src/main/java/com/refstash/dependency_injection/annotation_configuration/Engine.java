package com.refstash.dependency_injection.annotation_configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("harrierEngine")
public class Engine {
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