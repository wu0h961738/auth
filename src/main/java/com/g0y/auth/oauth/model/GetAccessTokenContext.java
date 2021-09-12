package com.g0y.auth.oauth.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * template of input*/
@Data
@NoArgsConstructor
public class GetAccessTokenContext {

    /**
     * code for requesting for access token
     * */
    @NonNull
    private String authorizationCode;

    /**
     * information scope permitted from user
     * */
    @NonNull
    private String scope;

    /**
     * security attribute hashed in session
     * */
    @NonNull
    private String nonce;

    /**
     * security attribute hashed in session
     * */
    private String state;


}
