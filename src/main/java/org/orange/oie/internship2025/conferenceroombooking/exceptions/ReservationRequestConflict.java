package org.orange.oie.internship2025.conferenceroombooking.exceptions;

public class ReservationRequestConflict extends RuntimeException {
    public ReservationRequestConflict(String message) {
        super(message);
    }
}
