package com.g0y.auth.oauth;

import com.g0y.auth.component.utils.SpringContextUtils;
import com.g0y.auth.controller.model.AuthPageRq;
import com.g0y.auth.oauth.model.AccessToken;
import com.g0y.auth.oauth.model.GetAccessTokenContext;
import com.g0y.auth.oauth.model.GetAuthPageUrlContext;
import com.g0y.auth.oauth.model.VerifyAccessTokenContext;
import com.g0y.auth.oauth.thirdparty.OAuth2;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * whole OAuth Process
 * */
@Service
public class OAuthService {

    /** name of method getting Auth url*/
    private static final String AUTHPAGE_URL = "getAuthPageUrl";

    /** name of method getting access token*/
    private static final String ACCESSTOKEN = "getAccessToken";

    /** suffix of class implementing oauth of which vendor provides*/
    private static final String SUFFIX_OF_CLASS = "OAuth2";

    /** name of vendor */
    private String agency;

    /**
     * get Auth page provided by vendor
     *
     * @param getAuthPageUrlContext context
     * */
    public String getUrl(GetAuthPageUrlContext getAuthPageUrlContext) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        agency = getAuthPageUrlContext.getAgency();
        Object beanObj = SpringContextUtils.getBean(agency + SUFFIX_OF_CLASS);
        Class<? extends OAuth2> agencyObj = (Class<? extends OAuth2>) beanObj.getClass();
        Method getUrlMethod = agencyObj.getMethod(AUTHPAGE_URL, GetAuthPageUrlContext.class);
        return (String) getUrlMethod.invoke(beanObj, getAuthPageUrlContext);
    }

    /**
     * request for accessToken
     * */
    public String getAccessToken(AuthPageRq authPageRq) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object beanObj = SpringContextUtils.getBean(agency + SUFFIX_OF_CLASS);
        Class<? extends OAuth2> agencyObj = (Class<? extends OAuth2>) beanObj.getClass();

        GetAccessTokenContext getAccessTokenContext = new GetAccessTokenContext();
        getAccessTokenContext.setAuthorizationCode(authPageRq.getCode());
        getAccessTokenContext.setScope(authPageRq.getScope());
        getAccessTokenContext.setNonce(authPageRq.getNonce());
        getAccessTokenContext.setState(authPageRq.getState());
        Method getUrlMethod = agencyObj.getMethod(ACCESSTOKEN, GetAccessTokenContext.class);
        return (String) getUrlMethod.invoke(beanObj, getAccessTokenContext);
    }

    /**
     * verify token by api requesting agency
     *
     * @param verifyAccessTokenContext context containing params for verifying access Token
     * */
    public Boolean verifyAccessToken(VerifyAccessTokenContext verifyAccessTokenContext){
        // Todo yet implement
        return true;
    }
}
