package org.orange.oie.internship2025.conferenceroombooking.service.interfac;
import org.orange.oie.internship2025.conferenceroombooking.dto.MeetingRoomDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


public interface MeetingRoomService {
    List<MeetingRoomDTO> getAvailableRooms(
            LocalDateTime startTime,
            LocalDateTime endTime,
            int capacity,
            Set<String> equipmentTypes
    );
    MeetingRoomDTO getMeetingRoomById(Long id);

}