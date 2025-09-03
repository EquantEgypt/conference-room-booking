package entity;
import jakarta.persistence.*;
import lombok.*;

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
    private Long jobId;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    @OneToMany(mappedBy = "job")
    private List<User> users = new ArrayList<>();
}





