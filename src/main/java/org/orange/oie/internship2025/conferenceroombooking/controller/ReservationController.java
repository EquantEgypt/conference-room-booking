package org.orange.oie.internship2025.conferenceroombooking.controller;


import org.apache.coyote.BadRequestException;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.service.interfaceService.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reserve")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody ReservationRequest reservationRequest) {
        try {
            List<ReservationResponse> responses = reservationService.createBooking(reservationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        } catch (UsernameNotFoundException usernameNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(usernameNotFoundException.getMessage());
        } catch (BadRequestException badRequestException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestException.getMessage());
        }
    }

}
