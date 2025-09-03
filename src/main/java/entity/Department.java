package entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

    @Entity
    @Table(name = "departments")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Department {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long deptId;

        @NotBlank(message = "Department name is required")
        @Column(nullable = false, unique = true)
        private String deptName;

        @OneToOne
        @JoinColumn(name = "manager_id", referencedColumnName = "id")
        private User manager;

        @OneToMany(mappedBy = "department")
        private List<User> employees = new ArrayList<>();
    }

