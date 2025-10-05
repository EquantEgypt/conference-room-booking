package org.orange.oie.internship2025.conferenceroombooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.orange.oie.internship2025.conferenceroombooking.enums.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String username;
    private UserRole role;
}
