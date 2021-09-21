package com.g0y.auth.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * context input for adapter
 * */
@Data
@NoArgsConstructor
public class AuthPageAdapterContext {

    /** request sent from provider */
    private Map<String, String> requestParams;

    /** name of provider */
    private String agency;

    /** secure code yielded before called back */
    private String nonce;
}
