package com.g0y.auth.controller;

import com.g0y.auth.component.APIService;
import com.g0y.auth.component.utils.CommonUtils;
import com.g0y.auth.constants.AgencyEnum;
import com.g0y.auth.constants.SessionEnum;
import com.g0y.auth.controller.model.AuthPageRq;
import com.g0y.auth.controller.model.GetTokenInfoRs;
import com.g0y.auth.oauth.OAuthService;
import com.g0y.auth.oauth.model.GetAuthPageUrlContext;
import com.g0y.auth.oauth.model.IdToken;
import com.g0y.auth.oauth.model.VerifyAccessTokenContext;
import com.g0y.auth.oauth.model.VerifyAccessTokenRs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /** key of session attribute*/
    private static final String NONCE = "nonce";

    /** OAuth method*/
    @Autowired
    OAuthService oAuthService;

    @Autowired
    APIService apiService;
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
    public String goToAuthPage(HttpSession httpSession) throws  InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        final String state = CommonUtils.randomAndEncodeWithBase64(32);
        final String nonce = CommonUtils.randomAndEncodeWithBase64(32);
        httpSession.setAttribute(LINE_WEB_LOGIN_STATE, state);
        httpSession.setAttribute(NONCE, nonce);

        GetAuthPageUrlContext getAuthPageUrlContext = new GetAuthPageUrlContext();
        getAuthPageUrlContext.setAgency((String) httpSession.getAttribute(SessionEnum.SESSION_KEY_AGENCY.getValue()));
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
            HttpSession httpSession,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "errorCode", required = false) String errorCode,
            @RequestParam(value = "errorMessage", required = false) String errorMessage) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (error != null || errorCode != null || errorMessage != null){
            return "redirect:/loginCancel";
        }
        if (!state.equals(httpSession.getAttribute(LINE_WEB_LOGIN_STATE))){
            return "redirect:/sessionError";
        }

        AuthPageRq authPageRq = new AuthPageRq();
        authPageRq.setCode(code);
        authPageRq.setState(state);
        GetTokenInfoRs tokenInfoRs = oAuthService.getToken(authPageRq, (String) httpSession.getAttribute(NONCE));
        httpSession.setAttribute(SessionEnum.SESSION_KEY_IDTOKEN.getValue(), tokenInfoRs.getIdToken());
        //TODO session could store list of redisKey = key : list<String> redisKey
        httpSession.setAttribute(SessionEnum.SESSION_KEY_REDISKEY.getValue(), tokenInfoRs.getHashKey());
        httpSession.setAttribute(SessionEnum.SESSION_KEY_AGENCY.getValue(), tokenInfoRs.getAgency());
        return "redirect:/success";
    }

    /**
     * <p> successPage
     * */
    @RequestMapping("/success")
    public String success(HttpServletResponse response, HttpSession httpSession, Model model) {
        httpSession.removeAttribute(NONCE);

        IdToken idToken = apiService.idToken((String) httpSession.getAttribute(SessionEnum.SESSION_KEY_IDTOKEN.getValue()));
        model.addAttribute("idToken", idToken);

        String keyName = AgencyEnum.getCookieNameByAgency((String) httpSession.getAttribute(SessionEnum.SESSION_KEY_AGENCY.getValue()));
        String cookieValue = (String) httpSession.getAttribute(SessionEnum.SESSION_KEY_REDISKEY.getValue());
        Cookie authentication = new Cookie(keyName, cookieValue);
        response.addCookie(authentication);

        //remove session attribute
        httpSession.removeAttribute(SessionEnum.SESSION_KEY_IDTOKEN.getValue());
        httpSession.removeAttribute(SessionEnum.SESSION_KEY_AGENCY.getValue());

        return "user/success";
    }

    /**
     * verify cookie value by querying redis
     * */
    @RequestMapping("/verify")
    public String verify(HttpSession httpSession) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        VerifyAccessTokenContext verifyAccessTokenContext = new VerifyAccessTokenContext();
        verifyAccessTokenContext.setAgencyName((String) httpSession.getAttribute(SessionEnum.SESSION_KEY_AGENCY.getValue()));
        verifyAccessTokenContext.setRedisKey((String) httpSession.getAttribute(SessionEnum.SESSION_KEY_REDISKEY.getValue()));
        VerifyAccessTokenRs verifyAccessTokenRs = oAuthService.verifyAccessToken(verifyAccessTokenContext);
        if(verifyAccessTokenRs.getIsValid()){
            httpSession.setAttribute(SessionEnum.SESSION_KEY_IDTOKEN.getValue(), verifyAccessTokenRs.getIdToken());
            return "redirect:/success";
        } else{
            return "redirect:/gotoauthpage/";
        }
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
