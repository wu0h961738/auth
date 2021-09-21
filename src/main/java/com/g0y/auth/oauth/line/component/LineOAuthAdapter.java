package com.g0y.auth.oauth.line.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g0y.auth.controller.model.AuthPageRq;
import com.g0y.auth.oauth.line.model.LineCallbackRq;
import com.g0y.auth.oauth.model.AuthPageRequestAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * format request param from platform*/
@Component
public class LineOAuthAdapter implements AuthPageRequestAdapter {

    /** object mapper */
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public AuthPageRq getRequest(Map<String, String> requestParams) {
        LineCallbackRq lineCallbackRq = objectMapper.convertValue(requestParams, LineCallbackRq.class);
        AuthPageRq authPageRq = new AuthPageRq();
        authPageRq.setCode(lineCallbackRq.getCode());
        authPageRq.setState(lineCallbackRq.getState());
        // nonce : set up on response layer
        return authPageRq;
    }
}
