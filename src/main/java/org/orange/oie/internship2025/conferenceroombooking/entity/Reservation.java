package org.orange.oie.internship2025.conferenceroombooking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;


    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_type", nullable = false, length = 20)
    private ReservationType type;

    @Column(nullable = false)
    private String title;

    @NotNull(message = "Description is required")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "date is required")
    @Column(nullable = false)
    private LocalDate date;


    @NotNull(message = "Start time is required")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_option", nullable = false, length = 20)
    private RecurrenceOption recurrenceOption = RecurrenceOption.ONE_TIME;

    @Column(name = "recurrence_end_date")
    private LocalDate recurrenceEndDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    @JsonBackReference(value = "parent-movement")
    private Reservation parentReservation;

    @OneToMany(mappedBy = "parentReservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "parent-movement")
    private List<Reservation> childReservations;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-movement")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @JsonBackReference(value = "meeting-room-movement")
    private MeetingRoom room;

}
