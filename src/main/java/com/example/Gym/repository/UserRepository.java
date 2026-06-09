package com.example.Gym.repository;

import com.example.Gym.model.AppUser;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Set<AppUser> findByIdIn(Collection<Long> ids);

    Optional<AppUser> findByUsername(String username);
}

