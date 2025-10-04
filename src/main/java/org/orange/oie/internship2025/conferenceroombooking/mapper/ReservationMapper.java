package org.orange.oie.internship2025.conferenceroombooking.mapper;

import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.UserRole;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    @Autowired
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

    public ReservationResponse toResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();

        response.setReservationId(reservation.getReservationId());
        response.setType(reservation.getType());
        response.setTitle(reservation.getTitle());
        response.setDescription(reservation.getDescription());
        response.setDate(reservation.getDate());
        response.setStartTime(reservation.getStartTime());
        response.setEndTime(reservation.getEndTime());
        response.setRecurrenceOption(reservation.getRecurrenceOption());
        response.setRecurrenceEndDate(reservation.getRecurrenceEndDate());
        response.setRoomName(reservation.getRoom().getName());
        response.setRoomId(reservation.getRoom().getRoomId());
        response.setNumberOfOccurrences(reservation.getNumberOfOccurrences());
        response.setParentId(reservation.getParentReservation() != null
                ? reservation.getParentReservation().getReservationId()
                : null);
        response.setUserId(reservation.getUser().getUserId());


        User currentUser = userDetailsServiceImplementation.getCurrentUser();

        boolean isOwner = reservation.getUser().getUserId().equals(currentUser.getUserId());
        response.setOwner(isOwner);

        if (currentUser.getRole().equals(UserRole.MANAGER)) {
          
            response.setBookedBy(isOwner ? "Me" : reservation.getUser().getEmail());
        } else {

            response.setBookedBy(isOwner ? "Me" : null);
        }
        return response;
    }

    public Reservation toEntity(
            ReservationRequest reservationRequest,
            User user,
            MeetingRoom meetingRoom,
            LocalDate recurrenceEndDate,
            Reservation parentReservation
    ) {
        Reservation reservation = new Reservation();
        reservation.setType(reservationRequest.getType());
        reservation.setTitle(reservationRequest.getTitle());
        reservation.setDescription(reservationRequest.getDescription());
        reservation.setStartTime(reservationRequest.getStartTime());
        reservation.setEndTime(reservationRequest.getEndTime());
        reservation.setRecurrenceOption(reservationRequest.getRecurrenceOption());
        reservation.setRecurrenceEndDate(recurrenceEndDate);
        if (parentReservation != null) reservation.setParentReservation(parentReservation);
        reservation.setUser(user);
        reservation.setRoom(meetingRoom);
        reservation.setDate(reservationRequest.getDate());
        reservation.setNumberOfOccurrences(reservationRequest.getNumberOfOccurrences());
        return reservation;
    }

    public List<ReservationResponse> toResponseList(List<Reservation> reservations) {
        return reservations.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Long calcNumberOfRecurrences(Reservation reservation) {
        if (reservation.getRecurrenceOption() == null
                || reservation.getRecurrenceOption() == RecurrenceOption.ONE_TIME) {
            return 1L;
        }

        LocalDate startDate = (reservation.getParentReservation() == null)
                ? reservation.getDate()
                : reservation.getParentReservation().getDate();

        long count;
        switch (reservation.getRecurrenceOption()) {
            case DAILY:
                count = java.time.temporal.ChronoUnit.DAYS.between(startDate, reservation.getRecurrenceEndDate()) + 1;
                break;
            case WEEKLY:
                count = java.time.temporal.ChronoUnit.WEEKS.between(startDate, reservation.getRecurrenceEndDate()) + 1;
                break;
            default:
                throw new IllegalArgumentException("Unsupported recurrence option: " + reservation.getRecurrenceOption());
        }

        return count;
    }
}
