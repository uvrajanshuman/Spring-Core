package com.refstash.dependency_injection.xml_configuration.autowiring;

/*
 * BinarySearch Implementation
 * depends on a SortAlgorithm
 */

public class BinarySearch {
	// Dependency of BinarySearch
	// Dependency injected by Spring

	private SortAlgorithm sortAlgorithm;

	// Autowiring byName
	/*
	public void setBubbleSort(SortAlgorithm bubbleSort) {
		this.sortAlgorithm = bubbleSort;
	}

	public SortAlgorithm getBubbleSort() {
		return sortAlgorithm;
	}
	*/

	// Autowiring byType
	/*
	public void setSortAlgorithm(SortAlgorithm sortAlgorithm) {
		this.sortAlgorithm = sortAlgorithm;
	}

	public SortAlgorithm getSortAlgorithm() {
		return sortAlgorithm;
	}
	*/

	public BinarySearch() {
		System.out.println("no arg constructor");
	}

	//Autowiring by constructor
	public BinarySearch(SortAlgorithm sortAlgorithm) {
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
