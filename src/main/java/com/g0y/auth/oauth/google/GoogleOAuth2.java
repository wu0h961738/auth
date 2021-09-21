package com.g0y.auth.oauth.google;

import com.g0y.auth.component.service.RedisSessionService;
import com.g0y.auth.component.utils.CommonUtils;
import com.g0y.auth.constants.AgencyEnum;
import com.g0y.auth.controller.model.GetTokenInfoRs;
import com.g0y.auth.oauth.google.component.GoogleOAuthService;
import com.g0y.auth.oauth.line.model.AccessToken;
import com.g0y.auth.oauth.model.GetAccessTokenContext;
import com.g0y.auth.oauth.model.GetAuthPageUrlContext;
import com.g0y.auth.oauth.model.VerifyAccessTokenContext;
import com.g0y.auth.oauth.model.VerifyAccessTokenRs;
import com.g0y.auth.oauth.model.OAuth2;
import com.google.api.client.googleapis.auth.oauth2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
    public GetTokenInfoRs getTokenInfo(GetAccessTokenContext getAccessTokenContext) throws Exception {
        GoogleTokenResponse tokenResponse = googleOAuthService.getAccessToken(getAccessTokenContext.getAuthorizationCode());

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
    public VerifyAccessTokenRs verifyToken(VerifyAccessTokenContext verifyAccessTokenContext) {
        return null;
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
