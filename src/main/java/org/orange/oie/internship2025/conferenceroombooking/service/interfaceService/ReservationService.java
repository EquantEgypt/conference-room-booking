package org.orange.oie.internship2025.conferenceroombooking.service.interfaceService;

import org.apache.coyote.BadRequestException;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface ReservationService {
    List<ReservationResponse> createBooking(ReservationRequest reservationRequest)
            throws BadRequestException, UsernameNotFoundException;
}
