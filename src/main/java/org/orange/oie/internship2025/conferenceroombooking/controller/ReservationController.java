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

    @GetMapping()
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> responses = reservationService.getAllReservations();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("{reservationId}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long reservationId) {
        ReservationResponse response = reservationService.getReservationById(reservationId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<List<ReservationResponse>> createBooking(@RequestBody ReservationRequest reservationRequest) {
        List<ReservationResponse> responses = reservationService.createBooking(reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<HttpStatus> deleteBooking(@PathVariable Long reservationId) {
        reservationService.deleteBooking(reservationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<List<ReservationResponse>> updateBooking(@RequestBody ReservationRequest reservationRequest, @PathVariable Long reservationId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.updateBooking(reservationRequest, reservationId));
    }
}
