package org.orange.oie.internship2025.conferenceroombooking.controller;

import org.orange.oie.internship2025.conferenceroombooking.dto.MeetingRoomDTO;
import org.orange.oie.internship2025.conferenceroombooking.service.interfac.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
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
    public ResponseEntity<List<MeetingRoomDTO>> getAvailableRooms(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) Set<String> equipmentTypes) {

        List<MeetingRoomDTO> availableRooms = meetingRoomService.getAvailableRooms(
                date,
                startTime,
                endTime,
                capacity,
                equipmentTypes
        );

        return ResponseEntity.ok(availableRooms);
    }


    @GetMapping("/{id}")
    public ResponseEntity<MeetingRoomDTO> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(meetingRoomService.getMeetingRoomById(id));
    }

}
