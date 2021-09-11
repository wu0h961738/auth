package com.g0y.auth.oauth.thirdparty.factory;

import com.g0y.auth.component.APIService;
import com.g0y.auth.oauth.thirdparty.interfacepack.OAuth2;

import java.lang.reflect.InvocationTargetException;

/**
 * get agency object dealing with OAuth process through static factory method
 * */
public final class OAuthFactory {
    public static OAuth2 getAgency(String agency) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // TODO 改成反射呼叫bean
        Class<? extends OAuth2> oAuth2Clazz = (Class<? extends OAuth2>) Class.forName("com.g0y.auth.oauth.thirdparty." + agency + "OAuth2");
        return oAuth2Clazz.getDeclaredConstructor().newInstance();
    }
}
