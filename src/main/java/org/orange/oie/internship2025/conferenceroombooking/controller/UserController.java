package org.orange.oie.internship2025.conferenceroombooking.controller;

import jakarta.validation.Valid;
import org.orange.oie.internship2025.conferenceroombooking.dto.LoginRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.UserResponse;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.UserDetailsServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Map;

@RestController
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImplementation userDetailsServiceImplementation;

    public UserController(AuthenticationManager authenticationManager
    , UserDetailsServiceImplementation userDetailsServiceImplementation) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImplementation = userDetailsServiceImplementation;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        authenticationManager.authenticate(authenticationToken);
        String encodedBase64UsernamePassword = Base64.getEncoder().encodeToString(
                ((loginRequest.getUsername()) + ":" + loginRequest.getPassword()).getBytes());
        return ResponseEntity.ok(Map.of("token", encodedBase64UsernamePassword));

    }

    @GetMapping("username")
    public ResponseEntity<UserResponse> getUsername() {
        return ResponseEntity.ok(userDetailsServiceImplementation.getCurrentUsername());
    }
}
