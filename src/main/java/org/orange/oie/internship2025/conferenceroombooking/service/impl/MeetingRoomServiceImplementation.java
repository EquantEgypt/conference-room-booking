package org.orange.oie.internship2025.conferenceroombooking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.orange.oie.internship2025.conferenceroombooking.dto.MeetingRoomDTO;
import org.orange.oie.internship2025.conferenceroombooking.entity.Equipment;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ResourceNotFoundException;
import org.orange.oie.internship2025.conferenceroombooking.repository.MeetingRoomRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.interfac.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public List<MeetingRoomDTO> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime, int capacity, Set<String> equipmentTypes) {
        List<MeetingRoom> rooms = meetingRoomRepository.findAvailableRooms(
                capacity,
                startTime,
                endTime,
                (equipmentTypes == null) ? Collections.emptySet() : equipmentTypes,
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