package com.g0y.auth.config;

import com.g0y.auth.oauth.GetOAuthPageServiceImpl;
import com.g0y.auth.oauth.interfacepack.GetOAuthPageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public GetOAuthPageService getOAuthPageService(){
        return new GetOAuthPageServiceImpl();
    }
}
