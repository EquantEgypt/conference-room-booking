package org.orange.oie.internship2025.conferenceroombooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TempRoomDataDto {
    private MeetingRoomDTO meetingRoomDTO;
    private String imgPath;
}
