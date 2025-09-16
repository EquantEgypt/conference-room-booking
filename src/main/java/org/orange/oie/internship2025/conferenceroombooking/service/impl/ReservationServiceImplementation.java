package org.orange.oie.internship2025.conferenceroombooking.service.impl;

import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.enums.MeetingRoomStatus;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;
import org.orange.oie.internship2025.conferenceroombooking.enums.RoomType;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.DateTimeConflictException;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ReservationNotFoundException;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ReservationRequestConflict;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ResourceNotFoundException;
import org.orange.oie.internship2025.conferenceroombooking.mapper.ReservationMapper;
import org.orange.oie.internship2025.conferenceroombooking.repository.MeetingRoomRepository;
import org.orange.oie.internship2025.conferenceroombooking.repository.ReservationRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.interfac.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImplementation implements ReservationService {

    private final UserDetailsServiceImplementation userDetailsServiceImplementation;
    private final MeetingRoomRepository meetingRoomRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationServiceImplementation(UserDetailsServiceImplementation userDetailsServiceImplementation
            , MeetingRoomRepository meetingRoomRepository
            , ReservationRepository reservationRepository
            , ReservationMapper reservationMapper) {
        this.userDetailsServiceImplementation = userDetailsServiceImplementation;
        this.meetingRoomRepository = meetingRoomRepository;
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }


    @Transactional // All operations within this method will be part of a single transaction
    @Override
    public List<ReservationResponse> createBooking(ReservationRequest reservationRequest) {

        User user = userDetailsServiceImplementation.getCurrentUser();

        MeetingRoom meetingRoom = meetingRoomRepository.findById(
                reservationRequest.getRoomId()).orElseThrow(() -> new ReservationRequestConflict("MeetingRoom is not Found"));

        validateReservation(reservationRequest, null); // validation

        List<Reservation> reservations = new ArrayList<>();
        List<ReservationResponse> reservationResponses = new ArrayList<>();

        Reservation parentReservation = reservationMapper.toEntity(reservationRequest, user,
                meetingRoom, reservationRequest.getDate(), null);

        reservations.add(parentReservation);

        // create parent reservation for all  ONE_TIME && DAILY && WEEKLY
        if (reservationRequest.getRecurrenceOption() != RecurrenceOption.ONE_TIME) {
            parentReservation.setRecurrenceEndDate(getEndDate(
                    parentReservation.getDate(),
                    parentReservation.getRecurrenceOption(),
                    reservationRequest.getNumberOfOccurrences()
            ));

            generateRecurringReservations(reservationRequest, reservations,
                    parentReservation, user, meetingRoom);
        }
        List<Reservation> savedReservations = new ArrayList<>();
        reservationRepository.saveAll(reservations)
                .forEach(savedReservations::add);

        reservationResponses = savedReservations.stream().map(reservationMapper::toResponse).collect(Collectors.toList());
        return reservationResponses;
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        User user = userDetailsServiceImplementation.getCurrentUser();
        return reservationRepository.findAllByUser(user).stream().map(reservationMapper::toResponse).toList();
    }

    @Override
    @jakarta.transaction.Transactional
    public void deleteBooking(Long reservationId) throws ResourceNotFoundException, UsernameNotFoundException {
        User user = userDetailsServiceImplementation.getCurrentUser();
        if (!reservationRepository.existsByReservationIdAndUser(reservationId, user)) {
            throw new ReservationNotFoundException("reservation is not found");
        }
        reservationRepository.deleteByReservationIdAndUser(reservationId, user);
    }

    @Override
    public ReservationResponse updateBooking(ReservationRequest reservationRequest, Long reservation_id) throws ResourceNotFoundException {
        User user = userDetailsServiceImplementation.getCurrentUser();
        MeetingRoom meetingRoom = meetingRoomRepository.findById(
                reservationRequest.getRoomId()).orElseThrow(() -> new ReservationRequestConflict("MeetingRoom is not Found"));
        if (!reservationRepository.existsByReservationIdAndUser(reservation_id, user))
            throw new ReservationNotFoundException("reservation not found");
        Reservation reservation = reservationRepository.findByReservationIdAndUser(reservation_id, user);

        if (reservation.getChildReservations() != null) {
            reservation.getChildReservations().clear();
        }

        validateReservation(reservationRequest, reservation);

        // Update existing reservation instead of creating new one
        reservation.setType(reservationRequest.getType());
        reservation.setTitle(reservationRequest.getTitle());
        reservation.setDescription(reservationRequest.getDescription());
        reservation.setDate(reservationRequest.getDate());
        reservation.setStartTime(reservationRequest.getStartTime());
        reservation.setEndTime(reservationRequest.getEndTime());
        reservation.setRecurrenceOption(reservationRequest.getRecurrenceOption());
        reservation.setRoom(meetingRoom);

        reservation.setReservationId(reservation_id);
        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toResponse(saved);
    }

    private boolean isRoomAvailable(MeetingRoom room, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Reservation> conflictingReservations = reservationRepository.findConflicts(room, date, startTime, endTime);
        return !conflictingReservations.isEmpty();
    }

    private boolean canUpdateDateTime(Reservation reservation, MeetingRoom room,
                                      LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Reservation> reservationList = reservationRepository.findConflicts(room, date, startTime, endTime);
        return (reservationList.isEmpty()) || (Objects.equals(reservationList.getFirst().getReservationId(), reservation.getReservationId())
                || reservationList.size() != 1L);
    }

    private void validateReservation(ReservationRequest reservationRequest, Reservation reservation) {

        MeetingRoom meetingRoom = meetingRoomRepository.findById(
                reservationRequest.getRoomId()).orElseThrow(() -> new ReservationRequestConflict("MeetingRoom is not Found"));

        if (meetingRoom.getStatus() == MeetingRoomStatus.UNDER_MAINTENANCE) {
            throw new ReservationRequestConflict("Room is UNDER_MAINTENANCE");
        }

        if ((meetingRoom.getRoomType() == RoomType.REGULAR) && (reservationRequest.getType() == ReservationType.EXTERNAL)) {
            throw new ReservationRequestConflict("External meeting can not be normal rooms");
        }

        if (!reservationRequest.getStartTime().isBefore(reservationRequest.getEndTime())) {
            throw new DateTimeConflictException("start must be before end.");
        }

        if ((reservation == null && isRoomAvailable(meetingRoom, reservationRequest.getDate(),
                reservationRequest.getStartTime(), reservationRequest.getEndTime()))) {
            throw new DateTimeConflictException("meeting room is booked in these range");
        } else if ((reservation != null && !canUpdateDateTime(Objects.requireNonNull(reservation), meetingRoom, reservationRequest.getDate(),
                reservationRequest.getStartTime(), reservationRequest.getEndTime()))) {
            throw new DateTimeConflictException("meeting room is booked in these range can't update");
        }

    }

    public void generateRecurringReservations(ReservationRequest request, List<Reservation> reservations,
                                              Reservation parentReservation, User user, MeetingRoom room) {
        LocalTime startTime = request.getStartTime();
        LocalTime endTime = request.getEndTime();
        LocalDate start = parentReservation.getDate();
        LocalDate end = parentReservation.getRecurrenceEndDate();

        while (start.isBefore(end)) {
            start = getEndDate(start, request.getRecurrenceOption(), 2L);

            if (isRoomAvailable(room, start, startTime, endTime)) {
                throw new DateTimeConflictException("Conflict found for time: " + startTime + " and " + endTime);
            }

            reservations.add(createOccurrence(request, start, end, parentReservation, room, user));
        }
    }

    private Reservation createOccurrence(ReservationRequest request,
                                         LocalDate date,
                                         LocalDate recurrenceEndDate,
                                         Reservation parentReservation,
                                         MeetingRoom room,
                                         User user) {

        Reservation reservation = new Reservation();

        reservation.setType(request.getType());
        reservation.setTitle(request.getTitle());
        reservation.setDescription(request.getDescription());
        reservation.setDate(date);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setRecurrenceOption(request.getRecurrenceOption());
        reservation.setParentReservation(parentReservation);
        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setRecurrenceEndDate(recurrenceEndDate);

        return reservation;
    }


    private LocalDate getEndDate(LocalDate date, RecurrenceOption option, Long numOfOccurrence) {
        switch (option) {
            case DAILY -> {
                return date.plusDays(numOfOccurrence - 1);
            }
            case WEEKLY -> {
                return date.plusWeeks(numOfOccurrence - 1);
            }
            default -> throw new ReservationRequestConflict("Unknown Recurrence Option");
        }
    }
}

