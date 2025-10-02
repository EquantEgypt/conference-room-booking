package org.orange.oie.internship2025.conferenceroombooking.repository;

import org.orange.oie.internship2025.conferenceroombooking.dto.CalendarView;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
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

    @Query(value = """
             SELECT r FROM Reservation AS r
                         WHERE r.date >= :date AND r.user.userId = :userId\s
                         ORDER BY r.date ASC\s
                         LIMIT 1
            \s""")
    Reservation findUpcomingReservation(@Param("date") LocalDate date, @Param("userId") Long userId);
}