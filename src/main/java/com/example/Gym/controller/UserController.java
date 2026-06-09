package com.example.Gym.controller;

import com.example.Gym.model.AppUser;
import com.example.Gym.model.Member;
import com.example.Gym.model.Trainer;
import com.example.Gym.repository.MemberRepository;
import com.example.Gym.repository.TrainerRepository;
import com.example.Gym.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          MemberRepository memberRepository,
                          TrainerRepository trainerRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.trainerRepository = trainerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("userForm", new AppUser());
        return "user";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("userForm", userRepository.findById(id).orElse(new AppUser()));
        return "user";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("userForm") AppUser user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("userForm") AppUser user) {
        user.setId(id);
        // Only re-hash if password field was actually filled in
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // Keep existing password
            userRepository.findById(id).ifPresent(existing -> user.setPassword(existing.getPassword()));
        }
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String delete(@PathVariable Long id) {
        userRepository.findById(id).ifPresent(user -> {

            Trainer trainer = user.getTrainer();
            if (trainer != null) {
                user.setTrainer(null);
                userRepository.save(user);
                trainer.setAppUser(null);
                trainerRepository.save(trainer);
            }

            Member member = user.getMember();
            if (member != null) {
                user.setMember(null);
                userRepository.save(user);
                member.setAppUser(null);
                memberRepository.save(member);
            }

            userRepository.delete(user);
        });
        return "redirect:/admin/users";
    }
}
