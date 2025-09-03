package entity;
import enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Role is required")
    @Column(nullable = false)
    private UserRole role;


    @NotBlank(message = "First name is required")
    @Column( nullable = false)
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
    private Department department;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;




}



