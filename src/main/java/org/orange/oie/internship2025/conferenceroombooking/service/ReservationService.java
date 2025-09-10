package org.orange.oie.internship2025.conferenceroombooking.service;

import org.apache.coyote.BadRequestException;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ReservationService {
    ReservationResponse createBooking(ReservationRequest reservationRequest) throws BadRequestException, UsernameNotFoundException;

    void deleteBooking(Long reservationId) throws ResourceNotFoundException, UsernameNotFoundException;

    ReservationResponse updateBooking(ReservationRequest reservationRequest, Long reservation_id) throws BadRequestException, UsernameNotFoundException;
}
