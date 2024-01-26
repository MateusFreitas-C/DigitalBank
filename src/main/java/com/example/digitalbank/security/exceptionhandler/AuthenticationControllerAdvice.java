package com.example.digitalbank.security.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AuthenticationControllerAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody @ResponseStatus(HttpStatus.FORBIDDEN)
    public String illegalArgumentExceptionHandler(IllegalArgumentException e){
        return e.getMessage();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseBody @ResponseStatus(HttpStatus.NOT_FOUND)
    public String usernameNotFoundExceptionHandler(UsernameNotFoundException e){
        return e.getMessage();
    }
}
