package org.orange.oie.internship2025.conferenceroombooking.controller;

import org.orange.oie.internship2025.conferenceroombooking.dto.MeetingRoomDTO;
import org.orange.oie.internship2025.conferenceroombooking.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<MeetingRoomDTO>> getAllMeetingRooms() {
        List<MeetingRoomDTO> rooms = meetingRoomService.getAllMeetingRooms();
        return ResponseEntity.ok(rooms);
    }

}




