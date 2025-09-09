package org.orange.oie.internship2025.conferenceroombooking.controller;
import org.orange.oie.internship2025.conferenceroombooking.dto.MeetingRoomDTO;
import org.orange.oie.internship2025.conferenceroombooking.service.interfac.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;


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
    @GetMapping("/available")
    public List<MeetingRoomDTO> getAvailableRooms(
            @RequestParam(required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) Set<String> equipmentTypes) {

        if (startTime.isAfter(endTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startTime must be before endTime");
        }

        try {
            List<MeetingRoomDTO> rooms = meetingRoomService.getAvailableRooms(
                    startTime,
                    endTime,
                    capacity == null ? 0 : capacity,
                    equipmentTypes == null ? Collections.emptySet() : equipmentTypes
            );
            return rooms;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Service failure", e);
        }
    }
}


//        LocalDate today = LocalDate.now();
//        LocalDateTime startDateTime = LocalDateTime.of(today, LocalTime.parse(startTime));
//        LocalDateTime endDateTime = LocalDateTime.of(today, LocalTime.parse(endTime));

