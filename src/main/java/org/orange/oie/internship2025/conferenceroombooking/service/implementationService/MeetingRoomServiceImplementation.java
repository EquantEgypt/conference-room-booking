package org.orange.oie.internship2025.conferenceroombooking.service.implementationService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.orange.oie.internship2025.conferenceroombooking.dto.MeetingRoomDTO;
import org.orange.oie.internship2025.conferenceroombooking.entity.Equipment;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.repository.MeetingRoomRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.interfaceService.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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
    public List<MeetingRoomDTO> getAllMeetingRooms() {
        List<MeetingRoom> rooms = meetingRoomRepository.findAll();

        if (rooms == null) {
            return Collections.emptyList(); // Defensive: avoid NPE
        }

        return rooms.stream()
                .map(room -> {
                    MeetingRoomDTO meetingRoomDTO = objectMapper.convertValue(room, MeetingRoomDTO.class);

                    // Null-safe equipment handling
                    if (room.getEquipmentList() != null) {
                        meetingRoomDTO.setEquipmentTypes(
                                room.getEquipmentList().stream()
                                        .filter(e -> e != null && e.getType() != null)
                                        .map(Equipment::getType)
                                        .collect(Collectors.toSet())
                        );
                    } else {
                        meetingRoomDTO.setEquipmentTypes(Collections.emptySet());
                    }

                    return meetingRoomDTO;
                })
                .collect(Collectors.toList());
    }

//
//    @Override
//    public List<MeetingRoomDTO> getAllMeetingRooms() {
//        List<MeetingRoom> rooms = meetingRoomRepository.findAll();
//
//        return rooms.stream()
//                .map(room -> {
//                    MeetingRoomDTO meetingRoomDTO = objectMapper.convertValue(room, MeetingRoomDTO.class);
//
//                    meetingRoomDTO.setEquipmentTypes(
//                            room.getEquipmentList() == null
//                                    ? Collections.emptySet()
//                                    : room.getEquipmentList().stream()
//                                    .map(Equipment::getType)
//                                    .collect(Collectors.toSet())
//                    );
//
//                    return meetingRoomDTO;
//                })
//                .collect(Collectors.toList());
//    }


}



