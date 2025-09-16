package org.orange.oie.internship2025.conferenceroombooking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiError {
    DATE_TIME_CONFLICT(HttpStatus.BAD_REQUEST, "the date or time had conflict in other reservation or were inputted incorrectly"),
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "reservation is not found"),
    ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "room is not found"),
    RESERVATION_REQUEST_EXIST(HttpStatus.BAD_REQUEST, "reservation request has conflict between its attributes"),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "user is not found");
    private final HttpStatus status;
    private final String message;
}
