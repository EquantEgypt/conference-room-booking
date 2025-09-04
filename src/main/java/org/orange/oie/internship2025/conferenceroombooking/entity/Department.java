package org.orange.oie.internship2025.conferenceroombooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long dept_id;

    @NotBlank(message = "Department name is required")
    @Column(name = "name", nullable = false, unique = true)
    private String deptName;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "user_id")
    private User manager;

    @OneToMany(mappedBy = "department")
    private List<User> employees = new ArrayList<>();
}
