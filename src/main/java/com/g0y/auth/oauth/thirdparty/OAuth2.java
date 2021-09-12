package com.g0y.auth.oauth.thirdparty;


import com.g0y.auth.oauth.model.AccessToken;
import com.g0y.auth.oauth.model.GetAccessTokenContext;
import com.g0y.auth.oauth.model.GetAuthPageUrlContext;

/**
 * implement OAuth2
 * */
public interface OAuth2 {

    /**
     * get auth page provided by agency
     * */
    String getAuthPageUrl(GetAuthPageUrlContext getAuthPageUrlContext);

    String getAccessToken(GetAccessTokenContext getAccessTokenContext);
}
