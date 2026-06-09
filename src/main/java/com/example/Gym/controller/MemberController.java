package com.example.Gym.controller;

import com.example.Gym.model.AppUser;
import com.example.Gym.model.Member;
import com.example.Gym.repository.MemberRepository;
import com.example.Gym.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/members")
public class MemberController {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    public MemberController(MemberRepository memberRepository, UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("memberForm", new Member());
        return "member";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("members", memberRepository.findAll());
        model.addAttribute("memberForm", memberRepository.findById(id).orElse(new Member()));
        return "member";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("memberForm") Member member) {
        memberRepository.save(member);
        return "redirect:/admin/members";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("memberForm") Member member) {
        member.setId(id);
        memberRepository.save(member);
        return "redirect:/admin/members";
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String delete(@PathVariable Long id) {
        memberRepository.findById(id).ifPresent(member -> {

            // so no FK violation on member_liked_trainers
            member.getLikedTrainers().clear();
            memberRepository.save(member);

            AppUser appUser = member.getAppUser();
            if (appUser != null) {
                appUser.setMember(null);
                userRepository.save(appUser);
            }

            memberRepository.delete(member);
        });
        return "redirect:/admin/members";
    }
}
