
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
        MeetingRoom room1 = new MeetingRoom();
        room1.setRoomId(1L);
        room1.setName("Conference Room A");
        room1.setGeoLocation("Building A - Ground Floor");
        room1.setBuilding("Building A");
        room1.setFloor(0);
        room1.setCapacity(10);
        room1.setOperatingHoursStart(LocalTime.of(8, 0));
        room1.setOperatingHoursEnd(LocalTime.of(18, 0));
        room1.setRoomType(RoomType.NORMAL);
        room1.setStatus(MeetingRoomStatus.AVAILABLE);
        room1.setEquipmentList(standardEquipment);

        MeetingRoom room2 = new MeetingRoom();
        room2.setRoomId(2L);
        room2.setName("VIP Meeting Room");
        room2.setGeoLocation("Building B - 5th Floor");
        room2.setBuilding("Building B");
        room2.setFloor(5);
        room2.setCapacity(20);
        room2.setOperatingHoursStart(LocalTime.of(7, 0));
        room2.setOperatingHoursEnd(LocalTime.of(20, 0));
        room2.setRoomType(RoomType.VIP);
        room2.setStatus(MeetingRoomStatus.AVAILABLE);
        room2.setEquipmentList(vipEquipment);

        MeetingRoom room3 = new MeetingRoom();
        room3.setRoomId(3L);
        room3.setName("Small Meeting Room");
        room3.setGeoLocation("Building A - 2nd Floor");
        room3.setBuilding("Building A");
        room3.setFloor(2);
        room3.setCapacity(5);
        room3.setOperatingHoursStart(LocalTime.of(9, 0));
        room3.setOperatingHoursEnd(LocalTime.of(17, 0));
        room3.setRoomType(RoomType.NORMAL);
        room3.setStatus(MeetingRoomStatus.UNDER_MAINTENANCE);
        room3.setEquipmentList(basicEquipment);

        MeetingRoom room4 = new MeetingRoom();
        room4.setRoomId(4L);
        room4.setName("Basic Room");
        room4.setGeoLocation("Building C - 1st Floor");
        room4.setBuilding("Building C");
        room4.setFloor(1);
        room4.setCapacity(8);
        room4.setOperatingHoursStart(LocalTime.of(8, 30));
        room4.setOperatingHoursEnd(LocalTime.of(17, 30));
        room4.setRoomType(RoomType.NORMAL);
        room4.setStatus(MeetingRoomStatus.BOOKED);
        room4.setEquipmentList(emptyEquipment);

        meetingRoomList = new ArrayList<>();
        meetingRoomList.add(room1);
        meetingRoomList.add(room2);
        meetingRoomList.add(room3);
        meetingRoomList.add(room4);

        // Initialize meeting room DTOs
        MeetingRoomDTO dto1 = new MeetingRoomDTO();
        dto1.setRoomId(1L);
        dto1.setName("Conference Room A");
        dto1.setBuilding("Building A");
        dto1.setFloor(0);
        dto1.setCapacity(10);
        dto1.setRoomType(RoomType.NORMAL);
        dto1.setStatus(MeetingRoomStatus.AVAILABLE);
        Set<String> standardEquipmentTypes = new HashSet<>();
        standardEquipmentTypes.add("Projector");
        standardEquipmentTypes.add("Whiteboard");
        dto1.setEquipmentTypes(standardEquipmentTypes);

        MeetingRoomDTO dto2 = new MeetingRoomDTO();
        dto2.setRoomId(2L);
        dto2.setName("VIP Meeting Room");
        dto2.setBuilding("Building B");
        dto2.setFloor(5);
        dto2.setCapacity(20);
        dto2.setRoomType(RoomType.VIP);
        dto2.setStatus(MeetingRoomStatus.AVAILABLE);
        Set<String> vipEquipmentTypes = new HashSet<>();
        vipEquipmentTypes.add("Projector");
        vipEquipmentTypes.add("Whiteboard");
        vipEquipmentTypes.add("Video Conferencing");
        vipEquipmentTypes.add("Microphone");
        dto2.setEquipmentTypes(vipEquipmentTypes);

        MeetingRoomDTO dto3 = new MeetingRoomDTO();
        dto3.setRoomId(3L);
        dto3.setName("Small Meeting Room");
        dto3.setBuilding("Building A");
        dto3.setFloor(2);
        dto3.setCapacity(5);
        dto3.setRoomType(RoomType.NORMAL);
        dto3.setStatus(MeetingRoomStatus.UNDER_MAINTENANCE);
        Set<String> basicEquipmentTypes = new HashSet<>();
        basicEquipmentTypes.add("Whiteboard");
        dto3.setEquipmentTypes(basicEquipmentTypes);

        MeetingRoomDTO dto4 = new MeetingRoomDTO();
        dto4.setRoomId(4L);
        dto4.setName("Basic Room");
        dto4.setBuilding("Building C");
        dto4.setFloor(1);
        dto4.setCapacity(8);
        dto4.setRoomType(RoomType.NORMAL);
        dto4.setStatus(MeetingRoomStatus.BOOKED);
        dto4.setEquipmentTypes(new HashSet<>()); // Empty equipment set

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
