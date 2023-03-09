package com.example.tinderappmaven.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String profilePicture;
    private String about;
    private String location;
    private String sex;
    private Long age;
    private String nickname;
    @ManyToMany(mappedBy = "likedProfiles")
    private List<UserEntity> users = new ArrayList<>();
}
