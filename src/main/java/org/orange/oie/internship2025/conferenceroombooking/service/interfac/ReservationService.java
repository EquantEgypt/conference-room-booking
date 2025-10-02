package org.orange.oie.internship2025.conferenceroombooking.service.interfac;

import org.orange.oie.internship2025.conferenceroombooking.dto.CalendarViewResponse;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.enums.DateScope;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    void deleteBooking(Long reservationId);
    void deleteAllBookings();

    List<ReservationResponse> updateBooking(ReservationRequest reservationRequest, Long reservationId);

    List<ReservationResponse> createBooking(ReservationRequest reservationRequest);

    List<ReservationResponse> getAllReservations();

    List<List<ReservationResponse>> getReservationWithFilter(DateScope dateScope,
                                                       ReservationType reservationType,
                                                       RecurrenceOption recurrenceOption);

    ReservationResponse getReservationById(Long reservationId);

    List<CalendarViewResponse> getReservationByDate(LocalDate reservationDate);

    List<ReservationResponse> getUpcomingReservation();


    }




