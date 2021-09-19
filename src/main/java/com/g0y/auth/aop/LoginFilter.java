package com.g0y.auth.aop;

import com.g0y.auth.component.service.RedisSessionService;
import com.g0y.auth.constants.AgencyEnum;
import com.g0y.auth.oauth.OAuthService;
import com.g0y.auth.oauth.model.AccessToken;
import com.g0y.auth.oauth.model.VerifyAccessTokenContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * crux: In the filter, what should be confirmed confine in scope of which access token either is valid or exists.
 * validate login status
 * */
public class LoginFilter implements Filter {

    /** redis session server */
    @Autowired
    private RedisSessionService redisSessionService;

    @Autowired
    private OAuthService oAuthService;

    /**
     * redis key:value = cookieId : JWT token = ackTknKey : accessToken
     * */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            filterChain.doFilter(servletRequest, servletResponse);
        } else{
            String cookieNameStoringAcstkn = this.getCookieName(request.getRequestURI());
            Optional<Cookie> cookieOfToken = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieNameStoringAcstkn)).findFirst();
            if(!cookieOfToken.isPresent()){
                // directly get into /gotoauthpage
                filterChain.doFilter(servletRequest, servletResponse);
            } else{
                // validate whether token exists in redis
                String ackTknKey = cookieOfToken.get().getValue();
                AccessToken accessToken = redisSessionService.getAccessToken(ackTknKey);
                VerifyAccessTokenContext verifyAccessTokenContext = new VerifyAccessTokenContext();
                verifyAccessTokenContext.setAccessToken(accessToken.getAccess_token());
                if(!oAuthService.verifyAccessToken(verifyAccessTokenContext)){
                    // directly get into /gotoauthpage
                    filterChain.doFilter(servletRequest, servletResponse);
                } else{
                    // redirect to success api
                    request.setAttribute(AgencyEnum.getCookieNameByAgency(cookieNameStoringAcstkn), accessToken.getId_token());
                    request.getRequestDispatcher("/success").forward(servletRequest, servletResponse);
                }
            }
        }

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.addCookie(new Cookie("Line", (String) request.getSession().getAttribute("Line")));
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
