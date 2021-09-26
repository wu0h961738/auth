package com.g0y.auth.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SetSessionWithTokenRs {

    /** serialized session object */
    private String sessionKey;

    /** token containing user payload */
    private String idToken;
}
