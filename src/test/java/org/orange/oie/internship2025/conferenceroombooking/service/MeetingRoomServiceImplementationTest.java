
package org.orange.oie.internship2025.conferenceroombooking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orange.oie.internship2025.conferenceroombooking.dto.MeetingRoomDTO;
import org.orange.oie.internship2025.conferenceroombooking.entity.Equipment;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.enums.MeetingRoomStatus;
import org.orange.oie.internship2025.conferenceroombooking.enums.RoomType;
import org.orange.oie.internship2025.conferenceroombooking.repository.MeetingRoomRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.implementationService.MeetingRoomServiceImplementation;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingRoomServiceImplementationTest {

    @Mock
    private MeetingRoomRepository meetingRoomRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MeetingRoomServiceImplementation roomServiceImplementation;

    private List<Set<Equipment>> equipmentSetList;
    private List<MeetingRoom> meetingRoomList;
    private List<MeetingRoomDTO> meetingRoomDTOList;

    @BeforeEach
    public void init() {
        // Initialize equipment
        Equipment projector = new Equipment(1L, "Projector", new HashSet<>());
        Equipment whiteboard = new Equipment(2L, "Whiteboard", new HashSet<>());
        Equipment videoConferencing = new Equipment(3L, "Video Conferencing", new HashSet<>());
        Equipment microphone = new Equipment(4L, "Microphone", new HashSet<>());

        // Create equipment sets
        Set<Equipment> standardEquipment = new HashSet<>();
        standardEquipment.add(projector);
        standardEquipment.add(whiteboard);

        Set<Equipment> vipEquipment = new HashSet<>();
        vipEquipment.add(projector);
        vipEquipment.add(whiteboard);
        vipEquipment.add(videoConferencing);
        vipEquipment.add(microphone);

        Set<Equipment> basicEquipment = null;
        Set<Equipment> emptyEquipment = new HashSet<>();

        equipmentSetList = new ArrayList<>();
        equipmentSetList.add(basicEquipment);
        equipmentSetList.add(standardEquipment);
        equipmentSetList.add(vipEquipment);
        equipmentSetList.add(emptyEquipment);

        // Initialize meeting rooms
        meetingRoomList = new ArrayList<>();
        meetingRoomList.add(createRoom(1L, "Conference Room A", RoomType.NORMAL, MeetingRoomStatus.AVAILABLE, standardEquipment));
        meetingRoomList.add(createRoom(2L, "VIP Meeting Room", RoomType.VIP, MeetingRoomStatus.AVAILABLE, vipEquipment));
        meetingRoomList.add(createRoom(3L, "Small Meeting Room", RoomType.NORMAL, MeetingRoomStatus.UNDER_MAINTENANCE, basicEquipment));
        meetingRoomList.add(createRoom(4L, "Basic Room", RoomType.NORMAL, MeetingRoomStatus.BOOKED, emptyEquipment));

        // Initialize DTOs
        meetingRoomDTOList = new ArrayList<>();
        meetingRoomDTOList.add(createRoomDTO(1L, "Conference Room A", RoomType.NORMAL, MeetingRoomStatus.AVAILABLE, "Projector", "Whiteboard"));
        meetingRoomDTOList.add(createRoomDTO(2L, "VIP Meeting Room", RoomType.VIP, MeetingRoomStatus.AVAILABLE, "Projector", "Whiteboard", "Video Conferencing", "Microphone"));
        meetingRoomDTOList.add(createRoomDTO(3L, "Small Meeting Room", RoomType.NORMAL, MeetingRoomStatus.UNDER_MAINTENANCE, "Whiteboard"));
        meetingRoomDTOList.add(createRoomDTO(4L, "Basic Room", RoomType.NORMAL, MeetingRoomStatus.BOOKED));
    }

    private MeetingRoom createRoom(Long id, String name, RoomType roomType, MeetingRoomStatus status, Set<Equipment> equipmentList) {
        MeetingRoom room = new MeetingRoom();
        room.setRoomId(id);
        room.setName(name);
        room.setBuilding(name); // Assuming building is the same as name for this example
        room.setFloor(0); // Set a default floor
        room.setCapacity(10); // Default capacity
        room.setOperatingHoursStart(LocalTime.of(8, 0));
        room.setOperatingHoursEnd(LocalTime.of(18, 0));
        room.setRoomType(roomType);
        room.setStatus(status);
        room.setEquipmentList(equipmentList);
        return room;
    }

    private MeetingRoomDTO createRoomDTO(Long id, String name, RoomType roomType, MeetingRoomStatus status, String... equipment) {
        MeetingRoomDTO dto = new MeetingRoomDTO();
        dto.setRoomId(id);
        dto.setName(name);
        dto.setBuilding(name);
        dto.setFloor(0); // Default floor
        dto.setCapacity(10); // Default capacity
        dto.setRoomType(roomType);
        dto.setStatus(status);
        Set<String> equipmentTypes = new HashSet<>();
        for (String eq : equipment) {
            equipmentTypes.add(eq);
        }
        dto.setEquipmentTypes(equipmentTypes);
        return dto;
    }

    @Test
    void shouldReturnListOfMeetingRoomDTOWhenGetAllMeetingRoomsIsCalled() {
        // Given
        when(meetingRoomRepository.findAll()).thenReturn(meetingRoomList);

        // Mock ObjectMapper to return corresponding DTO for each room
        when(objectMapper.convertValue(meetingRoomList.get(0), MeetingRoomDTO.class)).thenReturn(meetingRoomDTOList.get(0));
        when(objectMapper.convertValue(meetingRoomList.get(1), MeetingRoomDTO.class)).thenReturn(meetingRoomDTOList.get(1));
        when(objectMapper.convertValue(meetingRoomList.get(2), MeetingRoomDTO.class)).thenReturn(meetingRoomDTOList.get(2));
        when(objectMapper.convertValue(meetingRoomList.get(3), MeetingRoomDTO.class)).thenReturn(meetingRoomDTOList.get(3));

        // When
        List<MeetingRoomDTO> result = roomServiceImplementation.getAllMeetingRooms();

        // Then
        assertNotNull(result);
        assertEquals(4, result.size());

        // Validate all fields
        for (int i = 0; i < meetingRoomDTOList.size(); i++) {
            assertEquals(meetingRoomDTOList.get(i).getRoomId(), result.get(i).getRoomId());
            assertEquals(meetingRoomDTOList.get(i).getName(), result.get(i).getName());
            assertEquals(meetingRoomDTOList.get(i).getRoomType(), result.get(i).getRoomType());
            assertEquals(meetingRoomDTOList.get(i).getStatus(), result.get(i).getStatus());
            assertEquals(meetingRoomDTOList.get(i).getBuilding(), result.get(i).getBuilding());
            assertEquals(meetingRoomDTOList.get(i).getFloor(), result.get(i).getFloor());
            assertEquals(meetingRoomDTOList.get(i).getCapacity(), result.get(i).getCapacity());
            assertEquals(meetingRoomDTOList.get(i).getEquipmentTypes(), result.get(i).getEquipmentTypes());
        }
    }
}
