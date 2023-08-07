package com.refstash.bean_scope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DependentClass {
    @Autowired
    private DependencyClass dependencyClass;

    public DependencyClass getDependencyClass() {
        return dependencyClass;
    }

    public void setDependencyClass(DependencyClass dependencyClass) {
        this.dependencyClass = dependencyClass;
    }
}
