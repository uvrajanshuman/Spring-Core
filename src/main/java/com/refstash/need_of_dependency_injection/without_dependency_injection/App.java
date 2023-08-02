package com.refstash.need_of_dependency_injection.without_dependency_injection;

class App {
	public static void main(String[] args) {
		BinarySearch binarySearch = new BinarySearch();
		int arr[] = { 2, 3, 4, 10, 40 };
		int result = binarySearch.search(arr, 100);
		System.out.println(result != -1 ? "Element found" : "Element not found");
	}
}
