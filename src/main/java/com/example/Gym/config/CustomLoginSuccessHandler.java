package com.example.Gym.config;

import com.example.Gym.model.AppUser;
import com.example.Gym.model.Role;
import com.example.Gym.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        AppUser user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.USER) {
            response.sendRedirect("/choose-role");
        } else if (user.getRole() == Role.MEMBER) {
            response.sendRedirect("/member/dashboard");
        } else if (user.getRole() == Role.TRAINER) {
            response.sendRedirect("/trainer/dashboard");
        } else if (user.getRole() == Role.ADMIN) {
            response.sendRedirect("/admin/dashboard");
        } else {
            response.sendRedirect("/login?error");
        }
    }
}
