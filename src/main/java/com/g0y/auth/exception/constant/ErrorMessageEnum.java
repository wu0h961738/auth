package com.g0y.auth.exception.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * error messages
 * */
@Getter
@AllArgsConstructor
public enum ErrorMessageEnum {
    INVALID_AUTH_CODE(HttpStatus.UNAUTHORIZED.value(), "Invalid authorization code"),
    INVALID_IDTOKEN(HttpStatus.NOT_ACCEPTABLE.value(), "Invalid ID token");

    /** status code returned as code included in response status */
    private Integer statusCode;

    /** error message as body of response entity */
    private String errorMessage;
}
