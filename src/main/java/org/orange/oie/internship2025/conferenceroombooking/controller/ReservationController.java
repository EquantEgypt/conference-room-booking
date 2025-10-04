package org.orange.oie.internship2025.conferenceroombooking.controller;


import jakarta.validation.Valid;
import org.orange.oie.internship2025.conferenceroombooking.dto.CalendarViewResponse;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.enums.DateScope;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;
import org.orange.oie.internship2025.conferenceroombooking.service.interfac.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reserve")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> responses = reservationService.getAllReservations();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<List<ReservationResponse>>> getReservationByFilter(
            @RequestParam(required = false) DateScope dateScope,
            @RequestParam(required = false) ReservationType reservationType,
            @RequestParam(required = false) RecurrenceOption recurrenceOption,
            @RequestParam(defaultValue = "false") boolean isManager,
            @RequestParam(required = false) String managerView
    ) {
        List<List<ReservationResponse>> responses = reservationService.getReservationWithFilter(
                dateScope,
                reservationType,
                recurrenceOption,
                isManager,
                managerView
        );

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/up-coming")
    public ResponseEntity<List<ReservationResponse>> getUpcomingReservation() {
        List<ReservationResponse> response = reservationService.getUpcomingReservation();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("{reservationId}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long reservationId) {
        ReservationResponse response = reservationService.getReservationById(reservationId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("date/{reservationDate}")
    public ResponseEntity<List<CalendarViewResponse>> getReservationByDate(@PathVariable LocalDate reservationDate) {
        List<CalendarViewResponse> response = reservationService.getReservationByDate(reservationDate);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<List<ReservationResponse>> createBooking(@RequestBody @Valid ReservationRequest reservationRequest) {
        List<ReservationResponse> responses = reservationService.createBooking(reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    public ResponseEntity<HttpStatus> deleteBooking(@PathVariable Long reservationId) {
        reservationService.deleteBooking(reservationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



    @PutMapping("/{reservationId}")
    public ResponseEntity<List<ReservationResponse>> updateBooking(@RequestBody @Valid  ReservationRequest reservationRequest, @PathVariable Long reservationId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.updateBooking(reservationRequest, reservationId));
    }
}
