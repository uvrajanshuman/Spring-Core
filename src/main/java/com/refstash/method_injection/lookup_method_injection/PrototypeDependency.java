package com.refstash.method_injection.lookup_method_injection;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeDependency {
    private static int identifier = 0;

    public PrototypeDependency() {
        identifier++;
    }

    public void processData() {
        System.out.println("Processing data in PrototypeDependency " + identifier);
    }
}
