package com.g0y.auth.oauth.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * response of verification
 * */
@Data
@NoArgsConstructor
public class VerifyAccessTokenRs {

    /**
     * valid flag
     * */
    private Boolean isValid;

    /**
     * content of IdToken*/
    private String idToken;

}
