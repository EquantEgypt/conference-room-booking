package org.orange.oie.internship2025.conferenceroombooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.orange.oie.internship2025.conferenceroombooking.dto.MeetingRoomDTO;
import org.orange.oie.internship2025.conferenceroombooking.dto.TempRoomDataDto;
import org.orange.oie.internship2025.conferenceroombooking.entity.Equipment;
import org.orange.oie.internship2025.conferenceroombooking.enums.MeetingRoomStatus;
import org.orange.oie.internship2025.conferenceroombooking.enums.RoomType;
import org.orange.oie.internship2025.conferenceroombooking.service.MeetingRoomServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MeetingRoomController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class MeetingRoomControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeetingRoomServiceImplementation roomServiceImplementation;

    @MockBean
    private AuthenticationManager authenticationManager;

    private List<MeetingRoomDTO> meetingRoomDTOList;
    private List<Set<Equipment>> equipmentSetList;
    private List<TempRoomDataDto> tempRoomDataDtoList;

    @BeforeEach
    void init() {
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

        tempRoomDataDtoList = new ArrayList<>();
        tempRoomDataDtoList.add(new TempRoomDataDto(meetingRoomDTOList.get(0), "/images/room1.png"));
        tempRoomDataDtoList.add(new TempRoomDataDto(meetingRoomDTOList.get(1), "/images/room2.png"));
        tempRoomDataDtoList.add(new TempRoomDataDto(meetingRoomDTOList.get(2), "/images/room3.png"));
        tempRoomDataDtoList.add(new TempRoomDataDto(meetingRoomDTOList.get(3), "/images/room4.png"));


    }

    @Test
    void getAllMeetingRoomsShouldReturnStatusOkAndListOfMeetingRoomDTOWhenCalled() throws Exception {
        //Given
        when(roomServiceImplementation.getAllMeetingRooms())
                .thenReturn(meetingRoomDTOList);
        //When & Then
        this.mockMvc.perform(get("/rooms")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tempRoomDataDtoList)));
    }

    @Test
    void getAllMeetingRoomsShouldReturnStatusInternalServerErrorWhenAnyProblemOccur() throws Exception {
        //Given
        when(roomServiceImplementation.getAllMeetingRooms())
                .thenThrow(new RuntimeException("problem occurred"));
        //When & Then
        this.mockMvc.perform(get("/rooms")).andDo(print())
                .andExpect(status().isInternalServerError());
    }
}