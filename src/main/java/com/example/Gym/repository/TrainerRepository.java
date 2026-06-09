package com.example.Gym.repository;

import com.example.Gym.model.AppUser;
import com.example.Gym.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByAppUser(AppUser appUser);
}
