package org.orange.oie.internship2025.conferenceroombooking.repository;

import org.orange.oie.internship2025.conferenceroombooking.dto.CalendarView;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    void deleteByReservationIdAndUser(Long reservationId, User user);

    boolean existsByReservationIdAndUser(Long reservationId, User user);

    Reservation findByReservationIdAndUser(Long reservationId, User user);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.room = :room " +
            "AND r.date = :date " +
            "AND r.startTime < :endTime " +
            "AND r.endTime > :startTime")
    List<Reservation> findConflicts(@Param("room") MeetingRoom room,
                                    @Param("date") LocalDate date,
                                    @Param("startTime") LocalTime startTime,
                                    @Param("endTime") LocalTime endTime);


    List<Reservation> findAllByUser(User user);

    void deleteReservationsByParentReservation(Reservation parentReservation);

    @Query("""
            SELECT new org.orange.oie.internship2025.conferenceroombooking.dto.CalendarView(
                   mr.roomId, mr.name, CAST(mr.capacity AS long),
                   r.reservationId, r.type, r.title, r.date,
                   r.startTime, r.endTime, r.recurrenceOption,r.user.userId
            )
            FROM MeetingRoom AS mr
            LEFT JOIN Reservation AS r
              ON mr.roomId = r.room.roomId
             AND r.date = :date
            WHERE mr.status = org.orange.oie.internship2025.conferenceroombooking.enums.MeetingRoomStatus.AVAILABLE
            """)
    List<CalendarView> findRoomsWithReservationsByDate(@Param("date") LocalDate date);


    @Query("""
               SELECT new org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse(
                    r.reservationId,r.type,r.title,r.description,
                    r.date,r.startTime,r.endTime,r.recurrenceOption,
                    r.recurrenceEndDate,r.room.name,r.room.roomId,
                    r.numberOfOccurrences,r.parentReservation.reservationId
               )
               FROM Reservation r
               WHERE :userId = r.user.userId
                 AND r.date >= CURRENT_DATE
                 AND ((:start IS NULL AND :end IS NULL) OR (r.date BETWEEN :start AND :end))
                 AND (:reservationType IS NULL OR r.type = :reservationType)
                 AND (:recurrenceOption IS NULL OR r.recurrenceOption = :recurrenceOption)
               ORDER BY
                    r.date ASC,
                    r.startTime ASC
            """)
    List<ReservationResponse> getReservationWithFilter(
            @Param("start") LocalDate startDate,
            @Param("end") LocalDate endDate,
            @Param("reservationType") ReservationType reservationType,
            @Param("recurrenceOption") RecurrenceOption recurrenceOption,
            @Param("userId") Long userId);

}