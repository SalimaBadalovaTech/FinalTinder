package com.example.tinderappmaven.controller;

import com.example.tinderappmaven.dto.FavoriteDto;
import com.example.tinderappmaven.dto.ProfileDto;
import com.example.tinderappmaven.dto.RegistrationDto;
import com.example.tinderappmaven.model.Profile;
import com.example.tinderappmaven.model.Reactions;
import com.example.tinderappmaven.model.UserEntity;
import com.example.tinderappmaven.respository.ProfileRepository;
import com.example.tinderappmaven.respository.ReactionsRepository;
import com.example.tinderappmaven.respository.UserRepository;
import com.example.tinderappmaven.services.CustomUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class AccountController {

    private final CustomUserService userService;
    private final UserRepository userRepository;
    private final ReactionsRepository reactionsRepository;
    private final ProfileRepository profileRepository;

    public AccountController(CustomUserService userService, UserRepository userRepository, ReactionsRepository reactionsRepository, ProfileRepository profileRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.reactionsRepository = reactionsRepository;
        this.profileRepository = profileRepository;
    }

    @GetMapping(value = "/my-profile")
    public String getProfilePage(Principal principal, ModelMap modelMap) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName());
        ProfileDto updateUser = new ProfileDto();
        updateUser.setUsername(currentUser.getUsername());
        updateUser.setNickname(currentUser.getNickname());
        updateUser.setLocation(currentUser.getLocation());
        updateUser.setGender(currentUser.getGender());
        updateUser.setBirthDate(currentUser.getBirthDate());
        modelMap.addAttribute("updateUser", updateUser);
        modelMap.put("profile", userRepository.findByEmail(principal.getName()));
        modelMap.put("name",userRepository.findByEmail(principal.getName()).getUsername());
        return "my-profile";
    }

    @GetMapping(value = "/favourites")
    public String goToFavorites(Principal principal, ModelMap modelMap) {
        FavoriteDto user = new FavoriteDto();
        modelMap.addAttribute("user", user);
        List<Profile>  profiles = findFavouriteProfiles(principal);
        log.atInfo().log(profiles.get(0).getUsername());
        modelMap.addAttribute("liked",profiles);
        modelMap.put("name",principal.getName());
        return "favourites";
    }

    @PostMapping (value = "/remove-favorite")
    public String removeFavorite(@Valid @ModelAttribute("user") FavoriteDto user, Principal principal, ModelMap modelMap) {

        log.atInfo().log( user.getUsername()+" get user");
        reactionsRepository.deleteByUserIdAndProfileId(userRepository.findByEmail(principal.getName()).getId(),
                profileRepository.findByUsername(user.getUsername()).getId());
        return "favourites";
    }

    @PostMapping("/update-user")
    public String register(@Valid @ModelAttribute("user") ProfileDto user,
                           BindingResult result, Model model,Principal principal) {
        log.atInfo().log("called update user "+ user.getBirthDate());
        userService.updateUser(userRepository.findByEmail(principal.getName()),user);
        return "redirect:/main-page";
    }

    public List<Profile> findFavouriteProfiles(Principal principal) {
        List<Profile> allProfiles = profileRepository.findAll();
        List<Reactions> reactedProfiles = reactionsRepository.findByUserIdAndIsLiked(userRepository.findByEmail(principal.getName()).getId(),true);
        if (reactedProfiles.size() > 0) {
            log.atInfo().log(allProfiles.size() + " logging");
            allProfiles = allProfiles.stream()
                    .filter(profile -> reactedProfiles.stream()
                            .anyMatch(reactions -> Objects.equals(profile.getId(), reactions.getProfileId())))
                    .collect(Collectors.toList());
        }
        return allProfiles;
    }
}
