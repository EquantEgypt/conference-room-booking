package org.orange.oie.internship2025.conferenceroombooking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.enums.MeetingRoomStatus;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;
import org.orange.oie.internship2025.conferenceroombooking.enums.RoomType;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ReservationMapperTest {
    private ReservationMapper reservationMapper;
    private Reservation reservation;
    private User user;
    private MeetingRoom meetingRoom;
    private ReservationRequest reservationRequest;
    private ReservationResponse reservationResponse;

    @BeforeEach
    void init() {
        reservationMapper = new ReservationMapper();
        user = new User();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@orange.com");
        user.setPhone("01234567890");
        user.setPassword("$2y$10$hashedpassword");

        meetingRoom = new MeetingRoom();
        meetingRoom.setRoomId(1L);
        meetingRoom.setName("Conference Room A");
        meetingRoom.setCapacity(10);
        meetingRoom.setStatus(MeetingRoomStatus.AVAILABLE);
        meetingRoom.setRoomType(RoomType.VIP);

        reservationRequest = new ReservationRequest();
        reservationRequest.setType(ReservationType.EXTERNAL);
        reservationRequest.setDescription("Weekly team standup meeting");
        reservationRequest.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        reservationRequest.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        reservationRequest.setRecurrenceOption(RecurrenceOption.WEEKLY);
        reservationRequest.setRoomId(1L);

        reservation = new Reservation();
        reservation.setReservationId(1L);
        reservation.setType(ReservationType.EXTERNAL);
        reservation.setDescription("Weekly team standup meeting");
        reservation.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        reservation.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        reservation.setRecurrenceOption(RecurrenceOption.WEEKLY);
        reservation.setUser(user);
        reservation.setRoom(meetingRoom);

        reservationResponse = new ReservationResponse();
        reservationResponse.setReservationId(1L);
        reservationResponse.setType(ReservationType.EXTERNAL);
        reservationResponse.setDescription("Weekly team standup meeting");
        reservationResponse.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        reservationResponse.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        reservationResponse.setRecurrenceOption(RecurrenceOption.WEEKLY);
    }


    @Test
    void whenReservationMapper_toResponseList_thenReturnListOfReservationResponses() {
        // Given
        List<Reservation> reservationList = List.of(reservation);

        // When
        List<ReservationResponse> responseList = reservationMapper.toResponseList(reservationList);

        // Then
        assertNotNull(responseList);
        assertEquals(1, responseList.size());

        ReservationResponse response = responseList.get(0);
        assertEquals(reservation.getReservationId(), response.getReservationId());
        assertEquals(reservation.getType(), response.getType());
        assertEquals(reservation.getDescription(), response.getDescription());
        assertEquals(reservation.getStartTime(), response.getStartTime());
        assertEquals(reservation.getEndTime(), response.getEndTime());
        assertEquals(reservation.getRecurrenceOption(), response.getRecurrenceOption());
        assertEquals(reservation.getRoom().getRoomId(), response.getRoomId());
    }
}
