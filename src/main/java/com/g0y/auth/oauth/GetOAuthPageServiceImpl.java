package com.g0y.auth.oauth;

import com.g0y.auth.oauth.interfacepack.GetOAuthPageService;
import com.g0y.auth.oauth.model.GetAuthPageUrlContext;
import com.g0y.auth.oauth.thirdparty.factory.OAuthFactory;
import com.g0y.auth.oauth.thirdparty.interfacepack.OAuth2;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;

public class GetOAuthPageServiceImpl implements GetOAuthPageService {

    @Autowired
    public GetOAuthPageServiceImpl(){}

    @Override
    public String getUrl(GetAuthPageUrlContext getAuthPageUrlContext) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        OAuth2 agencyObj = OAuthFactory.getAgency(getAuthPageUrlContext.getAgency());
        return agencyObj.getAuthPageUrl(getAuthPageUrlContext);
    }
}
