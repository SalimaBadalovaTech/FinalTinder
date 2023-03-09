package com.example.tinderappmaven.services;


import com.example.tinderappmaven.dto.RegistrationDto;

public interface ValidationService {
    public String validateUser(RegistrationDto user);
}
