package com.example.tinderappmaven.respository;

import com.example.tinderappmaven.model.Profile;
import com.example.tinderappmaven.model.Reactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionsRepository extends JpaRepository<Reactions, Long> {
    List<Reactions> findByUserId(Long userId);
    List<Reactions> findByUserIdAndIsLiked(Long userId,Boolean isLiked);
    void deleteByUserIdAndProfileId(Long userId,Long profileId);
}
