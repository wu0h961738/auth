package com.g0y.auth.oauth.interfacepack;

import com.g0y.auth.oauth.model.GetAuthPageUrlContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * get Url of OAuth page
 * */
@Service
public interface GetOAuthPageService {

    /**
     * offer the auth page of agency
     *
     * @return Url of auth page
     * */
    String getUrl(GetAuthPageUrlContext getAuthPageUrlContext) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
