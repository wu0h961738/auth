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
 * validate login status
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
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Optional<Cookie> cookieOfToken = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName() == this.getCookieName(request.getRequestURI())).findFirst();
        if(!cookieOfToken.isPresent()){
            // directly get into /gotoauthpage
            filterChain.doFilter(servletRequest, servletResponse);
        } else{
            // validate whether token exists in redis
            String ackTknKey = cookieOfToken.get().getValue();
            String accessToken = sessionService.getAccessToken(ackTknKey);
            VerifyAccessTokenContext verifyAccessTokenContext = new VerifyAccessTokenContext();
            verifyAccessTokenContext.setAccessToken(accessToken);
            if(!oAuthService.verifyAccessToken(verifyAccessTokenContext)){
                // directly get into /gotoauthpage
                filterChain.doFilter(servletRequest, servletResponse);
            } else{
                // redirect to success api
                request.getRequestDispatcher("/success").forward(servletRequest, servletResponse);
            }
        }
    }

    /**
     * get the name of agency providing auth service
     *
     * @param urlPath full context of url
     * */
    private String getCookieName(String urlPath){
        return AgencyEnum.getCookieNameByAgency(urlPath.substring(urlPath.lastIndexOf('/')+1));
    }
}
