package com.refstash.need_of_dependency_injection.dependency_injection_with_spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class App {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JavaConfig.class);

		BinarySearch binarySearch = applicationContext.getBean("binarySearch", BinarySearch.class);

		int[] arr = { 2, 3, 4, 10, 40 };
		int result = binarySearch.search(arr, 100);
		System.out.println(result != -1 ? "Element found" : "Element not found");
	}
}
