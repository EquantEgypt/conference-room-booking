package org.orange.oie.internship2025.conferenceroombooking.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
