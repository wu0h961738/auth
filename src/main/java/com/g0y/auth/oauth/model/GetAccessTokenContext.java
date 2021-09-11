package com.g0y.auth.oauth.model;

import lombok.Data;
import lombok.NonNull;

/**
 * template of input*/
@Data
public class GetAccessTokenContext {

    /**
     * code for requesting for access token
     * */
    @NonNull
    String authorizationCode;

    /**
     * information scope permitted from user
     * */
    @NonNull
    String scope;

    /**
     * security attribute hashed in last session
     * */
    String state;


}
