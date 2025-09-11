package org.orange.oie.internship2025.conferenceroombooking.mapper;

import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getType(),
                reservation.getDescription(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getRecurrenceOption(),
                reservation.getRecurrenceEndDate(),
                reservation.getRoom().getRoomId()
        );
    }

    public Reservation toEntity(ReservationRequest reservationRequest, User user, MeetingRoom meetingRoom) {
        Reservation reservation = new Reservation();
        reservation.setType(reservationRequest.getType());
        reservation.setDescription(reservationRequest.getDescription());
        reservation.setStartTime(reservationRequest.getStartTime());
        reservation.setEndTime(reservationRequest.getEndTime());
        reservation.setRecurrenceOption(reservationRequest.getRecurrenceOption());
        reservation.setRecurrenceEndDate(reservationRequest.getRecurrenceEndDate());
        reservation.setUser(user);
        reservation.setRoom(meetingRoom);
        return reservation;
    }
    public List<ReservationResponse> toResponseList(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
