package org.orange.oie.internship2025.conferenceroombooking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
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
public class ReservationRequest {
    @NotBlank(message = "type is required")
    private ReservationType type;

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    @NotBlank(message = "date is required")
    @FutureOrPresent
    private LocalDate date;

    @NotBlank(message = "start time is required")
    private LocalTime startTime;

    @NotBlank(message = "end time is required")
    private LocalTime endTime;

    @NotBlank(message = "recurrenceOption is required")
    private RecurrenceOption recurrenceOption = RecurrenceOption.ONE_TIME;

    @NotBlank(message = "room id is required")
    private Long roomId;

    private Long numberOfOccurrences = 1L;
}
