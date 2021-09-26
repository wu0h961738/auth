package com.g0y.auth.exception.model;

/**
 * handle when the invalid agency name was provided
 * */
public class InvalidAgencyException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public InvalidAgencyException(String msg) {
        super(msg);
    }
}
