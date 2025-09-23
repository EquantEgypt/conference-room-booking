package org.orange.oie.internship2025.conferenceroombooking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.orange.oie.internship2025.conferenceroombooking.enums.UserRole;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is required")
    @Column(nullable = false)
    private UserRole role;


    @NotBlank(message = "First name is required")
    @Column(nullable = false)
    private String firstName;


    @NotBlank(message = "Last name is required")
    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String phone;

    @NotBlank(message = "Email is required")
    @Column(name = "business_email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonBackReference(value = "employee-movement")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "job_id")
    @JsonBackReference(value = "job-movement")
    private Job job;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value = "user-movement")
    private List<Reservation> reservations;
}
