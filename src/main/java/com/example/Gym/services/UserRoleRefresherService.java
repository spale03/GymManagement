package com.example.Gym.services;

import com.example.Gym.model.AppUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleRefresherService {

    public void updateUserRoleInSession(AppUser user) {
        List<SimpleGrantedAuthority> updatedAuthorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword(),
                updatedAuthorities
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
