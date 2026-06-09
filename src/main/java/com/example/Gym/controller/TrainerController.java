package com.example.Gym.controller;

import com.example.Gym.model.AppUser;
import com.example.Gym.model.Trainer;
import com.example.Gym.repository.MemberRepository;
import com.example.Gym.repository.TrainerRepository;
import com.example.Gym.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/trainers")
public class TrainerController {

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    public TrainerController(TrainerRepository trainerRepository,
                             UserRepository userRepository,
                             MemberRepository memberRepository) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("trainers", trainerRepository.findAll());
        model.addAttribute("trainerForm", new Trainer());
        return "trainer";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("trainers", trainerRepository.findAll());
        model.addAttribute("trainerForm", trainerRepository.findById(id).orElse(new Trainer()));
        return "trainer";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String add(@ModelAttribute("trainerForm") Trainer trainer) {
        trainerRepository.save(trainer);
        return "redirect:/admin/trainers";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id, @ModelAttribute("trainerForm") Trainer trainer) {
        trainer.setId(id);
        trainerRepository.save(trainer);
        return "redirect:/admin/trainers";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public String delete(@PathVariable Long id) {
        trainerRepository.findById(id).ifPresent(trainer -> {

            // to clear member_liked_trainers join table entries
            memberRepository.findAll().forEach(member -> {
                if (member.getLikedTrainers().remove(trainer)) {
                    memberRepository.save(member);
                }
            });

            AppUser appUser = trainer.getAppUser();
            if (appUser != null) {
                appUser.setTrainer(null);
                userRepository.save(appUser);
            }

            // Sessions and feedbacks are deleted via CascadeType.ALL on Trainer
            trainerRepository.delete(trainer);
        });
        return "redirect:/admin/trainers";
    }
}
