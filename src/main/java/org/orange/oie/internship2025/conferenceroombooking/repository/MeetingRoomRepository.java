package org.orange.oie.internship2025.conferenceroombooking.repository;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
    List<MeetingRoom> findAll();


    @Query("""
                SELECT mr
                FROM MeetingRoom mr
                JOIN mr.equipmentList e
                WHERE mr.capacity >= :capacity
                  AND mr.status = org.orange.oie.internship2025.conferenceroombooking.enums.MeetingRoomStatus.AVAILABLE
                  AND mr.roomId NOT IN (
                        SELECT r.room.roomId
                        FROM Reservation r
                        WHERE r.startTime < :endTime
                          AND r.endTime > :startTime
                  )
                  AND e.type IN :equipmentTypes
                GROUP BY mr
                HAVING COUNT(DISTINCT e.type) = :size
            """)
    List<MeetingRoom> findAvailableRooms(
            @Param("capacity") int capacity,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("equipmentTypes") Set<String> equipmentTypes,
            @Param("size") long size
    );
}