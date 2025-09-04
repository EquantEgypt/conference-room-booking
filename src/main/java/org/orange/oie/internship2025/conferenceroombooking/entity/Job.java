package org.orange.oie.internship2025.conferenceroombooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    private Long job_id;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    @OneToMany(mappedBy = "job")
    private List<User> users = new ArrayList<>();
}


