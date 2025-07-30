package com.codegym.mobilestore.controller;

import com.codegym.mobilestore.annotation.UniqueValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class GlobalHandlerController
{
    @Autowired
    private UniqueValidator validator;

    @InitBinder
    public void initBinder(org.springframework.web.bind.WebDataBinder binder) {
        binder.addValidators(validator);
    }


}
