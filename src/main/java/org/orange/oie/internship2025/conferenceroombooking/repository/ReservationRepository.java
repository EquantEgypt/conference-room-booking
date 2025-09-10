package org.orange.oie.internship2025.conferenceroombooking.repository;

import jakarta.validation.constraints.NotNull;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    List<Reservation> findAllByRoomAndEndTimeBetween(MeetingRoom room, @NotNull(message = "End time is required") LocalDateTime endTime, @NotNull(message = "End time is required") LocalDateTime endTime2);

    List<Reservation> findAllByRoomAndStartTimeBetween(MeetingRoom room, @NotNull(message = "Start time is required") LocalDateTime startTime, @NotNull(message = "Start time is required") LocalDateTime startTime2);

    void deleteByReservationIdAndUser(Long reservationId, User user);

    boolean existsByReservationIdAndUser(Long reservationId, User user);
}
