package com.g0y.auth.exception.model;

/**
 * exception while valid access token in fail
 * */
public class UnAuthorizedException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public UnAuthorizedException(String msg) {
        super(msg);
    }
}
