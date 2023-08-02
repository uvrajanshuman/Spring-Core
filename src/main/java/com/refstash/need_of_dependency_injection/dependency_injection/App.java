package com.refstash.need_of_dependency_injection.dependency_injection;

class App {
	public static void main(String[] args) {
		//Instance of desired SortAlgorithm.
		SortAlgorithm sortAlgorithm = new BubbleSort();
		//Injecting dependency while instantiating.
		BinarySearch binarySearch = new BinarySearch(sortAlgorithm);

		int arr[] = { 2, 3, 4, 10, 40 };
		int result = binarySearch.search(arr, 100);
		System.out.println(result != -1 ? "Element found" : "Element not found");
	}
}
