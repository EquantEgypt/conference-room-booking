package org.orange.oie.internship2025.conferenceroombooking.repository;

import jakarta.validation.constraints.NotNull;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    List<Reservation> findAllByRoomAndEndTimeBetween(MeetingRoom room,
                                                     @NotNull(message = "End time is required") LocalDateTime endTime,
                                                     @NotNull(message = "End time is required") LocalDateTime endTime2);

    List<Reservation> findAllByRoomAndStartTimeBetween(MeetingRoom room,
                                                       @NotNull(message = "Start time is required") LocalDateTime startTime,
                                                       @NotNull(message = "Start time is required") LocalDateTime startTime2);

    void deleteByReservationIdAndUser(Long reservationId, User user);

    boolean existsByReservationIdAndUser(Long reservationId, User user);

    Reservation findByReservationIdAndUser(Long reservationId, User user);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.room = :room " +
            "AND r.startTime < :endTime " +
            "AND r.endTime > :startTime")
    List<Reservation> findConflicts(@Param("room") MeetingRoom room,
                                    @Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime);

    List<Reservation> findAllByUser(User user);
}