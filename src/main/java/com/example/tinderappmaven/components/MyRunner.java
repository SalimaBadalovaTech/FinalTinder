package com.example.tinderappmaven.components;


import com.example.tinderappmaven.respository.ProfileRepository;
import com.example.tinderappmaven.respository.RoleRepository;
import com.example.tinderappmaven.respository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyRunner implements CommandLineRunner {


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public MyRunner(RoleRepository repository, UserRepository userRepository, ProfileRepository profileRepository) {
        this.roleRepository = repository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
