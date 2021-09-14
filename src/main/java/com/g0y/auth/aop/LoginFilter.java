package com.g0y.auth.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g0y.auth.component.service.SessionService;
import com.g0y.auth.component.service.model.AccessTokenInfo;
import com.g0y.auth.oauth.model.AccessToken;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Optional;

/**
 * TODO 驗證redis中的access token
 * */
@Aspect
@Component
@Order(1)
public class LoginFilter {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private ObjectMapper objectMapper;

    /** redis session server */
    @Autowired
    private SessionService sessionService;

    /**
     * validate the cookie at login phase */
    @Pointcut(value = "execution(* com.g0y.auth.controller.OAuthController.login( .. ))")
    public void validateTokenCut(){}

    /**
     * TODO**************************** crux:這個階段只要驗證有沒有accessToken 或有沒有過期，如果都沒有的話就直接return，不需要後面的流程
     * */
    @Before("validateTokenCut()")
    public void doBefore(){
        //TODO 前次取得Token的時候即存入cookie as key : value (base64(任意字串) : idToken)
        Optional<Cookie> cookieOfToken = Arrays.stream(httpServletRequest.getCookies()).filter(cookie -> cookie.getName() == "").findFirst();
        if(!cookieOfToken.isPresent()){
            // TODO 確認一下進入handler的的方法是不是直接return
            return;
        }

        //TODO 取得cookie 中 idToken as key 去查redis
        String cookieId = cookieOfToken.get().getValue();

        // TODO 記得加一段邏輯是：redis沒找到東西也直接走handler

        //cookieId(random yielded at first time) as the key of redis，getAccessToken
        AccessTokenInfo acstkn = sessionService.getAccessTokenInfo(cookieId);
        if(acstkn == null){
            sessionService.newSession(cookieId);
        }

        // deserialize
        //AccessToken accessToken = objectMapper.convertValue(, AccessToken.class);

        // TODO 驗證token是否到期
//        if(accessToken.expires_in < 0){
//            return;
//        }

        // 看有沒有過期，過期的話就正常驗證，沒有的話就直接載入使用者資訊
        httpSession.setAttribute("isValid", true);
    }
}
