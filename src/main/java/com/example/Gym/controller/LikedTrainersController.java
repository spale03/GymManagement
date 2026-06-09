package com.example.Gym.controller;

import com.example.Gym.model.AppUser;
import com.example.Gym.model.Member;
import com.example.Gym.model.Trainer;
import com.example.Gym.repository.UserRepository;
import com.example.Gym.repository.MemberRepository;
import com.example.Gym.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class LikedTrainersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @PostMapping("/member/like/{trainerId}")
    public String likeTrainer(@PathVariable Long trainerId, Principal principal) {
        AppUser user = userRepository.findByUsername(principal.getName()).orElse(null);
        Member member = memberRepository.findByAppUser(user).orElse(null);
        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);

        if (member != null && trainer != null && !member.getLikedTrainers().contains(trainer)) {
            member.getLikedTrainers().add(trainer);
            memberRepository.save(member);
        }

        return "redirect:/member/dashboard";
    }

    @PostMapping("/member/unlike/{trainerId}")
    public String unlikeTrainer(@PathVariable Long trainerId, Principal principal) {
        AppUser user = userRepository.findByUsername(principal.getName()).orElse(null);
        Member member = memberRepository.findByAppUser(user).orElse(null);
        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);

        if (member != null && trainer != null && member.getLikedTrainers().contains(trainer)) {
            member.getLikedTrainers().remove(trainer);
            memberRepository.save(member);
        }

        return "redirect:/member/dashboard";
    }
}
