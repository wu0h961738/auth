package com.g0y.auth.configuration;

import com.g0y.auth.aop.LoginFilter;
import com.g0y.auth.aop.SuccessPageFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * config bean for interceptor-ish function
 * */
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean gotoAuthFilterRegistration(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new LoginFilter());
        registrationBean.setName("LoginFilter");
        registrationBean.addUrlPatterns("/gotoauthpage/*"); //only intercept handler: /gotoauthpage
        registrationBean.setOrder(1); //set up with the highest priority
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean successFilterRegistration(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new SuccessPageFilter());
        registrationBean.setName("SuccessPageFilter");
        registrationBean.addUrlPatterns("/success"); //only intercept handler: /success
        registrationBean.setOrder(1); //set up with the highest priority
        return registrationBean;
    }
}
