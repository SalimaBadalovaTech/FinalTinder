package com.example.tinderappmaven.controller;

import com.example.tinderappmaven.dto.RegistrationDto;
import com.example.tinderappmaven.model.UserEntity;
import com.example.tinderappmaven.respository.UserRepository;
import com.example.tinderappmaven.services.CustomUserService;
import com.example.tinderappmaven.services.ValidationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@Slf4j
public class AuthController {
    private final CustomUserService userService;
    private final UserRepository userRepository;
    private final ValidationService validationService;

    public AuthController(CustomUserService userService, UserRepository userRepository, ValidationService validationService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    @PostMapping(value = "/login")
    public String userLogin(@RequestParam("email") String email,
                            @RequestParam("password") String password) {
        log.atInfo().log("Entered into UserLogin");
        if (authenticate(email, password)) {
            return "main-page";
        } else {
            return "index";
        }

    }

    public boolean authenticate(String email, String password) {

        UserEntity user = userRepository.findByEmailAndPassword(email, password);
        return user != null;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "index";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        RegistrationDto user = new RegistrationDto();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegistrationDto user,
                           BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "registration";
        }

        if (!Objects.equals(user.getPassword(), user.getConfirmPassword())) {
            log.atInfo().log("Entered into log info");
            ObjectError error = new ObjectError("globalError", "Password did not match");
            result.addError(error);
            return "redirect:/registration";
        }

        userService.saveUser(user);
        return "redirect:/main-page";
    }
}
