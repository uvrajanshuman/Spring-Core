package com.refstash.dependency_injection.xml_configuration.setter_injection;

public class Engine {
    private String type;
    private String displacement;
    private int cylinder;
    private String transmissionType;
    private String gearBox;

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
