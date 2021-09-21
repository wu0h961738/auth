package com.g0y.auth.oauth.line.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * full context of access token*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class AccessToken {

    private String scope;
    private String access_token;
    private String token_type;
    private String expires_in;
    private String refresh_token;
    private String id_token;


}
