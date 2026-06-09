package com.example.Gym.controller;

import com.example.Gym.model.*;
import com.example.Gym.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    @Autowired private UserRepository appUserRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private TrainerRepository trainerRepository;
    @Autowired private TrainingSessionRepository trainingSessionRepository;
    @Autowired private FeedbackRepository feedbackRepository;

    // which allowed any string as role path variable

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Principal principal, Model model) {
        model.addAttribute("role", "ADMIN");
        model.addAttribute("username", principal.getName());
        model.addAttribute("users", appUserRepository.findAll());
        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("trainers", trainerRepository.findAll());
        model.addAttribute("sessions", trainingSessionRepository.findAll());
        model.addAttribute("feedbacks", feedbackRepository.findAll());
        model.addAttribute("userForm", new AppUser());
        model.addAttribute("memberForm", new Member());
        model.addAttribute("trainerForm", new Trainer());
        model.addAttribute("sessionsForm", new TrainingSession());
        model.addAttribute("feedbackForm", new Feedback());
        return "shared-dashboard";
    }

    @GetMapping("/member/dashboard")
    @PreAuthorize("hasRole('MEMBER')")
    public String memberDashboard(Principal principal, Model model) {
        model.addAttribute("role", "MEMBER");
        model.addAttribute("username", principal.getName());

        AppUser user = appUserRepository.findByUsername(principal.getName()).orElse(null);
        if (user != null && user.getMember() != null) {
            Member member = user.getMember();
            model.addAttribute("likedTrainers", member.getLikedTrainers());
            model.addAttribute("mySessions", trainingSessionRepository.findByMember(member));
        }
        model.addAttribute("trainers", trainerRepository.findAll());
        return "shared-dashboard";
    }

    @GetMapping("/trainer/dashboard")
    @PreAuthorize("hasRole('TRAINER')")
    public String trainerDashboard(Principal principal, Model model) {
        model.addAttribute("role", "TRAINER");
        model.addAttribute("username", principal.getName());

        AppUser user = appUserRepository.findByUsername(principal.getName()).orElse(null);
        if (user != null && user.getTrainer() != null) {
            model.addAttribute("mySessions", trainingSessionRepository.findByTrainer(user.getTrainer()));
            model.addAttribute("myFeedbacks", feedbackRepository.findByTrainer(user.getTrainer()));
        }
        model.addAttribute("sessions", trainingSessionRepository.findAll());
        return "shared-dashboard";
    }

    @GetMapping("/user/dashboard")
    @PreAuthorize("hasRole('USER')")
    public String userDashboard(Principal principal, Model model) {
        model.addAttribute("role", "USER");
        model.addAttribute("username", principal.getName());
        return "shared-dashboard";
    }
}
