package org.orange.oie.internship2025.conferenceroombooking.dto;

import lombok.*;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarView {
    // room
    private Long roomId;
    private String roomName;
    private Long roomCapacity;

    // reservation

    private Long reservationId;
    private ReservationType reservationType;
    private String reservationTitle;
    private LocalDate reservationDate;
    private LocalTime reservationStartTime;
    private LocalTime reservationEndTime;
    private RecurrenceOption reservationRecurrenceOption;
    private Long userId;
}
