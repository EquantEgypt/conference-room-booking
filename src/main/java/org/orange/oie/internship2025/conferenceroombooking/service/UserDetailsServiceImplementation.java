package org.orange.oie.internship2025.conferenceroombooking.service;

import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUsersByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found by email: " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER").build();
    }
}
