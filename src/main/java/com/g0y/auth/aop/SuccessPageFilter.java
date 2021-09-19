package com.g0y.auth.aop;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * crux: In the filter, mainly set cookie with redis key
 * */
@Slf4j
public class SuccessPageFilter implements Filter {

    /**
     * redis key:value = cookieId : JWT token = ackTknKey : accessToken
     * */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.info(" success page filter in");
        filterChain.doFilter(servletRequest, servletResponse);
        log.info(" success page filter out");
    }
}
