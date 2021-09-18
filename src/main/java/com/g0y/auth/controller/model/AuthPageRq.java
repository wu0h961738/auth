package com.g0y.auth.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 *  request sent back from auth page provided by vendor
 * */
@Data
@NoArgsConstructor
public class AuthPageRq {

    /** authorization code */
    @NonNull
    private String code;

    /** secure attribute*/
    @NonNull
    private String state;

//    /** information scope */
//    @NonNull
//    private String scope;
//
//    @NonNull
//    private String nonce;

    /** error */
    private String error;

    private String errorCode;

    private String errorMessage;
}
