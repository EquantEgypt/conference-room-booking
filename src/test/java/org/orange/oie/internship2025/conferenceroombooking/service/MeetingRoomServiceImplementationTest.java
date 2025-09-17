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
import org.orange.oie.internship2025.conferenceroombooking.exceptions.DateTimeConflictException;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ResourceNotFoundException;
import org.orange.oie.internship2025.conferenceroombooking.repository.MeetingRoomRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.MeetingRoomServiceImplementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
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
        room1.setRoomId(1L);
        room1.setName("Conference Room A");
        room1.setGeoLocation("Building A - Ground Floor");
        room1.setBuilding("Building A");
        room1.setFloor(0);
        room1.setCapacity(10);
        room1.setOperatingHoursStart(LocalTime.of(8, 0));
        room1.setOperatingHoursEnd(LocalTime.of(18, 0));
        room1.setRoomType(RoomType.REGULAR);
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
        room3.setRoomType(RoomType.REGULAR);
        room3.setStatus(MeetingRoomStatus.AVAILABLE);
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
        room4.setRoomType(RoomType.REGULAR);
        room4.setStatus(MeetingRoomStatus.AVAILABLE);
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
        dto3.setStatus(MeetingRoomStatus.AVAILABLE);
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
    void testGetAvailableRooms_WithStandardEquipment() {
        // define parameters
        int capacity = 5;
        LocalDate date = LocalDate.of(2025, 10, 10);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(11, 0);
        Set<String> requiredEquipments = Set.of("Projector", "Whiteboard");

        // Mock repository -> return rooms that satisfy the query
        when(meetingRoomRepository.findAvailableRooms(
                eq(capacity),
                eq(date),
                eq(startTime),
                eq(endTime),
                argThat(set -> set.equals(requiredEquipments)),
                eq((long) requiredEquipments.size())
        )).thenReturn(List.of(meetingRoomList.get(0)));

        // Mock objectMapper -> map entity to DTO
        when(objectMapper.convertValue(meetingRoomList.get(0), MeetingRoomDTO.class))
                .thenReturn(meetingRoomDTOList.get(0));

        // Act
        List<MeetingRoomDTO> result = roomServiceImplementation.getAvailableRooms(
                date, startTime, endTime, capacity, requiredEquipments);

        // Assert
        assertThat(result).hasSize(1);
        MeetingRoomDTO dto = result.getFirst();
        assertThat(dto.getName()).isEqualTo("Conference Room A");
        assertThat(dto.getEquipmentTypes()).containsExactlyInAnyOrder("Projector", "Whiteboard");
    }


    @Test
    void testGetAvailableRooms_WhenEquipmentTypesNull() {
        // define parameters
        int capacity = 8;
        LocalDate date = LocalDate.of(2025, 10, 10);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(15, 0);

        // Mock repository -> return rooms that satisfy the query
        when(meetingRoomRepository.findAvailableRooms(
                eq(capacity),
                eq(date),
                eq(startTime),
                eq(endTime),
                eq(Collections.emptySet()),
                eq(0L)
        )).thenReturn(List.of(meetingRoomList.get(3)));

        // Mock objectMapper -> map entity to DTO
        when(objectMapper.convertValue(meetingRoomList.get(3), MeetingRoomDTO.class))
                .thenReturn(meetingRoomDTOList.get(3));

        // Act
        List<MeetingRoomDTO> result = roomServiceImplementation.getAvailableRooms(
                date, startTime, endTime, capacity, null
        );

        // Assert
        assertThat(result).hasSize(1);
        MeetingRoomDTO dto = result.getFirst();
        assertThat(dto.getName()).isEqualTo("Basic Room");
        assertThat(dto.getEquipmentTypes()).isEmpty();
    }

    @Test
    void getAvailableRoomsShouldReturnEmptyEquipmentTypesWhenEquipmentListIsNull() {
        // Given
        List<MeetingRoom> rooms = meetingRoomList;
        MeetingRoom roomWithoutEquipment = rooms.get(2);

        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = startTime.plusHours(1);

        when(meetingRoomRepository.findAvailableRooms(
                0,
                date,
                startTime,
                endTime,
                Collections.emptySet(),
                0L
        )).thenReturn(List.of(roomWithoutEquipment));

        when(objectMapper.convertValue(roomWithoutEquipment, MeetingRoomDTO.class))
                .thenReturn(meetingRoomDTOList.get(2));

        // When
        List<MeetingRoomDTO> result = roomServiceImplementation.getAvailableRooms(
                date,
                startTime,
                endTime,
                0,
                Collections.emptySet()
        );

        // Then
        assertThat(result).hasSize(1);
        MeetingRoomDTO returned = result.get(0);
        assertThat(returned.getEquipmentTypes()).isEmpty();
    }


    @Test
    void whenGetMeetingRoomByIdShouldReturnMeetingRoomDtoWhenSuccess() {
        //Given
        when(meetingRoomRepository.findById(meetingRoomList.getFirst().getRoomId()))
                .thenReturn(Optional.of(meetingRoomList.getFirst()));

        when(objectMapper.convertValue(meetingRoomList.getFirst(), MeetingRoomDTO.class))
                .thenReturn(meetingRoomDTOList.getFirst());

        //When
        MeetingRoomDTO response = roomServiceImplementation.getMeetingRoomById(meetingRoomList.getFirst().getRoomId());

        //Then
        assertThat(response.getName()).isEqualTo("Conference Room A");
        assertThat(response.getEquipmentTypes()).isNotEmpty();
    }

    @Test
    void whenGetMeetingRoomByIdShouldThrowResourceNotFoundWhenRoomIsNotFound() {
        when(meetingRoomRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            roomServiceImplementation.getMeetingRoomById(1L);
        });
    }

    @Test
    void whenStartTimeWithoutEndTime_thenThrowException() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        LocalTime startTime = LocalTime.of(10, 0);

        assertThatThrownBy(() ->
                roomServiceImplementation.getAvailableRooms(date, startTime, null, 0, Collections.emptySet())
        )
                .isInstanceOf(DateTimeConflictException.class)
                .hasMessage("You must provide both startTime and endTime, or leave both empty.");
    }

    @Test
    void whenEndTimeWithoutStartTime_thenThrowException() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        LocalTime endTime = LocalTime.of(12, 0);

        assertThatThrownBy(() ->
                roomServiceImplementation.getAvailableRooms(date, null, endTime, 0, Collections.emptySet())
        )
                .isInstanceOf(DateTimeConflictException.class)
                .hasMessage("You must provide both startTime and endTime, or leave both empty.");
    }

    @Test
    void whenStartTimeNotBeforeEndTime_thenThrowException() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        LocalTime startTime = LocalTime.of(12, 0);
        LocalTime endTime = LocalTime.of(10, 0); // invalid: end before start

        assertThatThrownBy(() ->
                roomServiceImplementation.getAvailableRooms(date, startTime, endTime, 0, Collections.emptySet())
        )
                .isInstanceOf(DateTimeConflictException.class)
                .hasMessage("startTime must be before endTime");
    }
}