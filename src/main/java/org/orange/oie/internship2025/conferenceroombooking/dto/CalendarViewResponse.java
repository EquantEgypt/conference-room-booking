package org.orange.oie.internship2025.conferenceroombooking.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarViewResponse {
    // room
    private Long roomId;
    private String roomName;
    private Long roomCapacity;

    // reservations
    List<CalendarViewReservation> reservations = new ArrayList<>();
}
