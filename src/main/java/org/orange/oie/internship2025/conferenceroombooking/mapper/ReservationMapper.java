package org.orange.oie.internship2025.conferenceroombooking.mapper;

import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getType(),
                reservation.getTitle(),
                reservation.getDescription(),
                reservation.getDate(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getRecurrenceOption(),
                reservation.getRecurrenceEndDate(),
                reservation.getRoom().getName(),
                reservation.getRoom().getRoomId(),
                calcNumberOfRecurrences(reservation)
        );
    }

    public Reservation toEntity(ReservationRequest reservationRequest, User user, MeetingRoom meetingRoom, LocalDate recurrenceEndDate
            ,Reservation parentReservation) {
        Reservation reservation = new Reservation();
        reservation.setType(reservationRequest.getType());
        reservation.setTitle(reservationRequest.getTitle());
        reservation.setDescription(reservationRequest.getDescription());
        reservation.setStartTime(reservationRequest.getStartTime());
        reservation.setEndTime(reservationRequest.getEndTime());
        reservation.setRecurrenceOption(reservationRequest.getRecurrenceOption());
        reservation.setRecurrenceEndDate(recurrenceEndDate);
        if(parentReservation != null) reservation.setParentReservation(parentReservation);
        reservation.setUser(user);
        reservation.setRoom(meetingRoom);
        reservation.setDate(reservationRequest.getDate());
        return reservation;
    }

    public List<ReservationResponse> toResponseList(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Long calcNumberOfRecurrences(Reservation reservation) {
        if (reservation.getRecurrenceOption() == null || reservation.getRecurrenceOption() == RecurrenceOption.ONE_TIME) {
            return 1L;
        }

        LocalDate startDate = reservation.getDate();
        long count = 0;

        switch (reservation.getRecurrenceOption()) {
            case DAILY:
                count = java.time.temporal.ChronoUnit.DAYS.between(startDate, reservation.getRecurrenceEndDate()) + 1;
                break;
            case WEEKLY:
                count = java.time.temporal.ChronoUnit.WEEKS.between(startDate, reservation.getRecurrenceEndDate()) + 1;
                break;
            default: throw new IllegalArgumentException("Unsupported recurrence option: " + reservation.getRecurrenceOption());
        }

        return count;
    }
}
