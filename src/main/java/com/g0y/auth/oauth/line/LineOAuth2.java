package com.g0y.auth.oauth.line;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.g0y.auth.exception.constant.ErrorMessageEnum;
import com.g0y.auth.exception.model.UnAuthorizedException;
import com.g0y.auth.oauth.line.component.LineAPIService;
import com.g0y.auth.component.utils.CommonUtils;
import com.g0y.auth.constants.AgencyEnum;
import com.g0y.auth.oauth.model.GetTokenInfoRs;
import com.g0y.auth.oauth.line.model.AccessToken;
import com.g0y.auth.oauth.line.model.IdToken;
import com.g0y.auth.oauth.line.model.Verify;
import com.g0y.auth.oauth.model.*;
import com.g0y.auth.oauth.model.OAuth2;
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
    private LineAPIService lineApiService;


    /** scope requiring information of user. */
    private static final String[] SCOPE = {"openid", "profile"};

    @Override
    public String getAuthPageUrl(GetAuthPageUrlContext getAuthPageUrlContext) {
        return lineApiService.getLineWebLoginUrl(getAuthPageUrlContext.getState()
                , getAuthPageUrlContext.getNonce()
                , Arrays.asList(SCOPE));
    }

    @Override
    public GetTokenInfoRs getTokenInfo(GetAccessTokenContext getAccessTokenContext) throws UnAuthorizedException{
        AccessToken accessToken = lineApiService.accessToken(getAccessTokenContext.getAuthorizationCode());
        if(accessToken == null){
            throw new UnAuthorizedException(ErrorMessageEnum.INVALID_AUTH_CODE.getErrorMessage());
        }
        String idToken = accessToken.getId_token();
        //verify token if meeting standard (XSS check : compare hash(idToken+nonce) ?= token received)
        lineApiService.verifyIdToken(idToken, getAccessTokenContext.getNonce());

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
        Verify verify = lineApiService.verify(accessToken);
        Boolean isValid = verify.expires_in >0;
        VerifyAccessTokenRs verifyAccessTokenRs = new VerifyAccessTokenRs();
        verifyAccessTokenRs.setIsValid(isValid);
        if(isValid){
            verifyAccessTokenRs.setIdToken(accessToken.getId_token());
        }
        return verifyAccessTokenRs;
    }

    @Override
    public GetPayloadInfoRs getUserInfo(String idToken) {
        IdToken payload;
        try{
            payload = lineApiService.idToken(idToken);
        } catch (JWTDecodeException jwtDecodeException){
            throw new UnAuthorizedException("Invalid Id token.");
        }

        GetPayloadInfoRs getPayloadInfoRs = new GetPayloadInfoRs();
        getPayloadInfoRs.setName(payload.name);
        getPayloadInfoRs.setPicture(payload.picture);
        return getPayloadInfoRs;
    }

    @Override
    public void revokeToken(String accessToken) {
        AccessToken tkn = new AccessToken();
        tkn.setAccess_token(accessToken);
        lineApiService.revoke(tkn);
    }

}
