package com.refstash.externalizing_configurations.spring_expression_language;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class SpElExample {

    // Arithmetic Operators
    @Value("#{10 + 10}")//20
    private int sum;

    @Value("#{'ABC' + 'DEF'}")  //"ABCDEF"
    private String concatString;

    @Value("#{10 - 5}") //5
    private int difference;

    @Value("#{5 * 2}")  //10.0
    private double product;

    @Value("#{30 / 2}") //15.0
    private double division;

    @Value("#{11 % 2}") //1.0
    //@Value("#{11 mod 2}") //1.0
    private double modulo;

    @Value("#{2 ^ 2}")  //4.0
    private double powerOf;

    @Value("#{(2 + 2) * 2 + 2}") //10.0
    private double brackets;

    // Relational Operations
    @Value("#{5 == 5}") //true
    //@Value("#{5 eq 5})    //true
    private boolean equal;

    @Value("#{5 != 5}") //false
    //@Value("#{5 ne 5}") //false
    private boolean notEqual;

    @Value("#{2 < 4}")  //true
    //@Value("#{2 lt 4}") //true
    private boolean lessThan;

    @Value("#{5 <= 5}") //true
    //@Value("#{5 le 5}") //true
    private boolean lessThanOrEqual;

    @Value("#{5 > 4}") //true
    //@Value("#{5 gt 4}") //true
    private boolean greaterThan;

    @Value("#{5 >= 5}") //true
    //@Value("#{5 ge 5}") //true
    private boolean greaterThanOrEqual;

    // Logical Operations
    @Value("#{200 > 100 && 200 < 400}") //true
    //@Value("#{200 > 100 and 200 < 400}") //true
    private boolean and;

    @Value("#{200 > 100 || 200 < 100}") //true
    //@Value("#{200 > 100 or 200 < 100}") //true
    private boolean or;

    @Value("#{!true}")  //false
    //@Value("#{not true}")   //false
    private boolean not;

    // Ternary Operation
    @Value("#{5 > 4 ? 5 : 4}") //5
    private int ternary;

    @Value("#{someBean.someProperty != null ? someBean.someProperty : 'default'}") //'default'
    private String anotherTernary;

    // Above example can be shortened using elvis operator
    // Elvis Operator // will inject 'default' if someProperty is null
    @Value("#{someBean.someProperty ?: 'default'}") //'default'
    private String elvis;

    //Regex Expression
    @Value("#{'John Doe' matches '[a-zA-Z\\s]+' }") //true
    private boolean validName;

    /* Referencing other bean, and it's property and methods */
    // Referring other bean
    @Value("#{someBean}")   //com.refstash.externalizing_configurations.spring_expression_language.SomeBean@55a147cc
    private SomeBean someBean;

    // Referencing static variable of a class
    @Value("#{T(java.lang.Math).PI}") //3.141592653589793
    private double pi;

    @Value("#{T(java.lang.Math).random()}") //0.18650743757386068
    private String random;

    // Referring other bean's property
    @Value("#{someBean.anotherProperty}") //'someBean.anotherProperty'
    private String someBeanAnotherProperty;

    // Referring other bean's method
    @Value("#{someBean.someMethod()}") //'someBean.someMethod()'
    private String someBeanMethod;

    // Referring Collections
    @Value("#{someBean.someArray}") //[3, 6, 9, 12, 15, 18]
    private int[] referredArray;

    @Value("#{someBean.someList}")  //[1, 2, 3, 4, 5, 6]
    private List<Integer> referredList;

    @Value("#{someBean.someMap}")   //{A=Apple, B=Ball, C=Cat}
    private Map<Character,String> referredMap;

    // Accessing Collection
    @Value("#{someBean.someArray[0]}")   //3
    private int accessedFirstIntFromArray;

    @Value("#{someBean.someList[0]}")   //1
    private int accessedFirstIntFromList;

    @Value("#{someBean.someList.size()}")   //6
    private int accessedListLength;

    @Value("#{someBean.someMap['A']}")  //'Apple'
    private String accessedStringFromMap;

    // Collection filtering
    @Value("#{someBean.someList.?[#this % 2 == 0]}") //[2, 4, 6]
    private List<Integer> evenNumList;

    @Value("#{someBean.someArray.?[#this > 5]}") //[6, 9, 12, 15, 18]
    private int[] greaterThanFiveArr;

    @Value("#{someBean.someMap.?[value.contains('a')]}") //{B=Ball, C=Cat}
    private Map<Character,String> newMap;

    @Override
    public String toString() {
        return "SpElExample{" +
                "\n sum=" + sum +
                ",\n concatString='" + concatString + '\'' +
                ",\n difference=" + difference +
                ",\n product=" + product +
                ",\n division=" + division +
                ",\n modulo=" + modulo +
                ",\n powerOf=" + powerOf +
                ",\n brackets=" + brackets +
                ",\n equal=" + equal +
                ",\n notEqual=" + notEqual +
                ",\n lessThan=" + lessThan +
                ",\n lessThanOrEqual=" + lessThanOrEqual +
                ",\n greaterThan=" + greaterThan +
                ",\n greaterThanOrEqual=" + greaterThanOrEqual +
                ",\n and=" + and +
                ",\n or=" + or +
                ",\n not=" + not +
                ",\n ternary=" + ternary +
                ",\n anotherTernary='" + anotherTernary + '\'' +
                ",\n elvis='" + elvis + '\'' +
                ",\n validName=" + validName +
                ",\n someBean=" + someBean +
                ",\n pi=" + pi +
                ",\n random=" + random +
                ",\n someBeanAnotherProperty='" + someBeanAnotherProperty + '\'' +
                ",\n someBeanMethod='" + someBeanMethod + '\'' +
                ",\n referredArray=" + Arrays.toString(referredArray) +
                ",\n referredList=" + referredList +
                ",\n referredMap=" + referredMap +
                ",\n accessedFirstIntFromArray=" + accessedFirstIntFromArray +
                ",\n accessedFirstIntFromList=" + accessedFirstIntFromList +
                ",\n accessedListLength=" + accessedListLength +
                ",\n accessedStringFromMap='" + accessedStringFromMap + '\'' +
                ",\n evenNumList=" + evenNumList +
                ",\n greaterThanFiveArr=" + Arrays.toString(greaterThanFiveArr) +
                ",\n newMap=" + newMap +
                "\n}";
    }
}

@Configuration
@ComponentScan
class App {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        SpElExample spel = applicationContext.getBean(SpElExample.class);
        System.out.println(spel);
    }
}

/*
 * Output:
 * someProperty: getter called
 * someProperty: getter called
 * anotherProperty: getter called
 * someMethod: invoked
 * someArray: getter called
 * someList: getter called
 * someMap: getter called
 * someArray: getter called
 * someList: getter called
 * someList: getter called
 * someMap: getter called
 * someList: getter called
 * someArray: getter called
 * someMap: getter called
 * SpElExample{
 *  sum=20,
 *  concatString='ABCDEF',
 *  difference=5,
 *  product=10.0,
 *  division=15.0,
 *  modulo=1.0,
 *  powerOf=4.0,
 *  brackets=10.0,
 *  equal=true,
 *  notEqual=false,
 *  lessThan=true,
 *  lessThanOrEqual=true,
 *  greaterThan=true,
 *  greaterThanOrEqual=true,
 *  and=true,
 *  or=true,
 *  not=false,
 *  ternary=5,
 *  anotherTernary='default',
 *  elvis='default',
 *  validName=true,
 *  someBean=com.refstash.externalizing_configurations.spring_expression_language.SomeBean@55a147cc,
 *  pi=3.141592653589793,
 *  random=0.35702526638036214,
 *  someBeanAnotherProperty='someBean.anotherProperty',
 *  someBeanMethod='someBean.someMethod()',
 *  referredArray=[3, 6, 9, 12, 15, 18],
 *  referredList=[1, 2, 3, 4, 5, 6],
 *  referredMap={A=Apple, B=Ball, C=Cat},
 *  accessedFirstIntFromArray=3,
 *  accessedFirstIntFromList=1,
 *  accessedListLength=6,
 *  accessedStringFromMap='Apple',
 *  evenNumList=[2, 4, 6],
 *  greaterThanFiveArr=[6, 9, 12, 15, 18],
 *  newMap={B=Ball, C=Cat}
 * }
 */