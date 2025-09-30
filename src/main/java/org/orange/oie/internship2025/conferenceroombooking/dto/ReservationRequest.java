package org.orange.oie.internship2025.conferenceroombooking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "type is required")
    private ReservationType type;

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    @NotNull(message = "date is required")
    @FutureOrPresent(message = "date must be today or in the future")
    private LocalDate date;

    @NotNull(message = "start time is required")
    private LocalTime startTime;

    @NotNull(message = "end time is required")
    private LocalTime endTime;

    @NotNull(message = "recurrenceOption is required")
    private RecurrenceOption recurrenceOption;

    @NotNull(message = "room id is required")
    private Long roomId;

    private Long numberOfOccurrences = 1L;
}
