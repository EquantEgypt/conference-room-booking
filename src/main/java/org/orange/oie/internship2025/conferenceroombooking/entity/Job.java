package org.orange.oie.internship2025.conferenceroombooking.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "job-movement")
    private List<User> users;
}
