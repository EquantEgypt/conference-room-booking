package org.orange.oie.internship2025.conferenceroombooking.controller;

import org.orange.oie.internship2025.conferenceroombooking.dto.LoginRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.UserResponse;
import org.orange.oie.internship2025.conferenceroombooking.jwt.util.JwtProvider;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.UserDetailsServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImplementation userDetailsServiceImplementation;
    private final JwtProvider jwtProvider;

    public UserController(AuthenticationManager authenticationManager
            , UserDetailsServiceImplementation userDetailsServiceImplementation, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImplementation = userDetailsServiceImplementation;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getUsername().isEmpty() || loginRequest.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errorMessage", "Username and password must be provided"));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            final UserDetails userDetails = userDetailsServiceImplementation.loadUserByUsername(loginRequest.getUsername());
            final String jwt = jwtProvider.generateToken(userDetails);

            return ResponseEntity.ok(Map.of("token", jwt));
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @GetMapping("username")
    public ResponseEntity<UserResponse> getUsername() {
        return ResponseEntity.ok(userDetailsServiceImplementation.getCurrentUsername());
    }
}
