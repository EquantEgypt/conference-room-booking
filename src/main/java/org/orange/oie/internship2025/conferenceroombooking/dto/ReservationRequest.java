package org.orange.oie.internship2025.conferenceroombooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    private ReservationType type;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RecurrenceOption recurrenceOption;
    private Long roomId;
}
