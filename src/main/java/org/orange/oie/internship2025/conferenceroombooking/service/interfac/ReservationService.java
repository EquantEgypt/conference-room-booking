package org.orange.oie.internship2025.conferenceroombooking.service.interfac;

import org.orange.oie.internship2025.conferenceroombooking.dto.CalendarViewResponse;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    void deleteBooking(Long reservationId);

    List<ReservationResponse> updateBooking(ReservationRequest reservationRequest, Long reservationId);

    List<ReservationResponse> createBooking(ReservationRequest reservationRequest);

    List<ReservationResponse> getAllReservations();

    ReservationResponse getReservationById(Long reservationId);

    List<CalendarViewResponse> getReservationByDate(LocalDate reservationDate);

    ReservationResponse getUpcomingReservation();
}



