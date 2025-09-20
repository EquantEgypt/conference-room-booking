package org.orange.oie.internship2025.conferenceroombooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private Long reservationId;
    private ReservationType type;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private RecurrenceOption recurrenceOption;
    private LocalDate recurrenceEndDate;
    private String roomName;
    private Long roomId;
    private Long numberOfReccurrences;
}
