package com.g0y.auth.oauth.thirdparty;

import com.g0y.auth.component.APIService;
import com.g0y.auth.oauth.model.GetAccessTokenContext;
import com.g0y.auth.oauth.model.GetAuthPageUrlContext;
import com.g0y.auth.oauth.thirdparty.interfacepack.OAuth2;
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
        return null;
    }
}
