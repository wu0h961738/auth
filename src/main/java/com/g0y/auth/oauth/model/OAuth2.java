package com.g0y.auth.oauth.model;

import com.g0y.auth.exception.model.UnAuthorizedException;

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
    GetTokenInfoRs getTokenInfo(GetAccessTokenContext getAccessTokenContext) throws UnAuthorizedException;

    /**
     * verify by sending token to vendor, confirming if token is valid
     * */
    VerifyAccessTokenRs verifyToken(VerifyAccessTokenContext verifyAccessTokenContext) throws GeneralSecurityException, IOException;

    /**
     * decode payload from id token
     * 嘗試用invalid的idToken來解payload時，執行revoke token功能
     * @param idToken token received from provider
     * */
    GetPayloadInfoRs getUserInfo(String idToken) throws GeneralSecurityException, IOException;

    /** revoke token*/
    void revokeToken(String accessToken);
}
