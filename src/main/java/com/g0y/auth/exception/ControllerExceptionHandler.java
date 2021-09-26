package com.g0y.auth.exception;

import com.g0y.auth.exception.model.BaseErrorMessage;
import com.g0y.auth.exception.model.InvalidAgencyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * exception response aop
 * */
@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    /**
     * exception for invalid agency
     * */
    @ExceptionHandler(InvalidAgencyException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseErrorMessage handleInvalidAgency(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse,
                                                InvalidAgencyException iae){
        BaseErrorMessage baseErrorMessage = new BaseErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                "invalid agency",
                iae.getMessage()
        );

        return baseErrorMessage;
    }

}
