package org.orange.oie.internship2025.conferenceroombooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptId;

    @NotBlank(message = "Department name is required")
    @Column(nullable = false, unique = true)
    private String deptName;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "user_id")
    private User manager;

    @OneToMany(mappedBy = "department")
    private List<User> employees = new ArrayList<>();
}
