package com.refstash.dependency_injection.annotation_configuration.autowiring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class App {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

		BinarySearch binarySearch = applicationContext.getBean("binarySearch", BinarySearch.class);

		int[] arr = { 2, 3, 4, 10, 40 };
		int result = binarySearch.search(arr, 40);
		System.out.println(result != -1 ? "Element found" : "Element not found");

	}
}

/*
 * Output:
 * com.refstash.dependency_injection.annotation_configuration.autowiring.BubbleSort@49139829
 * Element found
 */