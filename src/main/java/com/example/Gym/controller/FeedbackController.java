package com.example.Gym.controller;

import com.example.Gym.model.Feedback;
import com.example.Gym.repository.FeedbackRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/feedback")
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;

    public FeedbackController(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("feedbacks", feedbackRepository.findAll());
        model.addAttribute("feedbackForm", new Feedback());
        return "feedback";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("feedbacks", feedbackRepository.findAll());
        model.addAttribute("feedbackForm", feedbackRepository.findById(id).orElse(new Feedback()));
        return "feedback";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("feedbackForm") Feedback feedback) {
        feedbackRepository.save(feedback);
        return "redirect:/admin/feedback";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("feedbackForm") Feedback feedback) {
        feedback.setId(id);
        feedbackRepository.save(feedback);
        return "redirect:/admin/feedback";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        feedbackRepository.deleteById(id);
        return "redirect:/admin/feedback";
    }
}
