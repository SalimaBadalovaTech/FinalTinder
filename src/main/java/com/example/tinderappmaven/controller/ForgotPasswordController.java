package com.example.tinderappmaven.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForgotPasswordController {

    @GetMapping(value = "/forgot-pass")
    public String goToForgotPassword(){
        return "forgot-password";
    }
}