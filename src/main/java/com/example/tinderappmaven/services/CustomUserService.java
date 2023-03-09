package com.example.tinderappmaven.services;


import com.example.tinderappmaven.dto.ProfileDto;
import com.example.tinderappmaven.dto.RegistrationDto;
import com.example.tinderappmaven.model.UserEntity;

public interface CustomUserService {

    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    void updateUser(UserEntity userEntity, ProfileDto profileDto);
}
