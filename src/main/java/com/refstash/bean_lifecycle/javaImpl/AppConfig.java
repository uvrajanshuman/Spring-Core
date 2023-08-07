package com.refstash.bean_lifecycle.javaImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean(value = "lifeCycleExample", initMethod = "init", destroyMethod = "destroy")
    public LifeCycleExample getLifeCycleExampleBean(){
        return new LifeCycleExample(10);
    }
}
