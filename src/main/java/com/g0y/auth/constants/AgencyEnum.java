package com.g0y.auth.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * enum managing agency inputted from front-end
 * */
@AllArgsConstructor
@Getter
public enum AgencyEnum {
    LINE("line"),
    GOOGLE("google"),
    FB("facebook");

    private String agencyName;
}
