package com.g0y.auth.exception;

import com.g0y.auth.exception.model.InvalidAgencyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    @ExceptionHandler(InvalidAgencyException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public void handleInvalidAgency(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, InvalidAgencyException iae){

    }

}
