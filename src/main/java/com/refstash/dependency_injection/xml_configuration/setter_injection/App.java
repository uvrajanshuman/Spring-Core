package com.refstash.dependency_injection.xml_configuration.setter_injection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/com/refstash/dependency_injection/xml_configuration/setter_injection/config.xml");
        Car tataHarrier = context.getBean("tataHarrier",Car.class);
        System.out.println(tataHarrier);
    }
}

/*
 * Output:
 * Car{
 * brand='Tata'
 * model='Harrier'
 * engine=Engine{type='Kryotec 2.0 L Turbocharged Engine', displacement='1956cc', cylinder=4, transmissionType='Automatic', gearBox='6-Speed'}
 * specifications={Mileage=14.6kmpl, Fuel Type=Diesel, Fuel Tank Capacity=50.0, Seating Capacity=5, Body Type=SUV}
 * features=[Power Steering, Power Windows , Air Conditioner, Heater, Driver and Passenger Airbag, Automatic climate control]
 * colors=[Orcus White, Calypso Red, Daytona Grey, Oberon Black]
 * }
 */