package com.g0y.auth.oauth;

import com.g0y.auth.component.utils.SpringContextUtils;
import com.g0y.auth.oauth.model.GetAuthPageUrlContext;
import com.g0y.auth.oauth.thirdparty.interfacepack.OAuth2;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** whole OAuth Process*/
@Service
public class OAuthService {

    /** name of function getting Auth url*/
    private static final String NAME_OF_METHOD = "getAuthPageUrl";

    /**
     * get Auth page provided by vendor
     *
     * @param getAuthPageUrlContext context
     * */
    public String getUrl(GetAuthPageUrlContext getAuthPageUrlContext) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object beanObj = SpringContextUtils.getBean(getAuthPageUrlContext.getAgency() + "OAuth2");
        Class<? extends OAuth2> agencyObj = (Class<? extends OAuth2>) beanObj.getClass();
        Method getUrlMethod = agencyObj.getMethod(NAME_OF_METHOD, GetAuthPageUrlContext.class);
        return (String) getUrlMethod.invoke(beanObj, getAuthPageUrlContext);
    }

}
