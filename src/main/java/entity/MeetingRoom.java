package entity;
import enums.MeetingRoomStatus;
import enums.RoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meeting_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @NotBlank(message = "Room name is required")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "GeoLocation")
    private String geoLocation;

    @Column(nullable = false, length = 100)
    private String building;

    @Column(nullable = false)
    private Integer floor;

    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "operating_hours_start", nullable = false)
    private LocalTime operatingHoursStart;

    @Column(name = "operating_hours_end", nullable = false)
    private LocalTime operatingHoursEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false, length = 20)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MeetingRoomStatus status = MeetingRoomStatus.AVAILABLE;


    @OneToMany(mappedBy = "meetingRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "meetingroom_equipment",
            joinColumns = @JoinColumn(name = "meetingroom_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private Set<Equipment> equipmentList = new HashSet<>();
}

