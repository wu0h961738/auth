package com.g0y.auth.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * full context of access token*/
@Data
@AllArgsConstructor
public final class AccessToken {

    private final String scope;
    private final String access_token;
    private final String token_type;
    private final Integer expires_in;
    private final String refresh_token;
    private final String id_token;


}
