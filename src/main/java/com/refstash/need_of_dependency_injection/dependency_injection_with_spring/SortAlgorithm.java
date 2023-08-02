package com.refstash.need_of_dependency_injection.dependency_injection_with_spring;

import org.springframework.stereotype.Component;

@Component
public interface SortAlgorithm {
	void sort(int[] arr);
}
