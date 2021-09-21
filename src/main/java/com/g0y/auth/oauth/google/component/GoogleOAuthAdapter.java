package com.g0y.auth.oauth.google.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g0y.auth.controller.model.AuthPageRq;
import com.g0y.auth.oauth.google.model.GoogleCallbackRq;
import com.g0y.auth.oauth.model.AuthPageRequestAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * formating request param from platform*/
@Component
public class GoogleOAuthAdapter implements AuthPageRequestAdapter {

    /** object mapper */
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public AuthPageRq getRequest(Map<String, String> requestParams) {
        GoogleCallbackRq googleCallbackRq = objectMapper.convertValue(requestParams, GoogleCallbackRq.class);
        AuthPageRq authPageRq = new AuthPageRq();
        authPageRq.setCode(googleCallbackRq.getCode());
        authPageRq.setState(googleCallbackRq.getState());
        // nonce : set up on response layer
        return authPageRq;
    }
}
