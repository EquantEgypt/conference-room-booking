package org.orange.oie.internship2025.conferenceroombooking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReservationMapperTest {

    private ReservationMapper reservationMapper;

    @BeforeEach
    void setUp() {
        reservationMapper = new ReservationMapper();
    }

    @Test
    void testToResponse() {
        MeetingRoom room = new MeetingRoom();
        room.setRoomId(10L);

        Reservation reservation = new Reservation();
        reservation.setReservationId(1L);
        reservation.setType(ReservationType.EXTERNAL);
        reservation.setTitle("Team Meeting");
        reservation.setDescription("Team sync");
        reservation.setDate(LocalDate.of(2025, 9, 10));
        reservation.setStartTime(LocalTime.of(9, 0));
        reservation.setEndTime(LocalTime.of(10, 0));
        reservation.setRecurrenceOption(RecurrenceOption.ONE_TIME);
        reservation.setRecurrenceEndDate(null);
        reservation.setRoom(room);

        ReservationResponse response = reservationMapper.toResponse(reservation);

        assertEquals(1L, response.getReservationId());
        assertEquals(ReservationType.EXTERNAL, response.getType());
        assertEquals("Team sync", response.getDescription());
        assertEquals(LocalDate.of(2025, 9, 10), response.getDate());
        assertEquals(LocalTime.of(9, 0), response.getStartTime());
        assertEquals(LocalTime.of(10, 0), response.getEndTime());
        assertEquals(RecurrenceOption.ONE_TIME, response.getRecurrenceOption());
        assertNull(response.getRecurrenceEndDate());
        assertEquals(10L, response.getRoomId());
    }

    @Test
    void testToEntity() {
        ReservationRequest request = new ReservationRequest();
        request.setType(ReservationType.INTERNAL);
        request.setTitle("Training Session");
        request.setDescription("Spring Boot training");
        request.setDate(LocalDate.of(2025, 9, 15));
        request.setStartTime(LocalTime.of(14, 0));
        request.setEndTime(LocalTime.of(16, 0));
        request.setRecurrenceOption(RecurrenceOption.WEEKLY);

        User user = new User();
        user.setUserId(5L);

        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.setRoomId(20L);

        LocalDate recurrenceEndDate = LocalDate.of(2025, 12, 15);

        Reservation reservation = reservationMapper.toEntity(request, user, meetingRoom, recurrenceEndDate, null);

        assertEquals(ReservationType.INTERNAL, reservation.getType());
        assertEquals("Training Session", reservation.getTitle());
        assertEquals("Spring Boot training", reservation.getDescription());
        assertEquals(LocalDate.of(2025, 9, 15), reservation.getDate());
        assertEquals(LocalTime.of(14, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(16, 0), reservation.getEndTime());
        assertEquals(RecurrenceOption.WEEKLY, reservation.getRecurrenceOption());
        assertEquals(LocalDate.of(2025, 12, 15), reservation.getRecurrenceEndDate());
        assertEquals(user, reservation.getUser());
        assertEquals(meetingRoom, reservation.getRoom());
    }

    @Test
    void testToResponseList() {
        MeetingRoom room1 = new MeetingRoom();
        room1.setRoomId(1L);
        Reservation res1 = new Reservation();
        res1.setReservationId(101L);
        res1.setType(ReservationType.EXTERNAL);
        res1.setTitle("Meeting 1");
        res1.setDescription("Desc1");
        res1.setDate(LocalDate.now());
        res1.setStartTime(LocalTime.of(9, 0));
        res1.setEndTime(LocalTime.of(10, 0));
        res1.setRecurrenceOption(RecurrenceOption.ONE_TIME);
        res1.setRecurrenceEndDate(null);
        res1.setRoom(room1);

        MeetingRoom room2 = new MeetingRoom();
        room2.setRoomId(2L);
        Reservation res2 = new Reservation();
        res2.setReservationId(102L);
        res2.setType(ReservationType.INTERNAL);
        res2.setTitle("Meeting 2");
        res2.setDescription("Desc2");
        res2.setDate(LocalDate.now());
        res2.setStartTime(LocalTime.of(14, 0));
        res2.setEndTime(LocalTime.of(16, 0));
        res2.setRecurrenceOption(RecurrenceOption.DAILY);
        res2.setRecurrenceEndDate(LocalDate.now().plusDays(10));
        res2.setRoom(room2);

        List<Reservation> reservations = Arrays.asList(res1, res2);

        List<ReservationResponse> responses = reservationMapper.toResponseList(reservations);

        assertEquals(2, responses.size());
        assertEquals(101L, responses.get(0).getReservationId());
        assertEquals(102L, responses.get(1).getReservationId());
    }
}
