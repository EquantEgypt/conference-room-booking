package org.orange.oie.internship2025.conferenceroombooking.controller;


import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.service.interfac.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ReservationResponse>> createBooking(@RequestBody ReservationRequest reservationRequest) {
        List<ReservationResponse> responses = reservationService.createBooking(reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @DeleteMapping("/{reservation_id}")
    public ResponseEntity<HttpStatus> deleteBooking(@PathVariable Long reservation_id) {
        reservationService.deleteBooking(reservation_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{reservation_id}")
    public ResponseEntity<ReservationResponse> updateBooking(@RequestBody ReservationRequest reservationRequest, @PathVariable Long reservation_id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.updateBooking(reservationRequest, reservation_id));
    }

}
