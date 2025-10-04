package org.orange.oie.internship2025.conferenceroombooking.repository;

import org.orange.oie.internship2025.conferenceroombooking.dto.CalendarView;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;
import org.orange.oie.internship2025.conferenceroombooking.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

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
                         WHERE r.date = :date AND r.user.userId = :userId\s
                         ORDER BY r.date ASC\s, startTime ASC\s
            \s""")
    List<Reservation> findUpcomingReservation(@Param("date") LocalDate date, @Param("userId") Long userId);



    @Query("""
    SELECT r FROM Reservation r
    WHERE (:start IS NULL OR r.date >= :start)
      AND (:end IS NULL OR r.date <= :end)
      AND (:reservationType IS NULL OR r.type = :reservationType)
      AND (:recurrenceOption IS NULL OR r.recurrenceOption = :recurrenceOption)
      AND (
            (:userId IS NOT NULL AND r.user.userId = :userId)
            OR (:employeeIds IS NOT NULL AND r.user.userId IN :employeeIds)
            OR (:userId IS NULL AND :employeeIds IS NULL)
          )
""")
    List<Reservation> getReservationWithFilter(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("reservationType") ReservationType reservationType,
            @Param("recurrenceOption") RecurrenceOption recurrenceOption,
            @Param("employeeIds") List<Long> employeeIds,
            @Param("userId") Long userId
    );

}
