package org.orange.oie.internship2025.conferenceroombooking.mapper;

import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getReservation_id(),
                reservation.getType(),
                reservation.getDescription(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getRecurrenceOption(),
                reservation.getRoom().getRoom_id()
        );
    }

    public Reservation toEntity(ReservationRequest reservationRequest, User user, MeetingRoom meetingRoom) {
        Reservation reservation = new Reservation();
        reservation.setType(reservationRequest.getType());
        reservation.setDescription(reservationRequest.getDescription());
        reservation.setStartTime(reservationRequest.getStartTime());
        reservation.setEndTime(reservationRequest.getEndTime());
        reservation.setRecurrenceOption(reservationRequest.getRecurrenceOption());
        reservation.setUser(user);
        reservation.setRoom(meetingRoom);
        return reservation;
    }
}
