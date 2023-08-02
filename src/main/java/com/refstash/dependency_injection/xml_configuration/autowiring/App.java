package com.refstash.dependency_injection.xml_configuration.autowiring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

class App {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("com/refstash/dependency_injection/xml_configuration/autowiring/config.xml");

		BinarySearch binarySearch = applicationContext.getBean("binarySearch", BinarySearch.class);

		int[] arr = { 2, 3, 4, 10, 40 };
		int result = binarySearch.search(arr, 40);
		System.out.println(result != -1 ? "Element found" : "Element not found");
	}
}
