package com.g0y.auth.aop;

import com.g0y.auth.component.service.SessionService;
import com.g0y.auth.constants.AgencyEnum;
import com.g0y.auth.oauth.OAuthService;
import com.g0y.auth.oauth.model.VerifyAccessTokenContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * TODO 驗證redis中的access token
 * */
public class LoginFilter implements Filter {

    /** redis session server */
    @Autowired
    private SessionService sessionService;

    @Autowired
    private OAuthService oAuthService;

    /**
     * TODO**************************** crux:這個階段只要驗證有沒有accessToken 或有沒有過期，如果都沒有的話就直接return，不需要後面的流程
     * redis template : key:value = ring:JWT token = ackTknKey : accessToken
     * */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        //TODO 前次取得Token的時候即存入cookie as key : value (base64(agencyName+"g0sessionkey") : idToken)
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Optional<Cookie> cookieOfToken = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName() == this.getAgencyName(request.getRequestURI())).findFirst();
        if(!cookieOfToken.isPresent()){
            // TODO 確認一下進入handler的的方法是不是直接return
            // or indicate specific handler : request.getRequestDispatcher("/gotoauthpage").forward(servletRequest, servletResponse);
            return;
        }

        //TODO 取得cookie 中 idToken as key 去查redis
        String ackTknKey = cookieOfToken.get().getValue();
        String accessToken = sessionService.getAccessToken(ackTknKey);
        VerifyAccessTokenContext verifyAccessTokenContext = new VerifyAccessTokenContext();
        verifyAccessTokenContext.setAccessToken(accessToken);
        if(!oAuthService.verifyAccessToken(verifyAccessTokenContext)){
            // TODO 確認一下進入handler的的方法是不是直接return
            return;
        }

        // TODO verification succeed : redirect to /success
        filterChain.doFilter(servletRequest, servletResponse);

        //
    }

    /**
     * get the name of agency providing auth service
     *
     * @param urlPath full context of url
     * */
    private String getAgencyName(String urlPath){
        //TODO Regular expression extract agency name
        //enum.getbyagencyname
    }
}
