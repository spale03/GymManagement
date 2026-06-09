package com.example.Gym.repository;
import com.example.Gym.model.AppUser;
import com.example.Gym.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAppUser(AppUser appUser);
}

