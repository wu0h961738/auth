package com.g0y.auth.controller;

import com.g0y.auth.component.utils.CommonUtils;
import com.g0y.auth.controller.model.AuthPageRq;
import com.g0y.auth.oauth.OAuthService;
import com.g0y.auth.oauth.model.AccessToken;
import com.g0y.auth.oauth.model.GetAuthPageUrlContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;

/**
 * OAuth controller
 * */
@Controller
@RequestMapping("/")
public class OAuthController {

    /** key of session attribute*/
    private static final String LINE_WEB_LOGIN_STATE = "lineWebLoginState";

    static final String ACCESS_TOKEN = "accessToken";

    /** key of session attribute*/
    private static final String NONCE = "nonce";

    /** OAuth method*/
    @Autowired
    OAuthService oAuthService;

    /**
     * <p>LINE Login Button Page
     * <p>Login Type is to log in on any desktop or mobile website
     */
    @RequestMapping("/")
    public String login(HttpSession httpSession) {
        //session attribute given by filter layer
        Object isValid = httpSession.getAttribute("isValid");
        if( isValid != null && (boolean)isValid){
            httpSession.removeAttribute("isValid");
            return "redirect:/success";
        }
        return "user/login";
    }

    /**
     * <p>Redirect to Login Page</p>
     */
    @RequestMapping(value = "/gotoauthpage/{agency}")
    public String goToAuthPage(HttpSession httpSession, @PathVariable("agency") String agency) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // TODO
        //  1. agency 用enum管理(過濾不合法名稱)
        //  2. enum管理 state, nonce attribute keyname
        final String state = CommonUtils.randomAndEncodeWithBase64(32);
        final String nonce = CommonUtils.randomAndEncodeWithBase64(32);
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
     * <p>Redirect Page from Platform</p>
     * <p>Login Type is to log in on any desktop or mobile website
     */
    @RequestMapping("/auth")
    public String auth(
            HttpSession httpSession, @RequestBody AuthPageRq authPageRq) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        if (error != null || errorCode != null || errorMessage != null){
//            return "redirect:/loginCancel";
//        }
//
//        if (!state.equals(httpSession.getAttribute(LINE_WEB_LOGIN_STATE))){
//            return "redirect:/sessionError";
//        }
        httpSession.removeAttribute(LINE_WEB_LOGIN_STATE);
        authPageRq.setNonce((String) httpSession.getAttribute(NONCE));
        String token = oAuthService.getAccessToken(authPageRq);
        httpSession.setAttribute(ACCESS_TOKEN, token);
        return "redirect:/success";
    }

    /**
     * <p>login Cancel Page
     */
    @RequestMapping("/loginCancel")
    public String loginCancel() {
        return "user/login_cancel";
    }

    /**
     * <p>Session Error Page
     */
    @RequestMapping("/sessionError")
    public String sessionError() {
        return "user/session_error";
    }
}
