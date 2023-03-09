package com.example.tinderappmaven.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileDto {
    String username;
    String nickname;
    String location;
    String gender;
    LocalDate birthDate;
}
