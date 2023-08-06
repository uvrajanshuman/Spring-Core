package com.refstash.dependency_injection.annotation_configuration.autowiring;

/*
 * Implementation of SortAlgorithm: BubbleSort
 */

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//@Primary
@Qualifier("bubble")
public class BubbleSort implements SortAlgorithm {

	@Override
	public void sort(int[] arr) {

		int i, j, temp, n = arr.length;
		boolean swapped;
		for (i = 0; i < n - 1; i++) {
			swapped = false;
			for (j = 0; j < n - i - 1; j++) {
				if (arr[j] > arr[j + 1]) {
					// swap arr[j] and arr[j+1]
					temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
					swapped = true;
				}
			}

			// If no two elements were
			// swapped by inner loop, then break
			if (!swapped)
				break;
		}
	}

}
