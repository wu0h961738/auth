package com.g0y.auth.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * enum managing agency inputted from front-end
 * */
@AllArgsConstructor
@Getter
public enum AgencyEnum {
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

    /**
     * get the name of agency providing auth service
     *
     * @param urlPath full context of url
     * */
    public static String getCookieNameByUri(String urlPath){
        return AgencyEnum.getCookieNameByAgency(urlPath.substring(urlPath.lastIndexOf('/')+1));
    }

    /**
     * get agencyName by matching name in Uri
     *
     * @param urlPath full context of url
     * */
    public static String getAgencyNameByUri(String urlPath){
        String agencyInUri = urlPath.substring(urlPath.lastIndexOf('/')+1);
        for(AgencyEnum agency : values()){
            if(agency.getAgencyName().equals(agencyInUri)){
                return agency.getAgencyName();
            }
        }
        return null;
    }

}
