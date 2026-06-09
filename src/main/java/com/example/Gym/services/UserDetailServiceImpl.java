package com.example.Gym.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Gym.model.AppUser;
import com.example.Gym.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    
    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;    
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> personOptional = userRepository.findByUsername(username);
        if (personOptional.isEmpty()) {
            throw new UsernameNotFoundException("Username %s does not exist".formatted(username));
        }
        AppUser person = personOptional.get();

        return new User(person.getUsername(), person.getPassword(), getAuthorities(person));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(AppUser person) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + person.getRole().name()));
    }
}