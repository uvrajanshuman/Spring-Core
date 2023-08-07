package com.refstash.method_injection.lookup_method_injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public abstract class SingletonBean {
    private PrototypeDependency prototypeDependency;

    /*
    @Autowired
    private ApplicationContext applicationContext;

    public PrototypeDependency getPrototypeDependency() {
        return applicationContext.getBean(PrototypeDependency.class);
    }
    */

    //Lookup method injection
    /*
    @Lookup
    public  PrototypeDependency getPrototypeDependency() {
        // the implementation doesn't matter; it will be dynamically provided by Spring.
        return null;
    }
    */

    @Lookup
    public abstract PrototypeDependency getPrototypeDependency();
}
