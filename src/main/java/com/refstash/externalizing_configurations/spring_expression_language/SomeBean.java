package com.refstash.externalizing_configurations.spring_expression_language;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SomeBean {
    private String someProperty; //null

    private String anotherProperty;

    private int[] someArray;

    private List<Integer> someList;

    private Map<Character,String> someMap;

    // instance initialization block
    {
        anotherProperty = "someBean.anotherProperty";
        someArray = new int[]{3,6,9,12,15,18};
        someList = Arrays.asList(1,2,3,4,5,6);
        someMap = new HashMap<>();
        someMap.put('A', "Apple");
        someMap.put('B', "Ball");
        someMap.put('C', "Cat");
    }

    public SomeBean() {
    }

    public SomeBean(String someProperty, List<Integer> someList, Map<Character, String> someMap) {
        this.someProperty = someProperty;
        this.someList = someList;
        this.someMap = someMap;
    }

    public String getSomeProperty() {
        System.out.println("someProperty: getter called");
        return someProperty;
    }

    public void setSomeProperty(String someProperty) {
        this.someProperty = someProperty;
    }

    public String getAnotherProperty() {
        System.out.println("anotherProperty: getter called");
        return anotherProperty;
    }

    public void setAnotherProperty(String anotherProperty) {
        this.anotherProperty = anotherProperty;
    }

    public int[] getSomeArray() {
        System.out.println("someArray: getter called");
        return someArray;
    }

    public void setSomeArray(int[] someArray) {
        this.someArray = someArray;
    }

    public List<Integer> getSomeList() {
        System.out.println("someList: getter called");
        return someList;
    }

    public void setSomeList(List<Integer> someList) {
        this.someList = someList;
    }

    public Map<Character,String> getSomeMap() {
        System.out.println("someMap: getter called");
        return someMap;
    }

    public void setSomeMap(Map<Character,String> someMap) {
        this.someMap = someMap;
    }

    public String someMethod(){
        System.out.println("someMethod: invoked");
        return "someBean.someMethod()";
    }
}
