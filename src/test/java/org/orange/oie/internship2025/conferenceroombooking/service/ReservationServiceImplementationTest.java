package org.orange.oie.internship2025.conferenceroombooking.service;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import org.orange.oie.internship2025.conferenceroombooking.mapper.ReservationMapper;
import org.orange.oie.internship2025.conferenceroombooking.repository.MeetingRoomRepository;
import org.orange.oie.internship2025.conferenceroombooking.repository.ReservationRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.ReservationServiceImplementation;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.UserDetailsServiceImplementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplementationTest {
    @Mock
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

    @Mock
    private MeetingRoomRepository meetingRoomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImplementation reservationServiceImplementation;

    private Reservation reservation;
    private User user;
    private MeetingRoom meetingRoom;
    private ReservationRequest reservationRequest;
    private ReservationResponse reservationResponse;

    @BeforeEach
    void init() {
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
    void createBookingShouldReturnReservationResponseWhenCreateBookingSuccess() throws Exception {
        //Given
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationMapper.toEntity(any(ReservationRequest.class), any(User.class), any(MeetingRoom.class)))
                .thenReturn(reservation);
        when(reservationRepository.findAllByRoomAndStartTimeBetween(any(MeetingRoom.class)
                , any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(reservationRepository.findAllByRoomAndEndTimeBetween(any(MeetingRoom.class)
                , any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        //When
        ReservationResponse response = reservationServiceImplementation.createBooking(reservationRequest);
        //Then
        assertEquals(response.getReservationId(), reservationResponse.getReservationId());
        assertEquals(response.getStartTime(), reservationResponse.getStartTime());
        assertEquals(response.getEndTime(), reservationResponse.getEndTime());
        assertEquals(response.getType(), reservationResponse.getType());
    }

    @Test
    void createBookingShouldThrowBadRequestExceptionWhenRoomIsUnderMaintenance() {
        //Given
        meetingRoom.setStatus(MeetingRoomStatus.UNDER_MAINTENANCE);
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));

        //When & Then
        assertThrows(BadRequestException.class, () -> {
            reservationServiceImplementation.createBooking(reservationRequest);
        });

    }

    @Test
    void createBookingShouldThrowBadRequestExceptionWhenRoomTypeIsRegularAndReservationTypeIsExternal() {
        //Given
        meetingRoom.setRoomType(RoomType.REGULAR);
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));

        //When & Then
        assertThrows(BadRequestException.class, () -> {
            reservationServiceImplementation.createBooking(reservationRequest);
        });

    }

    @Test
    void createBookingShouldThrowBadRequestExceptionWhenRoomIsBookedInTimeRange() {
        //Given
        Reservation reservation = new Reservation();
        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.findAllByRoomAndStartTimeBetween(any(MeetingRoom.class)
                , any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(reservationList);

        //When & Then
        assertThrows(BadRequestException.class, () -> {
            reservationServiceImplementation.createBooking(reservationRequest);
        });

    }

    @Test
    void createBookingShouldThrowBadRequestExceptionWhenRoomIsNotFound() {
        //Given
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(BadRequestException.class, () -> {
            reservationServiceImplementation.createBooking(reservationRequest);
        });

    }

}
