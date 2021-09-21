package com.g0y.auth.oauth.line.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO of request calling back from auth page
 * */
@Data
@NoArgsConstructor
public class LineCallbackRq {

    /** authorization code */
    private String code;

    /** scope of user information */
    private String scope;

    /** secureCode */
    private String state;

    /** error info*/
    private String error;
    private String errorCode;
    private String errorMessage;
}
