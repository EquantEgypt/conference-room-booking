package org.orange.oie.internship2025.conferenceroombooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.orange.oie.internship2025.conferenceroombooking.enums.MeetingRoomStatus;
import org.orange.oie.internship2025.conferenceroombooking.enums.RoomType;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRoomDTO {
    private Long room_id;
    private String name;
    private String building;
    private Integer floor;
    private Integer capacity;
    private RoomType roomType;
    private MeetingRoomStatus status;
    private Set<String> equipmentTypes;
}
