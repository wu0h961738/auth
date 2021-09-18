package com.g0y.auth.oauth.thirdparty;

import com.g0y.auth.component.APIService;
import com.g0y.auth.component.service.RedisService;
import com.g0y.auth.component.utils.CommonUtils;
import com.g0y.auth.constants.AgencyEnum;
import com.g0y.auth.oauth.model.AccessToken;
import com.g0y.auth.oauth.model.GetAccessTokenContext;
import com.g0y.auth.oauth.model.GetAuthPageUrlContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    @Autowired
    private RedisService redisService;

    /** scope requiring information of user. */
    private static final String[] SCOPE = {"openid", "profile"};

    @Override
    public String getAuthPageUrl(GetAuthPageUrlContext getAuthPageUrlContext) {
        return apiService.getLineWebLoginUrl(getAuthPageUrlContext.getState()
                , getAuthPageUrlContext.getNonce()
                , Arrays.asList(SCOPE));
    }

    @Override
    public String getHashKeyOfToken(GetAccessTokenContext getAccessTokenContext) throws Exception {
        AccessToken accessToken = apiService.accessToken(getAccessTokenContext.getAuthorizationCode());
        if(accessToken == null){
            throw new Exception("Invalid authorization code");
        }
        String idToken = accessToken.id_token;
        //verify token whether meeting standard (XSS check : compare hash(idToken+nonce) ?= token received)
        apiService.verifyIdToken(idToken, getAccessTokenContext.getNonce());

        // key : value =  : accessToken(id_token(payload))
        String hashKey = CommonUtils.generateTokenKey(AgencyEnum.LINE.getAgencyName());
        redisService.valueSet(hashKey, accessToken.access_token, Duration.ofDays(7));
        //return token and put it into header of httpresponse at handler layer
        return hashKey;
    }

}
