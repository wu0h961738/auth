package com.g0y.auth.oauth.thirdparty;

import com.g0y.auth.component.APIService;
import com.g0y.auth.component.service.RedisService;
import com.g0y.auth.component.service.RedisSessionService;
import com.g0y.auth.component.utils.CommonUtils;
import com.g0y.auth.constants.AgencyEnum;
import com.g0y.auth.controller.model.GetTokenInfoRs;
import com.g0y.auth.oauth.model.*;
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
    private RedisSessionService redisSessionService;

    /** scope requiring information of user. */
    private static final String[] SCOPE = {"openid", "profile"};

    @Override
    public String getAuthPageUrl(GetAuthPageUrlContext getAuthPageUrlContext) {
        return apiService.getLineWebLoginUrl(getAuthPageUrlContext.getState()
                , getAuthPageUrlContext.getNonce()
                , Arrays.asList(SCOPE));
    }

    @Override
    public GetTokenInfoRs getTokenInfo(GetAccessTokenContext getAccessTokenContext) throws Exception {
        AccessToken accessToken = apiService.accessToken(getAccessTokenContext.getAuthorizationCode());
        if(accessToken == null){
            throw new Exception("Invalid authorization code");
        }
        String idToken = accessToken.getId_token();
        //verify token if meeting standard (XSS check : compare hash(idToken+nonce) ?= token received)
        apiService.verifyIdToken(idToken, getAccessTokenContext.getNonce());

        // key : value =  : accessToken(id_token(payload))
        String hashKey = CommonUtils.generateTokenKey(AgencyEnum.LINE.getAgencyName());
        redisSessionService.setAccessToken(hashKey, accessToken);

        //return token and put it into header of httpresponse at handler layer
        GetTokenInfoRs getTokenInfoRs = new GetTokenInfoRs();
        getTokenInfoRs.setHashKey(hashKey);
        getTokenInfoRs.setIdToken(idToken);
        getTokenInfoRs.setAgency(AgencyEnum.LINE.getAgencyName());
        return getTokenInfoRs;
    }

    @Override
    public VerifyAccessTokenRs verifyToken(VerifyAccessTokenContext verifyAccessTokenContext) {
        AccessToken accessToken = redisSessionService.getAccessToken(verifyAccessTokenContext.getRedisKey());
        Verify verify = apiService.verify(accessToken);
        Boolean isValid = verify.expires_in >0;
        VerifyAccessTokenRs verifyAccessTokenRs = new VerifyAccessTokenRs();
        verifyAccessTokenRs.setIsValid(isValid);
        if(isValid){
            verifyAccessTokenRs.setIdToken(accessToken.getId_token());
        }
        return verifyAccessTokenRs;
    }

}
