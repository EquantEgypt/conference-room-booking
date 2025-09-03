package entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
    public class Equipment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;


        @Column(nullable = false, unique = true, length = 100)
        private String type;

//        @ManyToMany(mappedBy = "equipmentList")
//        private List<MeetingRoom> meetingRooms;
//    }

    @ManyToMany(mappedBy = "equipmentList")
    private Set<MeetingRoom> meetingRooms = new HashSet<>();
}