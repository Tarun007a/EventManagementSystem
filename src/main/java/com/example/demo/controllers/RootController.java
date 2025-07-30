package com.example.demo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class RootController {
    @GetMapping("/home")
    public String homePage(){
        return "Home controller view";
    }

    @GetMapping("/")
    public void home(HttpServletResponse response) throws IOException {
        response.sendRedirect("/home");
    }
}
