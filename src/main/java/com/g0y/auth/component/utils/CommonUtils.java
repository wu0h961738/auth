/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.g0y.auth.component.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g0y.auth.session.model.SessionObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * <p>Common utilities</p>
 */
@Component
@Slf4j
public final class CommonUtils {

    private static final SecureRandom RANDOM = new SecureRandom();

    /** serializer TODO customize configuration of mapper */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * <p>Generate Token</p>
     */
    public String randomAndEncodeWithBase64(int length) {
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return token;
    }

    /**
     * serialize to the serialized string for storing in cookie
     *
     * @param sessionObj obj to be serialized
     * @return serialized string
     * */
    public String serializeSessionObj(SessionObject sessionObj) {
        String serializedString = null;
        try{
            serializedString = objectMapper.writeValueAsString(sessionObj);
        } catch (JsonProcessingException jpe){
            // TODO error handling?
            log.error("serialize session object to json string failed.");
        }
        return serializedString;
    }

    /**
     * deserialize to Session object
     *
     * @param json json String
     * */
    public SessionObject deserializeSessionObjString(String json){
        SessionObject sessionObject = null;
        try{
            sessionObject = objectMapper.readValue(json, SessionObject.class);
        } catch (JsonMappingException e) {
            // TODO error handling?
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO error handling?
            e.printStackTrace();
        }
        return sessionObject;
    }

    /**
     * generate random value for cookie value
     * TODO implement a better way for giving redis key name
     *
     * @param agencyName name of auth provider
     * */
    public String generateTokenKey(String agencyName){
        return agencyName + CommonUtils.randomAndEncodeWithBase64(6);
    }
}
