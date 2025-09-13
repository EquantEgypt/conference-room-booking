package org.orange.oie.internship2025.conferenceroombooking.service.interfac;

import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;

import java.util.List;

public interface ReservationService {
    void deleteBooking(Long reservationId);

    ReservationResponse updateBooking(ReservationRequest reservationRequest, Long reservation_id);

    List<ReservationResponse> createBooking(ReservationRequest reservationRequest);
}
