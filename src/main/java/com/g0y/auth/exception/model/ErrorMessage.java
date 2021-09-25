package com.g0y.auth.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ErrorMessage {

    /** http status code */
    private int statusCode;

    /** time when the error occurs */
    private Date timestamp;

    /** title of error msg */
    private String message;

    /** detail of error */
    private String description;

}
