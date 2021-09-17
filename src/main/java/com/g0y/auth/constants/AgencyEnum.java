package com.g0y.auth.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * enum managing agency inputted from front-end
 * */
@AllArgsConstructor
@Getter
public enum AgencyEnum {
    // TODO change prefix of cookieName
    LINE("line", "ggline"),
    GOOGLE("google", "gggoogle"),
    FB("facebook", "ggfacebook");

    /** name of auth provider */
    private String agencyName;

    /** name of cookie presenting key of access token stored in redis*/
    private String cookieName;

    /**
     * getCookieName by passing agency name
     * */
    public static String getCookieNameByAgency(String agencyName){
        for(AgencyEnum agency : values()){
            if(agency.getAgencyName().equals(agencyName)){
                return agency.getCookieName();
            }
        }
        return null;
    }
}
