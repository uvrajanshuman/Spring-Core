package com.refstash.bean_scope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) //This statement is redundant - singleton is default scope
//@Scope("singleton")
public class SingletonBean {
}
