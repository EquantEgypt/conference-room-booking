package org.orange.oie.internship2025.conferenceroombooking.repository;

import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
    List<MeetingRoom> findAll();


    @Query("""
            SELECT mr
            FROM MeetingRoom mr
            LEFT JOIN mr.equipmentList e
            WHERE mr.capacity >= :capacity
              AND mr.status = org.orange.oie.internship2025.conferenceroombooking.enums.MeetingRoomStatus.AVAILABLE
              AND mr.roomId NOT IN (
                    SELECT r.room.roomId
                    FROM Reservation r
                    WHERE r.date = :date
                      AND r.startTime < :endTime
                      AND r.endTime > :startTime
              )
              AND (:size = 0 OR e.type IN :equipmentTypes)
            GROUP BY mr
            HAVING (:size = 0 OR COUNT(DISTINCT e.type) = :size)
            """)
    List<MeetingRoom> findAvailableRooms(
            @Param("capacity") int capacity,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("equipmentTypes") Set<String> equipmentTypes,
            @Param("size") long size
    );


}