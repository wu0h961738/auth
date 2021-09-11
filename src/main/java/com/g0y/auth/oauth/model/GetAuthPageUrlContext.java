package com.g0y.auth.oauth.model;

import lombok.Data;

/**
 * context for getting OAuth page url of agency
 * */
@Data
public class GetAuthPageUrlContext {

    /** OAuth service provider*/
    String agency;

    /** secure param1*/
    String state;

    /** secure param2*/
    String nonce;

}
