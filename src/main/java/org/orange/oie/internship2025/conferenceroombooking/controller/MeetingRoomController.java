package org.orange.oie.internship2025.conferenceroombooking.controller;

import org.orange.oie.internship2025.conferenceroombooking.dto.MeetingRoomDTO;
import org.orange.oie.internship2025.conferenceroombooking.dto.TempRoomDataDto;
import org.orange.oie.internship2025.conferenceroombooking.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/rooms")
public class MeetingRoomController {

    private final MeetingRoomService meetingRoomService;

    @Autowired
    public MeetingRoomController(MeetingRoomService meetingRoomService) {
        this.meetingRoomService = meetingRoomService;
    }

    @GetMapping
    public ResponseEntity<?> getAllMeetingRooms() {
        try {
            List<MeetingRoomDTO> meetingRooms = meetingRoomService.getAllMeetingRooms();
            List<TempRoomDataDto> roomDataDos = new ArrayList<>();
            for (int i = 0; i < meetingRooms.size(); i++) {
                roomDataDos.add(new TempRoomDataDto(
                        meetingRooms.get(i),
                        "/images/room" + (i + 1) + ".png"
                ));
            }
            return ResponseEntity.ok(roomDataDos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

}




