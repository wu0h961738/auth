package com.g0y.auth.oauth.thirdparty;

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

    /**
     * get the redis key pointing access token
     * */
    String getHashKeyOfToken(GetAccessTokenContext getAccessTokenContext) throws Exception;
}
