package com.refstash.advanced_bean_processing_and_lifecycle_management.asynchronous_processing;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    @Async
    public void asyncMethod() {
        System.out.println("asyncMethod() : execution starts");
        for (int i = 1; i <= 10; i++) {
            System.out.println("asyncMethod: "+i);
        }
        System.out.println("asyncMethod() : execution ends");
    }
}
