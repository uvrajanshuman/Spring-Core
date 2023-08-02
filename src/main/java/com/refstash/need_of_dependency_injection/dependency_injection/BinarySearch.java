package com.refstash.need_of_dependency_injection.dependency_injection;

public class BinarySearch {
	// Dependency of BinarySearch
	private SortAlgorithm sortAlgorithm;

	public BinarySearch() {
	}

	//Instantiation of sortAlgorithm through constructor.
	public BinarySearch(SortAlgorithm sortAlgorithm) {
		this.sortAlgorithm = sortAlgorithm;
	}

	//Instantiation of sortAlgorithm through the setter method.
	public void setSortAlgorithm(SortAlgorithm sortAlgorithm) {
		this.sortAlgorithm = sortAlgorithm;
	}

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
