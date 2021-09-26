package com.g0y.auth.session.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * structure to be encoded in JWT and to be stored as session value in cookie
 * */
@Data
@NoArgsConstructor
public class SessionObject {

    /** id pointing to the slot in which access token payload stores in redis. */
    private String sessionId;

    /** key pointing to the slot storing refresh token */
    private String keyForRefreshToken;
}
