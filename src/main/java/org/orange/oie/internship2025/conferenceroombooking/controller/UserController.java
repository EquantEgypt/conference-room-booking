package org.orange.oie.internship2025.conferenceroombooking.controller;

import org.orange.oie.internship2025.conferenceroombooking.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Map;

@RestController
public class UserController {
    private final AuthenticationManager authenticationManager;

    public UserController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            authenticationManager.authenticate(authenticationToken);
            String encodedBase64UsernamePassword = Base64.getEncoder().encodeToString(
                    ((loginRequest.getUsername()) + ":" + loginRequest.getPassword()).getBytes());
            return ResponseEntity.ok(Map.of("token", encodedBase64UsernamePassword));
        } catch (AuthenticationException authenticationException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("error", "invalid username or password")
            );
        }

    }
}
