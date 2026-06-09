package com.example.Gym.controller;

import com.example.Gym.model.AppUser;
import com.example.Gym.model.Member;
import com.example.Gym.model.Role;
import com.example.Gym.model.Trainer;
import com.example.Gym.repository.MemberRepository;
import com.example.Gym.repository.TrainerRepository;
import com.example.Gym.repository.UserRepository;
import com.example.Gym.services.UserRoleRefresherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class RoleSelectionController {

    @Autowired private UserRepository userRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private TrainerRepository trainerRepository;
    @Autowired private UserRoleRefresherService roleRefresherService;

    @GetMapping("/choose-role")
    public String showRolePage(Principal principal) {
        AppUser user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Redirect away if already has a non-USER role
        if (user.getRole() == Role.MEMBER) return "redirect:/member/dashboard";
        if (user.getRole() == Role.TRAINER) return "redirect:/trainer/dashboard";
        if (user.getRole() == Role.ADMIN) return "redirect:/admin/dashboard";

        return "choose-role";
    }

    @PostMapping("/choose-role")
    public String setUserRole(@RequestParam("role") String role, Principal principal) {
        AppUser user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (role.equals("MEMBER")) {
            user.setRole(Role.MEMBER);
            userRepository.save(user);
            roleRefresherService.updateUserRoleInSession(user);

            if (memberRepository.findByAppUser(user).isEmpty()) {
                Member member = new Member();
                member.setName(user.getUsername());
                member.setEmail(user.getEmail());
                member.setAppUser(user);
                memberRepository.save(member);
                user.setMember(member);
                userRepository.save(user);
            }
            return "redirect:/member/dashboard";

        } else if (role.equals("TRAINER")) {
            user.setRole(Role.TRAINER);
            userRepository.save(user);
            roleRefresherService.updateUserRoleInSession(user);

            if (trainerRepository.findByAppUser(user).isEmpty()) {
                Trainer trainer = new Trainer();
                trainer.setName(user.getUsername());
                trainer.setAppUser(user);
                trainerRepository.save(trainer);
                user.setTrainer(trainer);
                userRepository.save(user);
            }
            return "redirect:/trainer/dashboard";
        }

        // Unknown role — send back to choose
        return "redirect:/choose-role";
    }
}
