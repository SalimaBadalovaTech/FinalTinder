package com.example.tinderappmaven.controller;


import com.example.tinderappmaven.model.Profile;
import com.example.tinderappmaven.model.Reactions;
import com.example.tinderappmaven.respository.ProfileRepository;
import com.example.tinderappmaven.respository.ReactionsRepository;
import com.example.tinderappmaven.respository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class HomeController {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ReactionsRepository reactionsRepository;

    public HomeController(UserRepository userRepository, ProfileRepository profileRepository, ReactionsRepository reactionsRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.reactionsRepository = reactionsRepository;
    }

    @GetMapping(value = "/main-page")
    public String goToHome(Principal principal, ModelMap modelMap) {
        modelMap.put("name", userRepository.findByEmail(principal.getName()).getUsername());
        List<Profile> allProfiles = findUnusedProfiles(principal);
        modelMap.addAttribute("profile1", allProfiles.get(0));
        if (allProfiles.size() > 1) {
            modelMap.addAttribute("profile2", allProfiles.get(1));
        }
        return "main-page";
    }

    @PostMapping(value = "/like-button")
    public String likeButton(ModelMap modelMap, Principal principal) {
        log.atInfo().log("in like button");
        reactionsRepository.save(new Reactions((long) (reactionsRepository.findAll().size() + 1),
                userRepository.findByEmail(principal.getName()).getId(), findUnusedProfiles(principal).get(0).getId(), true));
        return "redirect:/main-page";
    }

    @PostMapping(value = "/dislike-button")
    public String dislikeButton(ModelMap modelMap, Principal principal) {
        log.atInfo().log("in dislike button");
        reactionsRepository.save(new Reactions((long) (reactionsRepository.findAll().size() + 1),
                userRepository.findByEmail(principal.getName()).getId(), findUnusedProfiles(principal).get(0).getId(), false));
        return "redirect:/main-page";
    }

    public List<Profile> findUnusedProfiles(Principal principal) {
        List<Profile> allProfiles = profileRepository.findAll();
        List<Reactions> reactedProfiles = reactionsRepository.findByUserId(userRepository.findByEmail(principal.getName()).getId());
        if (reactedProfiles.size() > 0) {
            log.atInfo().log(allProfiles.size() + " logging");
            allProfiles = allProfiles.stream()
                    .filter(profile -> reactedProfiles.stream()
                            .noneMatch(reactions -> Objects.equals(profile.getId(), reactions.getProfileId())))
                    .collect(Collectors.toList());
        }
        return allProfiles;
    }

    @GetMapping(value = "/user-profile")
    public String goToUserProfile(Principal principal, ModelMap modelMap) {
        modelMap.put("name", userRepository.findByEmail(principal.getName()).getUsername());
        List<Profile> allProfiles = findUnusedProfiles(principal);
        modelMap.addAttribute("profile1", allProfiles.get(0));
        return "user-profile";
    }
}
