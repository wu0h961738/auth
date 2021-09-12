package com.g0y.auth.component.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * bean controller
 * */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    /** application context*/
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringContextUtils.applicationContext == null){
            SpringContextUtils.applicationContext = applicationContext;
        }
    }

    /** getter : applicationContext */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /** Bean getter : name */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);

    }

    public static <T> T getBean(String name, Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}
