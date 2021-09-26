package com.g0y.auth.oauth.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Unified access token template for AP
 * */
@Data
@NoArgsConstructor
public class GetTokenInfoRs {

    /** */
    private String scope;

    /** (short-lived) access token*/
    private String accessToken;

    /** default : Bearer */
    private String tokenType;

    /** expired time, unit : sec */
    private String expiresIn;

    /** (long-lived) refresh token */
    private String refreshToken;

    /** user info payload */
    private String idToken;
}
