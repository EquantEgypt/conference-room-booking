package org.orange.oie.internship2025.conferenceroombooking.controller;

import org.orange.oie.internship2025.conferenceroombooking.service.interfaceService.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
            return ResponseEntity.ok(meetingRoomService.getAllMeetingRooms());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

}




