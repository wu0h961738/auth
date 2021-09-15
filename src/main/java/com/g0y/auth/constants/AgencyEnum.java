package com.g0y.auth.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * enum managing agency inputted from front-end
 * */
@AllArgsConstructor
@Getter
public enum AgencyEnum {
    LINE("line", ""),
    GOOGLE("google", ""),
    FB("facebook", "");

    /** name of auth provider */
    private String agencyName;

    /** name of cookie presenting key of access token stored in redis*/
    private String cookieName;

    // TODO new function getCookieNameByAgency
}
