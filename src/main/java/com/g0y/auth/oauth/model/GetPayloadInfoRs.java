package com.g0y.auth.oauth.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * payload information extracted from id token
 * */
@Data
@NoArgsConstructor
public class GetPayloadInfoRs {

    /**
     * name of user
     * */
    private String name;

    /**
     * picture of user
     * */
    private String picture;
}
