package com.g0y.auth.controller;

import com.g0y.auth.component.utils.CommonUtils;
import com.g0y.auth.oauth.OAuthService;
import com.g0y.auth.oauth.model.GetAuthPageUrlContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;

@Controller
@RequestMapping("/")
public class OAuthController {

    private static final String LINE_WEB_LOGIN_STATE = "lineWebLoginState";
    static final String ACCESS_TOKEN = "accessToken";
    private static final String NONCE = "nonce";

    @Autowired
    OAuthService oAuthService;
    /**
     * <p>LINE Login Button Page
     * <p>Login Type is to log in on any desktop or mobile website
     */
    @RequestMapping("/")
    public String login() {
        return "user/login";
    }

    /**
     * <p>Redirect to Login Page</p>
     */
    @RequestMapping(value = "/gotoauthpage/{agency}")
    public String goToAuthPage(HttpSession httpSession, @PathVariable("agency") String agency) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //TODO session類的拉到filter層做掉
        final String state = CommonUtils.randomAndEncodeWithBase64();
        final String nonce = CommonUtils.randomAndEncodeWithBase64();
        httpSession.setAttribute(LINE_WEB_LOGIN_STATE, state);
        httpSession.setAttribute(NONCE, nonce);

        GetAuthPageUrlContext getAuthPageUrlContext = new GetAuthPageUrlContext();
        getAuthPageUrlContext.setAgency(agency);
        getAuthPageUrlContext.setNonce(nonce);
        getAuthPageUrlContext.setState(state);
        String url = oAuthService.getUrl(getAuthPageUrlContext);

        return "redirect:" + url;
    }

    /**
     * <p>Redirect Page from LINE Platform</p>
     * <p>Login Type is to log in on any desktop or mobile website
     */
    @RequestMapping("/auth")
    public String auth(
            HttpSession httpSession,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "errorCode", required = false) String errorCode,
            @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        return "gg";
    }
}
