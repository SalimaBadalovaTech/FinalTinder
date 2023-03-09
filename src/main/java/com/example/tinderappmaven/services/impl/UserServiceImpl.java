package com.example.tinderappmaven.services.impl;

import com.example.tinderappmaven.dto.ProfileDto;
import com.example.tinderappmaven.dto.RegistrationDto;
import com.example.tinderappmaven.model.Role;
import com.example.tinderappmaven.model.UserEntity;
import com.example.tinderappmaven.respository.RoleRepository;
import com.example.tinderappmaven.respository.UserRepository;
import com.example.tinderappmaven.services.CustomUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class UserServiceImpl implements CustomUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        log.atInfo().log("saveUser called");
        UserEntity user = new UserEntity();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_ADMIN");
        log.atInfo().log("Role is "+ role);
        user.setRoles(Arrays.asList(role));
        user.setBirthDate(null);
        user.setNickname(null);
        user.setLocation(null);
        user.setGender(null);
        userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void updateUser(UserEntity user, ProfileDto profileDto) {
        UserEntity userUpdate = userRepository.findByEmail(user.getEmail());
        userUpdate.setUsername(profileDto.getUsername());
        userUpdate.setLocation(profileDto.getLocation());
        userUpdate.setGender(profileDto.getGender());
        userUpdate.setNickname(profileDto.getNickname());
        userUpdate.setBirthDate(profileDto.getBirthDate());
        userRepository.save(userUpdate);
    }
}