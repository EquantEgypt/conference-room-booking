package org.orange.oie.internship2025.conferenceroombooking.service.interfac;
import org.orange.oie.internship2025.conferenceroombooking.dto.MeetingRoomDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;


public interface MeetingRoomService {
    List<MeetingRoomDTO> getAvailableRooms(
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            Integer capacity,
            Set<String> equipmentTypes
    );
    MeetingRoomDTO getMeetingRoomById(Long id);

}