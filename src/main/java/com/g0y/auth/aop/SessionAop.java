package com.g0y.auth.aop;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Aspect
@Component
@Order(1)
public class SessionAop {

    @Autowired
    private HttpSession session;

    /**
     * cut the aspect at certain method redirecting auth page and set session attribute:
     * state
     * nonce */
    @Pointcut(value = "execution(* com.g0y.auth.controller.OAuthController.goToAuthPage( .. ))")
    public void setSecureAttributesPointCut(){}

    @Before("setSecureAttributesPointCut()")
    public void doBefore(){

    }

}
