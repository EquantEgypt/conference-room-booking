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
    private MeetingRoomServiceImplementation roomServiceImplementation; // target to be tested

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
        Set<Equipment> basicEquipment = null;

        Set<Equipment> standardEquipment = new HashSet<>();
        standardEquipment.add(projector);
        standardEquipment.add(whiteboard);

        Set<Equipment> vipEquipment = new HashSet<>();
        vipEquipment.add(projector);
        vipEquipment.add(whiteboard);
        vipEquipment.add(videoConferencing);
        vipEquipment.add(microphone);

        Set<Equipment> emptyEquipment = new HashSet<>();

        equipmentSetList = new ArrayList<>();
        equipmentSetList.add(basicEquipment);
        equipmentSetList.add(standardEquipment);
        equipmentSetList.add(vipEquipment);
        equipmentSetList.add(emptyEquipment);

        // Initialize meeting rooms
        MeetingRoom room1 = new MeetingRoom();
        room1.setRoom_id(1L);
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
        room2.setRoom_id(2L);
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
        room3.setRoom_id(3L);
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
        room4.setRoom_id(4L);
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
        dto1.setRoom_id(1L);
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
        dto2.setRoom_id(2L);
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
        dto3.setRoom_id(3L);
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
        dto4.setRoom_id(4L);
        dto4.setName("Basic Room");
        dto4.setBuilding("Building C");
        dto4.setFloor(1);
        dto4.setCapacity(8);
        dto4.setRoomType(RoomType.NORMAL);
        dto4.setStatus(MeetingRoomStatus.BOOKED);
        dto4.setEquipmentTypes(new HashSet<>()); // Empty equipment set

        meetingRoomDTOList = new ArrayList<>();
        meetingRoomDTOList.add(dto1);
        meetingRoomDTOList.add(dto2);
        meetingRoomDTOList.add(dto3);
        meetingRoomDTOList.add(dto4);
    }

    @Test
    void shouldReturnListOfMeetingRoomDTOWhenGetAllMeetingRoomsIsCalled() {
        //Given
        when(meetingRoomRepository.findAll()).thenReturn(meetingRoomList);
        when(objectMapper.convertValue(meetingRoomList.get(0), MeetingRoomDTO.class))
                .thenReturn(meetingRoomDTOList.get(0));
        when(objectMapper.convertValue(meetingRoomList.get(1), MeetingRoomDTO.class))
                .thenReturn(meetingRoomDTOList.get(1));
        when(objectMapper.convertValue(meetingRoomList.get(2), MeetingRoomDTO.class))
                .thenReturn(meetingRoomDTOList.get(2));
        when(objectMapper.convertValue(meetingRoomList.get(3), MeetingRoomDTO.class))
                .thenReturn(meetingRoomDTOList.get(3));
        //When
        List<MeetingRoomDTO> meetingRoomDTOList = roomServiceImplementation.getAllMeetingRooms();
        //Then
        assertNotNull(meetingRoomDTOList);

        for (int i = 0; i < meetingRoomDTOList.size(); i++) {
            assertEquals(meetingRoomDTOList.get(i).getRoom_id(), meetingRoomList.get(i).getRoom_id());
            assertEquals(meetingRoomDTOList.get(i).getName(), meetingRoomList.get(i).getName());
            assertEquals(meetingRoomDTOList.get(i).getRoomType(), meetingRoomList.get(i).getRoomType());
            assertEquals(meetingRoomDTOList.get(i).getFloor(), meetingRoomList.get(i).getFloor());
            assertEquals(meetingRoomDTOList.get(i).getCapacity(), meetingRoomList.get(i).getCapacity());
            assertEquals(meetingRoomDTOList.get(i).getStatus(), meetingRoomList.get(i).getStatus());
            assertEquals(meetingRoomDTOList.get(i).getBuilding(), meetingRoomList.get(i).getBuilding());
        }
    }
}