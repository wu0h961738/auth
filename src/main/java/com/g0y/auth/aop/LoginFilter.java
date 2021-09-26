package com.g0y.auth.aop;

import com.g0y.auth.constants.AgencyEnum;
import com.g0y.auth.constants.SessionEnum;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * crux: In the filter, what should be confirmed confine in scope of which access token either is valid or exists.
 * validate login status
 * */
@Slf4j
public class LoginFilter implements Filter {

    /**
     * redis key:value = cookieId : JWT token = ackTknKey : accessToken
     * */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.info(" login filter in");
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //set agencyName defined in enum to session
        HttpSession httpSession = request.getSession();
        String url = request.getRequestURI();
        httpSession.setAttribute(SessionEnum.SESSION_KEY_AGENCY.getValue(), AgencyEnum.getAgencyNameByUri(url));
        //check login record by checking coookie
        Cookie[] cookies = request.getCookies();
        String cookieNameStoringAcstkn = AgencyEnum.getCookieNameByUri(url);
        if(cookies != null){
            Optional<Cookie> cookieOfToken = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieNameStoringAcstkn)).findFirst();
            if(cookieOfToken.isPresent()){
                // validate whether token exists in redis
                String sessionObjJWT = cookieOfToken.get().getValue();
                httpSession.setAttribute(SessionEnum.SESSION_JWT.getValue(), sessionObjJWT);
                request.getRequestDispatcher("/verify").forward(request, servletResponse);
                log.info("trans to verify page");
                return ;
            }
        }
        filterChain.doFilter(request, servletResponse);
        log.info("login filter out");
    }

}
