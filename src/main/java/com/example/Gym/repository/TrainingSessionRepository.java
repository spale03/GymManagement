package com.example.Gym.repository;

import com.example.Gym.model.Member;
import com.example.Gym.model.Trainer;
import com.example.Gym.model.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    List<TrainingSession> findByMember(Member member);
    List<TrainingSession> findByTrainer(Trainer trainer);

    @Modifying
    @Query("UPDATE TrainingSession t SET t.trainer = null WHERE t.trainer = :trainer")
    void detachTrainer(Trainer trainer);

    @Modifying
    @Query("UPDATE TrainingSession t SET t.member = null WHERE t.member = :member")
    void detachMember(Member member);
}
