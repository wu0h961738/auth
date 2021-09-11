package com.g0y.auth.oauth.thirdparty.interfacepack;


import com.g0y.auth.oauth.model.GetAccessTokenContext;
import com.g0y.auth.oauth.model.GetAuthPageUrlContext;

/**
 * implement OAuth2*/
public interface OAuth2 {

    String getAuthPageUrl(GetAuthPageUrlContext getAuthPageUrlContext);

    String getAccessToken(GetAccessTokenContext getAccessTokenContext);
}
