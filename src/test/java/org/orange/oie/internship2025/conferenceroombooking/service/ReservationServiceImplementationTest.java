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
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ResourceNotFoundException;
import org.orange.oie.internship2025.conferenceroombooking.mapper.ReservationMapper;
import org.orange.oie.internship2025.conferenceroombooking.repository.MeetingRoomRepository;
import org.orange.oie.internship2025.conferenceroombooking.repository.ReservationRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.ReservationServiceImplementation;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.UserDetailsServiceImplementation;
import org.springframework.web.server.ResponseStatusException;

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
        reservationRequest.setRecurrenceOption(RecurrenceOption.ONE_TIME);
        reservationRequest.setRoomId(1L);

        reservation = new Reservation();
        reservation.setReservationId(1L);
        reservation.setType(ReservationType.EXTERNAL);
        reservation.setDescription("Weekly team standup meeting");
        reservation.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        reservation.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        reservation.setRecurrenceOption(RecurrenceOption.ONE_TIME);
        reservation.setUser(user);
        reservation.setRoom(meetingRoom);

        reservationResponse = new ReservationResponse();
        reservationResponse.setReservationId(1L);
        reservationResponse.setType(ReservationType.EXTERNAL);
        reservationResponse.setDescription("Weekly team standup meeting");
        reservationResponse.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        reservationResponse.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        reservationResponse.setRecurrenceOption(RecurrenceOption.ONE_TIME);
    }

    @Test
    void createBookingShouldReturnReservationResponseWhenCreateBookingSuccess() throws Exception {
        //Given
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationMapper.toEntity(any(ReservationRequest.class), any(User.class), any(MeetingRoom.class)))
                .thenReturn(reservation);
        when(reservationRepository.findConflicts(any(MeetingRoom.class)
                , any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        //When
        List<ReservationResponse> responses = reservationServiceImplementation.createBooking(reservationRequest);

        //Then
        assertEquals(1, responses.size());
        ReservationResponse response = responses.get(0);
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
        // Given
        Reservation reservation = new Reservation();
        reservation.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        reservation.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);

        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.findConflicts(any(MeetingRoom.class),
                any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(reservationList);

        // When & Then
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


    @Test
    void createBookingShouldThrowBadRequestWhenStartTimeIsAfterEndTime() {
        reservationRequest.setStartTime(LocalDateTime.of(2024, 1, 15, 11, 0));
        reservationRequest.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));

        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));

        assertThrows(ResponseStatusException.class, () -> {
            reservationServiceImplementation.createBooking(reservationRequest);
        });
    }

    @Test
    void createBookingShouldThrowBadRequestWhenStartOrEndTimeIsNull() {
        reservationRequest.setStartTime(null);
        reservationRequest.setEndTime(null);

        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));

        assertThrows(ResponseStatusException.class, () -> {
            reservationServiceImplementation.createBooking(reservationRequest);
        });
    }

    @Test
    void createRecurringReservationShouldReturnReservationResponseWhenCreateRecurringBookingSuccess_Daily() throws Exception {
        // Given
        reservationRequest.setRecurrenceOption(RecurrenceOption.DAILY);
        reservationRequest.setRecurrenceEndDate(LocalDateTime.of(2024, 2, 15, 9, 0));

        Reservation recurringReservation = new Reservation();
        recurringReservation.setReservationId(2L);
        recurringReservation.setType(ReservationType.EXTERNAL);
        recurringReservation.setDescription("Daily team standup meeting ");
        recurringReservation.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        recurringReservation.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        recurringReservation.setRecurrenceOption(RecurrenceOption.DAILY);
        recurringReservation.setUser(user);
        recurringReservation.setRoom(meetingRoom);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(recurringReservation);

        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.findConflicts(any(MeetingRoom.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(reservationMapper.toEntity(any(ReservationRequest.class), any(User.class), any(MeetingRoom.class)))
                .thenReturn(recurringReservation);
        when(reservationRepository.saveAll(any(List.class))).thenReturn(reservations);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);

        // When
        List<ReservationResponse> responses = reservationServiceImplementation.createBooking(reservationRequest);

        // Then
        assertEquals(1, responses.size());
        ReservationResponse response = responses.get(0);
        assertEquals(response.getReservationId(), reservationResponse.getReservationId());
        assertEquals(response.getStartTime(), recurringReservation.getStartTime());
        assertEquals(response.getEndTime(), recurringReservation.getEndTime());
        assertEquals(response.getType(), recurringReservation.getType());
    }


    @Test
    void createRecurringReservationShouldReturnReservationResponseWhenCreateRecurringBookingSuccess_Weekly() throws Exception {
        // Given
        reservationRequest.setRecurrenceOption(RecurrenceOption.WEEKLY);
        reservationRequest.setRecurrenceEndDate(LocalDateTime.of(2024, 2, 15, 9, 0));


        Reservation recurringReservation = new Reservation();
        recurringReservation.setReservationId(2L);
        recurringReservation.setType(ReservationType.EXTERNAL);
        recurringReservation.setDescription("Weekly team standup meeting ");
        recurringReservation.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        recurringReservation.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        recurringReservation.setRecurrenceOption(RecurrenceOption.WEEKLY);
        recurringReservation.setUser(user);
        recurringReservation.setRoom(meetingRoom);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(recurringReservation);

        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.findConflicts(any(MeetingRoom.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(reservationMapper.toEntity(any(ReservationRequest.class), any(User.class), any(MeetingRoom.class)))
                .thenReturn(recurringReservation);
        when(reservationRepository.saveAll(any(List.class))).thenReturn(reservations);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);

        // When
        List<ReservationResponse> responses = reservationServiceImplementation.createBooking(reservationRequest);

        // Then
        assertEquals(1, responses.size());
        ReservationResponse response = responses.get(0);
        assertEquals(response.getReservationId(), reservationResponse.getReservationId());
        assertEquals(response.getStartTime(), recurringReservation.getStartTime());
        assertEquals(response.getEndTime(), recurringReservation.getEndTime());
        assertEquals(response.getType(), recurringReservation.getType());
    }


    @Test
    void createRecurringReservationShouldReturnReservationResponseWhenCreateRecurringBookingSuccess_Monthly() throws Exception {
        // Given
        reservationRequest.setRecurrenceOption(RecurrenceOption.MONTHLY);
        reservationRequest.setRecurrenceEndDate(LocalDateTime.of(2024, 2, 15, 9, 0));


        Reservation recurringReservation = new Reservation();
        recurringReservation.setReservationId(2L);
        recurringReservation.setType(ReservationType.EXTERNAL);
        recurringReservation.setDescription("Weekly team standup meeting ");
        recurringReservation.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        recurringReservation.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        recurringReservation.setRecurrenceOption(RecurrenceOption.MONTHLY);
        recurringReservation.setUser(user);
        recurringReservation.setRoom(meetingRoom);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(recurringReservation);

        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.findConflicts(any(MeetingRoom.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(reservationMapper.toEntity(any(ReservationRequest.class), any(User.class), any(MeetingRoom.class)))
                .thenReturn(recurringReservation);
        when(reservationRepository.saveAll(any(List.class))).thenReturn(reservations);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);

        // When
        List<ReservationResponse> responses = reservationServiceImplementation.createBooking(reservationRequest);

        // Then
        assertEquals(1, responses.size());
        ReservationResponse response = responses.get(0);
        assertEquals(response.getReservationId(), reservationResponse.getReservationId());
        assertEquals(response.getStartTime(), recurringReservation.getStartTime());
        assertEquals(response.getEndTime(), recurringReservation.getEndTime());
        assertEquals(response.getType(), recurringReservation.getType());
    }


    @Test
    void createRecurringReservationShouldReturnReservationResponseWhenCreateRecurringBookingFailuer_Weekly() throws Exception {

        reservationRequest.setRecurrenceOption(RecurrenceOption.WEEKLY);
        reservationRequest.setRecurrenceEndDate(LocalDateTime.of(2024, 1, 15, 9, 0));


        Reservation recurringReservation = new Reservation();
        recurringReservation.setReservationId(2L);
        recurringReservation.setType(ReservationType.EXTERNAL);
        recurringReservation.setDescription("Weekly team standup meeting ");
        recurringReservation.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        recurringReservation.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        recurringReservation.setRecurrenceOption(RecurrenceOption.MONTHLY);
        recurringReservation.setUser(user);
        recurringReservation.setRoom(meetingRoom);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(recurringReservation);


        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.findConflicts(any(MeetingRoom.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(reservationMapper.toEntity(any(ReservationRequest.class), any(User.class), any(MeetingRoom.class)))
                .thenReturn(recurringReservation);
        when(reservationRepository.saveAll(any(List.class))).thenReturn(reservations);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);


        List<ReservationResponse> responses = reservationServiceImplementation.createBooking(reservationRequest);


        assertEquals(1, responses.size());
        ReservationResponse response = responses.get(0);
        assertEquals(response.getReservationId(), reservationResponse.getReservationId());
        assertEquals(response.getStartTime(), recurringReservation.getStartTime());
        assertEquals(response.getEndTime(), recurringReservation.getEndTime());
        assertEquals(response.getType(), recurringReservation.getType());

    }

    @Test
    void createRecurringReservationShouldReturnReservationResponseWhenCreateRecurringBookingFailuer_Monthly() throws Exception {

        reservationRequest.setRecurrenceOption(RecurrenceOption.MONTHLY);
        reservationRequest.setRecurrenceEndDate(LocalDateTime.of(2024, 1, 15, 9, 0));


        Reservation recurringReservation = new Reservation();
        recurringReservation.setReservationId(2L);
        recurringReservation.setType(ReservationType.EXTERNAL);
        recurringReservation.setDescription("Weekly team standup meeting ");
        recurringReservation.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        recurringReservation.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        recurringReservation.setRecurrenceOption(RecurrenceOption.WEEKLY);
        recurringReservation.setUser(user);
        recurringReservation.setRoom(meetingRoom);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(recurringReservation);


        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.findConflicts(any(MeetingRoom.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(reservationMapper.toEntity(any(ReservationRequest.class), any(User.class), any(MeetingRoom.class)))
                .thenReturn(recurringReservation);
        when(reservationRepository.saveAll(any(List.class))).thenReturn(reservations);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);


        List<ReservationResponse> responses = reservationServiceImplementation.createBooking(reservationRequest);


        assertEquals(1, responses.size());
        ReservationResponse response = responses.get(0);
        assertEquals(response.getReservationId(), reservationResponse.getReservationId());
        assertEquals(response.getStartTime(), recurringReservation.getStartTime());
        assertEquals(response.getEndTime(), recurringReservation.getEndTime());
        assertEquals(response.getType(), recurringReservation.getType());

    }


    @Test
    void createRecurringReservationShouldThrowBadRequestExceptionWhenRoomIsNotFound() {
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> {
            reservationServiceImplementation.createBooking(reservationRequest);
        });
    }

    @Test
    void createRecurringReservationShouldThrowBadRequestExceptionWhenStartOrEndTimeIsNull() {
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        assertThrows(ResponseStatusException.class, () -> {
            reservationServiceImplementation.createBooking(reservationRequest);
        });
    }

    @Test
    void createRecurringReservationShouldThrowBadRequestExceptionWhenStartOrEndTimeIsEmpty() {
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        assertThrows(ResponseStatusException.class, () -> {
            reservationServiceImplementation.createBooking(reservationRequest);
        });

    }


    @Test
    void createBookingShouldThrowBadRequestExceptionWhenRoomIsBooked() {
        // Given
        meetingRoom.setStatus(MeetingRoomStatus.AVAILABLE);
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            reservationServiceImplementation.createBooking(reservationRequest);
        });
    }


    @Test
    void shouldReturnVoidWhenSuccessfulDelete() {
        //Given
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(reservationRepository.existsByReservationIdAndUser(anyLong(), any(User.class)))
                .thenReturn(true);
        //When & Then
        reservationServiceImplementation.deleteBooking(1L);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenReservationNotFound() {
        //Given
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(reservationRepository.existsByReservationIdAndUser(anyLong(), any(User.class)))
                .thenReturn(false);
        //When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            reservationServiceImplementation.deleteBooking(1L);
        });
    }

    @Test
    void updateBookingShouldReturnReservationResponseWhenUpdateBookingSuccess() throws Exception {
        //Given
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.existsByReservationIdAndUser(anyLong(), any(User.class)))
                .thenReturn(true);
        when(reservationRepository.findByReservationIdAndUser(anyLong(), any(User.class)))
                .thenReturn(reservation);
        when(reservationMapper.toEntity(any(ReservationRequest.class), any(User.class), any(MeetingRoom.class)))
                .thenReturn(reservation);
        when(reservationRepository.findConflicts(any(MeetingRoom.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);
        //When
        ReservationResponse response = reservationServiceImplementation.updateBooking(reservationRequest, reservation.getReservationId());
        //Then
        assertEquals(response.getReservationId(), reservationResponse.getReservationId());
        assertEquals(response.getStartTime(), reservationResponse.getStartTime());
        assertEquals(response.getEndTime(), reservationResponse.getEndTime());
        assertEquals(response.getType(), reservationResponse.getType());
    }

    @Test
    void updateBookingShouldThrowResourceNotFoundWhenReservationIsNotFound() {
        //Given
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.existsByReservationIdAndUser(anyLong(), any(User.class)))
                .thenReturn(false);

        //When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            reservationServiceImplementation.updateBooking(reservationRequest, reservation.getReservationId());
        });

    }

    @Test
    void updateBookingShouldReturnReservationResponseWhenUpdateBookingSuccessAndNoTimeConflictInCanUpdateDateTime() throws Exception {
        //Given
        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(reservationRepository.existsByReservationIdAndUser(anyLong(), any(User.class)))
                .thenReturn(true);
        when(reservationRepository.findByReservationIdAndUser(anyLong(), any(User.class)))
                .thenReturn(reservation);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.findConflicts(any(MeetingRoom.class)
                , any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(reservationList);
        when(reservationMapper.toEntity(any(ReservationRequest.class), any(User.class), any(MeetingRoom.class)))
                .thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);

        //When
        ReservationResponse response = reservationServiceImplementation.updateBooking(reservationRequest, reservation.getReservationId());
        //Then
        assertEquals(response.getReservationId(), reservationResponse.getReservationId());
        assertEquals(response.getStartTime(), reservationResponse.getStartTime());
        assertEquals(response.getEndTime(), reservationResponse.getEndTime());
        assertEquals(response.getType(), reservationResponse.getType());
    }

    @Test
    void updateBookingShouldReturnBadRequestWhenUpdateBookingHasDateTimeConflict() {
        //Given
        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);
        reservationList.add(reservation);
        when(userDetailsServiceImplementation.getCurrentUser()).thenReturn(user);
        when(reservationRepository.existsByReservationIdAndUser(anyLong(), any(User.class)))
                .thenReturn(true);
        when(reservationRepository.findByReservationIdAndUser(anyLong(), any(User.class)))
                .thenReturn(reservation);
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.of(meetingRoom));
        when(reservationRepository.findConflicts(any(MeetingRoom.class)
                , any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(reservationList);

        //When & Then
        assertThrows(ResponseStatusException.class, () -> {
            reservationServiceImplementation.updateBooking(reservationRequest, reservation.getReservationId());
        });
    }

}
