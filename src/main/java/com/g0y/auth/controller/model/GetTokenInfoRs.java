package com.g0y.auth.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * response after getting access token from vendor
 * */
@Data
@NoArgsConstructor
public class GetTokenInfoRs {

    /**
     * key pointing full access token stored in redis
     * */
    private String hashKey;

    /**
     * payload including user info
     * */
    private String idToken;
}
