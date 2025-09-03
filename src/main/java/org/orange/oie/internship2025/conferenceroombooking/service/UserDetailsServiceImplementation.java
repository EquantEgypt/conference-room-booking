package org.orange.oie.internship2025.conferenceroombooking.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //integrate the repository of the database
        return org.springframework.security.core.userdetails.User
                .withUsername("seif")
                .password("$2a$12$FvdWPqAtHEVWoJlEHPp1b.Zyyw24bhzyUfUTYamMBUioixLVYkyIK")
                .roles("USER").build();
    }
}
