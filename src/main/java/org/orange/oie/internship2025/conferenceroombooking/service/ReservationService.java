package org.orange.oie.internship2025.conferenceroombooking.service;

import org.apache.coyote.BadRequestException;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ReservationService {
    ReservationResponse createBooking(ReservationRequest reservationRequest) throws BadRequestException, UsernameNotFoundException;
}
