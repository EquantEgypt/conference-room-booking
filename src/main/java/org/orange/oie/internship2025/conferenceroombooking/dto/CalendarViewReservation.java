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
public class CalendarViewReservation {
    private Long reservationId;
    private ReservationType type;
    private String title;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private RecurrenceOption recurrenceOption;
    private boolean myReservation;
}
