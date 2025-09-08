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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        user.setUser_id(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@orange.com");
        user.setPhone("01234567890");
        user.setPassword("$2y$10$hashedpassword");

        meetingRoom = new MeetingRoom();
        meetingRoom.setRoom_id(1L);
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
        reservationRequest.setRoom_id(1L);

        reservation = new Reservation();
        reservation.setReservation_id(1L);
        reservation.setType(ReservationType.EXTERNAL);
        reservation.setDescription("Weekly team standup meeting");
        reservation.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        reservation.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        reservation.setRecurrenceOption(RecurrenceOption.WEEKLY);
        reservation.setUser(user);
        reservation.setRoom(meetingRoom);

        reservationResponse = new ReservationResponse();
        reservationResponse.setReservation_id(1L);
        reservationResponse.setType(ReservationType.EXTERNAL);
        reservationResponse.setDescription("Weekly team standup meeting");
        reservationResponse.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        reservationResponse.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        reservationResponse.setRecurrenceOption(RecurrenceOption.WEEKLY);
    }

    @Test
    void whenReservationMapper_toResponseReturnReservationResponse() {
        //Given
        //When
        ReservationResponse reservationResponse1 = reservationMapper.toResponse(reservation);
        //Then
        assertEquals(reservationResponse.getReservation_id(), reservationResponse1.getReservation_id());
        assertEquals(reservationResponse.getType(), reservationResponse1.getType());
        assertEquals(reservationResponse.getDescription(), reservationResponse1.getDescription());
        assertEquals(reservationResponse.getStartTime(), reservationResponse1.getStartTime());
        assertEquals(reservationResponse.getEndTime(), reservationResponse1.getEndTime());
        assertEquals(reservationResponse.getRecurrenceOption(), reservationResponse1.getRecurrenceOption());
        assertEquals(meetingRoom.getRoom_id(), reservationResponse1.getRoom_id());
    }

    @Test
    void whenReservationMapper_toEntityReturnReservation() {
        //Given
        //When
        Reservation reservation1 = reservationMapper.toEntity(reservationRequest, user, meetingRoom);
        //Then
        assertEquals(reservationRequest.getType(), reservation1.getType());
        assertEquals(reservationRequest.getDescription(), reservation1.getDescription());
        assertEquals(reservationRequest.getStartTime(), reservation1.getStartTime());
        assertEquals(reservationRequest.getEndTime(), reservation1.getEndTime());
        assertEquals(reservationRequest.getRecurrenceOption(), reservation1.getRecurrenceOption());
        assertEquals(user, reservation1.getUser());
        assertEquals(meetingRoom, reservation1.getRoom());
    }
}
