package com.refstash.need_of_dependency_injection.without_dependency_injection;

public class BinarySearch {
	// Dependency of BinarySearch
	// Dependency directly instantiated making the classes tightly coupled.
	private SortAlgorithm sortAlgorithm = new BubbleSort();

	int search(int arr[], int x) {
		// sorting the array
		sortAlgorithm.sort(arr);

		int l = 0, r = arr.length - 1;
		while (l <= r) {
			int m = l + (r - l) / 2;

			// Check if x is present at mid
			if (arr[m] == x)
				return m;

			// If x greater, ignore left half
			if (arr[m] < x)
				l = m + 1;

			// If x is smaller, ignore right half
			else
				r = m - 1;
		}

		// if we reach here, then element was
		// not present
		return -1;
	}
}
