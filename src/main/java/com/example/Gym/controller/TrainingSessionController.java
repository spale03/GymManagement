package com.example.Gym.controller;

import com.example.Gym.model.TrainingSession;
import com.example.Gym.repository.TrainingSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/sessions")
public class TrainingSessionController {

    private final TrainingSessionRepository trainingSessionRepository;

    public TrainingSessionController(TrainingSessionRepository trainingSessionRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("sessions", trainingSessionRepository.findAll());
        model.addAttribute("sessionsForm", new TrainingSession());
        return "sessions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("sessions", trainingSessionRepository.findAll());
        model.addAttribute("sessionsForm", trainingSessionRepository.findById(id).orElse(new TrainingSession()));
        return "sessions";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("sessionsForm") TrainingSession sessions) {
        trainingSessionRepository.save(sessions);
        return "redirect:/admin/sessions";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("sessionsForm") TrainingSession sessions) {
        sessions.setId(id);
        trainingSessionRepository.save(sessions);
        return "redirect:/admin/sessions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        trainingSessionRepository.deleteById(id);
        return "redirect:/admin/sessions";
    }
}
