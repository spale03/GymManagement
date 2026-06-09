package com.example.Gym.repository;

import com.example.Gym.model.Feedback;
import com.example.Gym.model.Member;
import com.example.Gym.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByMember(Member member);
    List<Feedback> findByTrainer(Trainer trainer);

    // Used when nulling out trainer FK before trainer delete (if needed from outside cascade)
    @Modifying
    @Query("UPDATE Feedback f SET f.trainer = null WHERE f.trainer = :trainer")
    void detachTrainer(Trainer trainer);
}
