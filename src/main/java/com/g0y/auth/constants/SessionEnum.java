package com.g0y.auth.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * enum describing session factor
 * */
@Getter
@AllArgsConstructor
public enum SessionEnum {

    SESSION_KEY_REDISKEY("hashKey"),
    SESSION_KEY_IDTOKEN("idToken"),
    SESSION_KEY_AGENCY("agency");

    private String value;

}
