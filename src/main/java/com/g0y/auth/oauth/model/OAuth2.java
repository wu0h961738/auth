package com.g0y.auth.oauth.model;

import com.g0y.auth.controller.model.GetTokenInfoRs;

import java.io.IOException;
import java.security.GeneralSecurityException;

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
    GetTokenInfoRs getTokenInfo(GetAccessTokenContext getAccessTokenContext) throws Exception;

    /**
     * verify by sending token to vendor, confirming if token is valid
     * */
    VerifyAccessTokenRs verifyToken(VerifyAccessTokenContext verifyAccessTokenContext);

    /**
     * decode payload from id token
     *
     * @param idToken token received from provider
     * */
    GetPayloadInfoRs getUserInfo(String idToken) throws GeneralSecurityException, IOException;
}
