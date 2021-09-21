package com.g0y.auth.oauth.model;

import com.g0y.auth.controller.model.AuthPageRq;

import java.util.Map;

/** adapter to AutPageRq*/
public interface AuthPageRequestAdapter {

    AuthPageRq getRequest(Map<String, String> requestParams);
}
