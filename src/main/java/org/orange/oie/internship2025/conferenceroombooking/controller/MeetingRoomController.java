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
    public ResponseEntity<List<MeetingRoomDTO>> getAvailableRooms(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) Set<String> equipmentTypes) {

        // Validation: allow only both null OR both non-null
        if ((startTime == null && endTime != null) || (startTime != null && endTime == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must provide both startTime and endTime, or leave both empty.");
        }

        // -- default --
        // if startTime and endTime not entered
        // picking an early date (like 2000-01-01) but within the same day.
        LocalDateTime defaultStart = (startTime == null) ?  LocalDateTime.of(2000, 1, 1, 0, 0) : startTime;
        LocalDateTime defaultEnd = (endTime == null) ? LocalDateTime.of(2000, 1, 1, 23, 59) : endTime;
        Integer defaultCapacity = (capacity == null) ? 1 : capacity;
        Set<String> equipmentTypesSet = equipmentTypes == null ? Collections.emptySet() : equipmentTypes;

        if (defaultStart.isAfter(defaultEnd)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startTime must be before endTime");
        }

        List<MeetingRoomDTO> rooms = meetingRoomService.getAvailableRooms(
                defaultStart,
                defaultEnd,
                defaultCapacity,
                equipmentTypesSet
        );

        return ResponseEntity.ok(rooms);
    }
    @GetMapping("/{id}")
    public ResponseEntity<MeetingRoomDTO> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(meetingRoomService.getMeetingRoomById(id));
    }

}