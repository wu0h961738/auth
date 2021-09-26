package com.g0y.auth.oauth.google;

import com.g0y.auth.component.service.RedisSessionService;
import com.g0y.auth.component.utils.CommonUtils;
import com.g0y.auth.constants.AgencyEnum;
import com.g0y.auth.oauth.model.GetTokenInfoRs;
import com.g0y.auth.exception.model.UnAuthorizedException;
import com.g0y.auth.oauth.google.component.GoogleOAuthService;
import com.g0y.auth.oauth.line.model.AccessToken;
import com.g0y.auth.oauth.model.*;
import com.google.api.client.googleapis.auth.oauth2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;


/**
 * Oauth implementation with Google
 * */
@Service
public class GoogleOAuth2 implements OAuth2 {

    /** component dealing with OAuth*/
    @Autowired
    private GoogleOAuthService googleOAuthService;

    @Autowired
    private RedisSessionService redisSessionService;

    @Override
    public String getAuthPageUrl(GetAuthPageUrlContext getAuthPageUrlContext) {
        return googleOAuthService.getAuthUrl(getAuthPageUrlContext.getState());
    }

    @Override
    public GetTokenInfoRs getTokenInfo(GetAccessTokenContext getAccessTokenContext) throws UnAuthorizedException {
        GoogleTokenResponse tokenResponse;
        try{
            tokenResponse = googleOAuthService.getAccessToken(getAccessTokenContext.getAuthorizationCode());
        } catch(IOException ioe){
            throw new UnAuthorizedException("");
        }

        // redis - key : value =  : accessToken(id_token(payload))
        String hashKey = CommonUtils.generateTokenKey(AgencyEnum.GOOGLE.getAgencyName());
        redisSessionService.setAccessToken(hashKey, serializeToToken(tokenResponse));

        GetTokenInfoRs getTokenInfoRs = new GetTokenInfoRs();
        getTokenInfoRs.setIdToken(tokenResponse.getIdToken());
        getTokenInfoRs.setAgency(AgencyEnum.GOOGLE.getAgencyName());
        getTokenInfoRs.setHashKey(hashKey);

        return getTokenInfoRs;
    }

    @Override
    public VerifyAccessTokenRs verifyToken(VerifyAccessTokenContext verifyAccessTokenContext) throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = googleOAuthService.getPayload(""); // TODO update session management supposed to be passed by context
        VerifyAccessTokenRs verifyAccessTokenRs = new VerifyAccessTokenRs();
        verifyAccessTokenRs.setIdToken(""); // TODO update session management
        verifyAccessTokenRs.setIsValid(payload != null); //null if expired
        return null;
    }

    @Override
    public GetPayloadInfoRs getUserInfo(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = googleOAuthService.getPayload(idToken);
        GetPayloadInfoRs getPayloadInfoRs = new GetPayloadInfoRs();
        getPayloadInfoRs.setName((String) payload.get("name"));
        getPayloadInfoRs.setPicture((String) payload.get("picture"));
        return getPayloadInfoRs;
    }

    /**
     * serialize to the format used to store in redis
     *
     * @param tokenResponse pojo of token sent from google
     * */
    private AccessToken serializeToToken( GoogleTokenResponse tokenResponse){
        AccessToken accessToken = new AccessToken();
        accessToken.setAccess_token(tokenResponse.getAccessToken());
        accessToken.setId_token(tokenResponse.getIdToken());
        accessToken.setExpires_in(String.valueOf(tokenResponse.getExpiresInSeconds()));
        accessToken.setToken_type(tokenResponse.getTokenType());
        accessToken.setScope(tokenResponse.getScope());
        accessToken.setRefresh_token(tokenResponse.getRefreshToken());
        return accessToken;
    }
}
