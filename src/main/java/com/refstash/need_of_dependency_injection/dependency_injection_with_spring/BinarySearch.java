package com.refstash.need_of_dependency_injection.dependency_injection_with_spring;

/*
 * BinarySearch Implementation
 * depends on a SortAlgorithm
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BinarySearch {
	// Dependency of BinarySearch
	// Dependency injected by Spring
	@Autowired
	private SortAlgorithm sortAlgorithm;

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
