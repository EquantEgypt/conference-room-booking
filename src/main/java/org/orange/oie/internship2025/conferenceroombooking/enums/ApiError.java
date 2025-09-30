package org.orange.oie.internship2025.conferenceroombooking.enums;
import org.springframework.http.HttpStatus;


public enum ApiError {
    INCOMPLETE_TIME_RANGE_FILTER(HttpStatus.BAD_REQUEST, "You must provide both startTime and endTime, or leave both empty"),
    START_AFTER_END(HttpStatus.BAD_REQUEST, "startTime must be before endTime"),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Reservation not found"),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "Meeting room not found"),
    ROOM_UNDER_MAINTENANCE(HttpStatus.BAD_REQUEST, "Room is under maintenance"),
    INVALID_ROOM_TYPE(HttpStatus.BAD_REQUEST, "External meetings cannot be held in regular rooms"),
    INVALID_RECURRENCE(HttpStatus.BAD_REQUEST, "Number of occurrences must be ≥ 2 for daily or weekly recurrence"),
    ROOM_ALREADY_BOOKED(HttpStatus.CONFLICT, "Meeting room is already booked for the given time range"),
    RESERVATION_REQUEST_CONFLICT(HttpStatus.BAD_REQUEST, "Reservation request has conflicting attributes"),
    ROOM_ALREADY_BOOKED_UPDATE(HttpStatus.CONFLICT, "Meeting room is already booked for this range, cannot update"),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Username not found");



    private final HttpStatus httpStatus;
    private final String defaultMessage;

    ApiError(HttpStatus httpStatus, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
