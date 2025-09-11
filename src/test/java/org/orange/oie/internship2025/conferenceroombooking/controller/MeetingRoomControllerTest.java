package org.orange.oie.internship2025.conferenceroombooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.orange.oie.internship2025.conferenceroombooking.dto.MeetingRoomDTO;
import org.orange.oie.internship2025.conferenceroombooking.entity.Equipment;
import org.orange.oie.internship2025.conferenceroombooking.enums.MeetingRoomStatus;
import org.orange.oie.internship2025.conferenceroombooking.enums.RoomType;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.MeetingRoomServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static javax.management.Query.eq;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
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

    private List<MeetingRoomDTO> meetingRoomDTOList;
    private List<Set<Equipment>> equipmentSetList;

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
        dto1.setRoomId(1L);
        dto1.setName("Conference Room A");
        dto1.setBuilding("Building A");
        dto1.setFloor(0);
        dto1.setCapacity(10);
        dto1.setRoomType(RoomType.REGULAR);
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
        dto3.setRoomType(RoomType.REGULAR);
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
        dto4.setRoomType(RoomType.REGULAR);
        dto4.setStatus(MeetingRoomStatus.AVAILABLE);
        dto4.setEquipmentTypes(new HashSet<>()); // Empty equipment set

        meetingRoomDTOList = new ArrayList<>();
        meetingRoomDTOList.add(dto1);
        meetingRoomDTOList.add(dto2);
        meetingRoomDTOList.add(dto3);
        meetingRoomDTOList.add(dto4);


    }

    @Test
    void getAvailableRoomsShouldReturnStatusOkAndFilteredRooms() throws Exception {
        // Given
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 10, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 9, 10, 11, 0);
        int capacity = 10;
        Set<String> equipmentTypes = new HashSet<>(Arrays.asList("Projector", "Whiteboard"));

        List<MeetingRoomDTO> filteredRooms = Arrays.asList(meetingRoomDTOList.get(0), meetingRoomDTOList.get(1));

        when(roomServiceImplementation.getAvailableRooms(startTime, endTime, capacity, equipmentTypes))
                .thenReturn(filteredRooms);

        // When & Then
        this.mockMvc.perform(get("/rooms")
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString())
                        .param("capacity", String.valueOf(capacity))
                        .param("equipmentTypes", "Projector", "Whiteboard"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(filteredRooms)));
    }


    @Test
    void getAvailableRoomsShouldReturnStatusOkWhenNoCapacityOrEquipmentProvided() throws Exception {
        // Given
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 10, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 9, 10, 11, 0);

        when(roomServiceImplementation.getAvailableRooms(startTime, endTime, 1, Collections.emptySet()))
                .thenReturn(meetingRoomDTOList);

        // When & Then
        this.mockMvc.perform(get("/rooms")
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString()))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(meetingRoomDTOList)));
    }

    @Test
    void getAvailableRoomsShouldReturnEmptyListWhenNoRoomsMatch() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 10, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 9, 10, 11, 0);

        when(roomServiceImplementation.getAvailableRooms(startTime, endTime, 50, Set.of("NonExistentEquipment")))
                .thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/rooms")
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString())
                        .param("capacity", "50")
                        .param("equipmentTypes", "NonExistentEquipment"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAvailableRoomsShouldReturnBadRequestWhenStartTimeAfterEndTime() throws Exception {
        // Given
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 10, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 9, 10, 10, 0);

        // When & Then
        this.mockMvc.perform(get("/rooms")
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenStartTimeWithoutEndTime_thenThrowException() throws Exception {
        this.mockMvc.perform(get("/rooms")
                        .param("startTime", "2025-01-01T10:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("You must provide both startTime and endTime, or leave both empty."));
    }

    @Test
    void whenEndTimeWithoutStartTime_thenThrowException() throws Exception {
        mockMvc.perform(get("/rooms")
                        .param("endTime", "2025-01-01T12:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("You must provide both startTime and endTime, or leave both empty."));
    }

}