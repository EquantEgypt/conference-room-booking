package org.orange.oie.internship2025.conferenceroombooking.exceptions;

import org.orange.oie.internship2025.conferenceroombooking.dto.ErrorCode;
import org.orange.oie.internship2025.conferenceroombooking.enums.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    ApiError apiError;
    ErrorCode errorCode;

    @ExceptionHandler(DateTimeConflictException.class)
    public ResponseEntity<ErrorCode> handleDateTimeConflict(DateTimeConflictException ex) {
        apiError = ApiError.DATE_TIME_CONFLICT;
        errorCode = new ErrorCode(apiError.getStatus(), ex.getMessage());
        return ResponseEntity.badRequest().body(errorCode);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorCode> handleReservationNotFound(ReservationNotFoundException ex) {
        apiError = ApiError.RESERVATION_NOT_FOUND;
        errorCode = new ErrorCode(apiError.getStatus(), ex.getMessage());
        return ResponseEntity.badRequest().body(errorCode);
    }

    @ExceptionHandler(ReservationRequestConflict.class)
    public ResponseEntity<ErrorCode> handleReservationConflict(ReservationRequestConflict ex) {
        apiError = ApiError.RESERVATION_REQUEST_EXIST;
        errorCode = new ErrorCode(apiError.getStatus(), ex.getMessage());
        return ResponseEntity.badRequest().body(errorCode);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorCode> handleUsernameNotFound(UsernameNotFoundException ex) {
        apiError = ApiError.USER_NOT_FOUND;
        errorCode = new ErrorCode(apiError.getStatus(), ex.getMessage());
        return new ResponseEntity<>(errorCode, HttpStatus.UNAUTHORIZED);
    }
}
