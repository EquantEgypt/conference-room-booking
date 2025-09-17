package org.orange.oie.internship2025.conferenceroombooking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.orange.oie.internship2025.conferenceroombooking.dto.MeetingRoomDTO;
import org.orange.oie.internship2025.conferenceroombooking.entity.Equipment;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.DateTimeConflictException;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ResourceNotFoundException;
import org.orange.oie.internship2025.conferenceroombooking.repository.MeetingRoomRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.interfac.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MeetingRoomServiceImplementation implements MeetingRoomService {

    private final MeetingRoomRepository meetingRoomRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public MeetingRoomServiceImplementation(MeetingRoomRepository meetingRoomRepository,
                                            ObjectMapper objectMapper) {
        this.meetingRoomRepository = meetingRoomRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    public List<MeetingRoomDTO> getAvailableRooms(LocalDate date, LocalTime startTime, LocalTime endTime, Integer capacity, Set<String> equipmentTypes) {

        // Validation: allow only both null OR both non-null
        if ((startTime == null && endTime != null) || (startTime != null && endTime == null)) {
            throw new DateTimeConflictException("You must provide both startTime and endTime, or leave both empty.");
        }

        // -- default --
        // if startTime and endTime not entered
        // picking an early date (like 2000-01-01) but within the same day.
        LocalDate defaultDate = (date == null) ? LocalDate.of(2000, 1, 1) : date;
        LocalTime defaultStart = (startTime == null) ? LocalTime.of(0, 0) : startTime;
        LocalTime defaultEnd = (endTime == null) ? LocalTime.of(23, 59) : endTime;
        int defaultCapacity = (capacity == null) ? 1 : capacity;
        Set<String> equipmentTypesSet = equipmentTypes == null ? Collections.emptySet() : equipmentTypes;

        if (!defaultStart.isBefore(defaultEnd)) {
            throw new DateTimeConflictException("startTime must be before endTime");
        }

        List<MeetingRoom> rooms = meetingRoomRepository.findAvailableRooms(
                defaultCapacity,
                defaultDate,
                defaultStart,
                defaultEnd,
                equipmentTypesSet,
                (equipmentTypes == null) ? 0L : equipmentTypes.size()
        );

        return rooms.stream()
                .map(room -> {
                    MeetingRoomDTO meetingRoomDTO = objectMapper.convertValue(room, MeetingRoomDTO.class);

                    meetingRoomDTO.setEquipmentTypes(
                            room.getEquipmentList() == null
                                    ? Collections.emptySet()
                                    : room.getEquipmentList().stream()
                                    .map(Equipment::getType)
                                    .collect(Collectors.toSet())
                    );

                    return meetingRoomDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public MeetingRoomDTO getMeetingRoomById(Long id) {
        MeetingRoom room = meetingRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Meeting room not found with id: " + id));


        MeetingRoomDTO meetingRoomDTO = objectMapper.convertValue(room, MeetingRoomDTO.class);

        meetingRoomDTO.setEquipmentTypes(
                room.getEquipmentList() == null
                        ? Collections.emptySet()
                        : room.getEquipmentList().stream()
                        .map(Equipment::getType)
                        .collect(Collectors.toSet())
        );

        return meetingRoomDTO;
    }

}