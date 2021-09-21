package com.g0y.auth.oauth.google.model;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * request called back from Google */
@Data
@NoArgsConstructor
public class GoogleCallbackRq {

    /** token to get authorization code */
    private String code;

    private String state;

    private String scope;

    private String authuser;

    private String prompt;



}
