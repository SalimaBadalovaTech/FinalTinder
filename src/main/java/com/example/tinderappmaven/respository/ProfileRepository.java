package com.example.tinderappmaven.respository;

import com.example.tinderappmaven.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUsername(String email);
}
