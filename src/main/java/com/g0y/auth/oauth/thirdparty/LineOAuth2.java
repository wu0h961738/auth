package com.g0y.auth.oauth.thirdparty;

import com.g0y.auth.component.APIService;
import com.g0y.auth.oauth.model.AccessToken;
import com.g0y.auth.oauth.model.GetAccessTokenContext;
import com.g0y.auth.oauth.model.GetAuthPageUrlContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * implement OAuth2 provided by LINE
 * */
@Service
public class LineOAuth2 implements OAuth2 {

    /**
     * API component
     * */
    @Autowired
    private APIService apiService;

    /** scope requiring information of user. */
    private static final String[] SCOPE = {"openid", "profile"};

    @Override
    public String getAuthPageUrl(GetAuthPageUrlContext getAuthPageUrlContext) {
        return apiService.getLineWebLoginUrl(getAuthPageUrlContext.getState()
                , getAuthPageUrlContext.getNonce()
                , Arrays.asList(SCOPE));
    }

    @Override
    public String getAccessToken(GetAccessTokenContext getAccessTokenContext) {
        AccessToken accessToken = apiService.accessToken(getAccessTokenContext.getAuthorizationCode());
        if(accessToken == null){
            // TODO throw new exception
        }
        String idToken = accessToken.id_token;
        //verify token whether meeting standard (XSS check : compare hash(idToken+nonce) ?= token received)
        apiService.verifyIdToken(idToken, getAccessTokenContext.getNonce());

        // TODO put it into redis
        // id_token(payload) : accessToken

        //return token and put it into header of httpresponse at handler layer
        return accessToken.id_token;
    }
}
