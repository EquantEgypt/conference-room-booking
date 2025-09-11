package org.orange.oie.internship2025.conferenceroombooking.controller;


import org.apache.coyote.BadRequestException;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ResourceNotFoundException;
import org.orange.oie.internship2025.conferenceroombooking.service.interfaceService.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long reservation_id) {
        try {
            reservationService.deleteBooking(reservation_id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("reservation: "
                    + reservation_id + " is successfully deleted");
        } catch (UsernameNotFoundException usernameNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(usernameNotFoundException.getMessage());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("reservation not found");
        }
    }

    @PutMapping("/{reservation_id}")
    public ResponseEntity<?> updateBooking(@RequestBody ReservationRequest reservationRequest, @PathVariable Long reservation_id) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(reservationService.updateBooking(reservationRequest, reservation_id));
        } catch (ResourceNotFoundException | BadRequestException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceNotFoundException.getMessage());
        } catch (UsernameNotFoundException usernameNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(usernameNotFoundException.getMessage());
        }
    }

}
