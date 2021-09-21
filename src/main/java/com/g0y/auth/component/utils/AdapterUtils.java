package com.g0y.auth.component.utils;

import com.g0y.auth.controller.model.AuthPageAdapterContext;
import com.g0y.auth.controller.model.AuthPageRq;
import com.g0y.auth.oauth.google.component.GoogleOAuthAdapter;
import com.g0y.auth.oauth.line.component.LineOAuthAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * request / response adapter
 * */
@Component
public class AdapterUtils {

    /**
     * adapter for line
     * */
    @Autowired
    private LineOAuthAdapter lineOAuthAdapter;

    /**
     * adapter for google
     * */
    @Autowired
    private GoogleOAuthAdapter googleOAuthAdapter;


    /**
     * adapt to AuthPageRq
     * */
    public AuthPageRq getAuthPageRq(AuthPageAdapterContext authPageAdapterContext){
        AuthPageRq authPageRq= null;
        String agency = authPageAdapterContext.getAgency();
        Map<String, String> callbackRq = authPageAdapterContext.getRequestParams();
        switch(agency){
            case "line":
                authPageRq = lineOAuthAdapter.getRequest(callbackRq);
                break;
            case "google":
                authPageRq = googleOAuthAdapter.getRequest(callbackRq);
                break;
            default:
        }
        return  authPageRq;
    }

}
