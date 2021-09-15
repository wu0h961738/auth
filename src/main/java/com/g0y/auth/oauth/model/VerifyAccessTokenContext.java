package com.g0y.auth.oauth.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * required context for verifying accessToken*/
@Data
@NoArgsConstructor
public class VerifyAccessTokenContext {

    /** access Token to be verified*/
    @NonNull
    String accessToken;
}
