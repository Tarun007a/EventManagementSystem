package com.example.demo.services.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    public String getEmailOfLoggedUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
