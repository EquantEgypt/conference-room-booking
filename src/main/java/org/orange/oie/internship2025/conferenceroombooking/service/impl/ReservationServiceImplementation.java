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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


//    @Transactional // All operations within this method will be part of a single transaction
//    @Override
//    public List<ReservationResponse> createBooking(ReservationRequest reservationRequest) {
//
//        User user = userDetailsServiceImplementation.getCurrentUser();
//
//        MeetingRoom meetingRoom = meetingRoomRepository.findById(
//                reservationRequest.getRoomId()).orElseThrow(() -> new ReservationRequestConflict("MeetingRoom is not Found"));
//
//        handleReservationForUser(reservationRequest, null);
//        reservationRequest.setRecurrenceEndDate(null);
//        RecurrenceOption recurrenceOption = reservationRequest.getRecurrenceOption();
//        if (recurrenceOption != null && recurrenceOption != RecurrenceOption.ONE_TIME) {
//            LocalDateTime recurrenceEnd = reservationRequest.getStartTime().plusMonths(3);
//            reservationRequest.setRecurrenceEndDate(recurrenceEnd);
//
//            List<Reservation> reservations = generateRecurringReservations(reservationRequest, user, meetingRoom);
//
//            for (Reservation reservation : reservations) {
//                if (isRoomAvailable(meetingRoom, reservation.getStartTime(), reservation.getEndTime())) {
//                    throw new DateTimeConflictException("Conflict found for time: " + reservation.getStartTime());
//                }
//            }
//
//            if (reservationRequest.getRoomId() == null || reservationRequest.getType() == null) {
//                throw new DateTimeConflictException("Invalid reservation data");
//            }
//
//
//            List<Reservation> saved = new ArrayList<>();
//            reservationRepository.saveAll(reservations).forEach(saved::add);
//
//            return saved.stream().map(reservationMapper::toResponse).toList();
//        }
//
//        Reservation reservation = reservationMapper.toEntity(reservationRequest, user, meetingRoom);
//        if (reservation == null) {
//            throw new ReservationRequestConflict("Failed to create reservation");
//        }
//        Reservation saved = reservationRepository.save(reservation);
//        return List.of(reservationMapper.toResponse(saved));
//
//    }


    @Transactional
    @Override
    public List<ReservationResponse> createBooking(ReservationRequest reservationRequest) {

        User user = userDetailsServiceImplementation.getCurrentUser();
        MeetingRoom meetingRoom = meetingRoomRepository.findById(
                reservationRequest.getRoomId()).orElseThrow(() -> new ReservationRequestConflict("MeetingRoom is not Found"));

        handleReservationForUser(reservationRequest, null);


        Reservation parentReservation = reservationMapper.toEntity(reservationRequest, user, meetingRoom);
        parentReservation = reservationRepository.save(parentReservation);

        RecurrenceOption recurrenceOption = reservationRequest.getRecurrenceOption();

        if (recurrenceOption == null || recurrenceOption == RecurrenceOption.ONE_TIME) {
            return List.of(reservationMapper.toResponse(parentReservation));
        }

        LocalDateTime recurrenceEnd = reservationRequest.getStartTime().plusMonths(3);
        reservationRequest.setRecurrenceEndDate(recurrenceEnd);

        List<Reservation> recurringReservations = generateRecurringReservations(reservationRequest, user, meetingRoom);

        for (Reservation child : recurringReservations) {
            child.setParentReservationId(parentReservation);
        }

        reservationRepository.saveAll(recurringReservations);

        List<Reservation> allReservations = new ArrayList<>();
        allReservations.add(parentReservation);
        allReservations.addAll(recurringReservations);

        return allReservations.stream().map(reservationMapper::toResponse).toList();
    }


    @Override
    public List<ReservationResponse> getAllReservations() {
        User user = userDetailsServiceImplementation.getCurrentUser();
        return reservationRepository.findAllByUser(user).stream().map(reservationMapper::toResponse).toList() ;
    }

    @Override
    @Transactional
    public void deleteBooking(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        Reservation parent = (reservation.getParentReservationId() == null)
                ? reservation
                : reservation.getParentReservationId();

        reservationRepository.delete(parent);
    }



    @Override
    public ReservationResponse updateBooking(ReservationRequest reservationRequest, Long reservationId) throws ResourceNotFoundException {
        User user = userDetailsServiceImplementation.getCurrentUser();
        MeetingRoom meetingRoom = meetingRoomRepository.findById(
                reservationRequest.getRoomId()).orElseThrow(() -> new ReservationRequestConflict("MeetingRoom is not Found"));
        if (!reservationRepository.existsByReservationIdAndUser(reservationId, user))
            throw new ReservationNotFoundException("reservation not found");
        Reservation reservation = reservationRepository.findByReservationIdAndUser(reservationId, user);
        handleReservationForUser(reservationRequest, reservation);
        reservation = reservationMapper.toEntity(reservationRequest, user, meetingRoom);
        if (reservation == null) {
            throw new ReservationRequestConflict("Failed to create reservation");
        }
        reservation.setReservationId(reservationId);
        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toResponse(saved);
    }

    private boolean isRoomAvailable(MeetingRoom room, LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> conflictingReservations = reservationRepository.findConflicts(room, startTime, endTime);
        return !conflictingReservations.isEmpty();
    }

    private boolean canUpdateDateTime(Reservation reservation, MeetingRoom room,
                                      LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> reservationList = reservationRepository.findConflicts(room, startTime, endTime);
        return (reservationList.isEmpty()) || (Objects.equals(reservationList.getFirst().getReservationId(), reservation.getReservationId())
                || reservationList.size() != 1L);
    }

    private void handleReservationForUser(ReservationRequest reservationRequest, Reservation reservation) {

        MeetingRoom meetingRoom = meetingRoomRepository.findById(
                reservationRequest.getRoomId()).orElseThrow(() -> new ReservationRequestConflict("MeetingRoom is not Found"));

        if (meetingRoom.getStatus() == MeetingRoomStatus.UNDER_MAINTENANCE) {
            throw new ReservationRequestConflict("Room is UNDER_MAINTENANCE");
        }

        if ((meetingRoom.getRoomType() == RoomType.REGULAR) && (reservationRequest.getType() == ReservationType.EXTERNAL)) {
            throw new ReservationRequestConflict("External meeting can not be normal rooms");
        }

        if (reservationRequest.getStartTime() == null || reservationRequest.getEndTime() == null) {
            throw new DateTimeConflictException("Start and end time are required");
        }

        if ((reservation == null && isRoomAvailable(meetingRoom, reservationRequest.getStartTime(), reservationRequest.getEndTime()))) {
            throw new DateTimeConflictException("meeting room is booked in these range");
        } else if ((reservation != null && !canUpdateDateTime(Objects.requireNonNull(reservation), meetingRoom, reservationRequest.getStartTime(), reservationRequest.getEndTime()))) {
            throw new DateTimeConflictException("meeting room is booked in these range can't update");
        }

    }


    public List<Reservation> generateRecurringReservations(ReservationRequest request, User user, MeetingRoom room) {
        List<Reservation> reservations = new ArrayList<>();
        LocalDateTime currentStart = request.getStartTime();
        LocalDateTime currentEnd = request.getEndTime();
        LocalDateTime recurrenceEnd = request.getRecurrenceEndDate();
        RecurrenceOption option = request.getRecurrenceOption();

        while (true) {
            switch (option) {
                case DAILY -> {
                    currentStart = currentStart.plusDays(1);
                    currentEnd = currentEnd.plusDays(1);
                }
                case WEEKLY -> {
                    currentStart = currentStart.plusWeeks(1);
                    currentEnd = currentEnd.plusWeeks(1);
                }
                case MONTHLY -> {
                    currentStart = currentStart.plusMonths(1);
                    currentEnd = currentEnd.plusMonths(1);
                }
            }

            if (currentStart.isAfter(recurrenceEnd)) break;

            if (isRoomAvailable(room, currentStart, currentEnd)) {
                throw new DateTimeConflictException("Conflict found for time: " + currentStart);
            }

            ReservationRequest occurrence = createOccurrenceRequest(request, currentStart, currentEnd);
            Reservation reservation = reservationMapper.toEntity(occurrence, user, room);
            reservations.add(reservation);
        }

        return reservations;
    }



    private ReservationRequest createOccurrenceRequest(ReservationRequest request, LocalDateTime currentStart, LocalDateTime currentEnd) {
        return new ReservationRequest(
                request.getType(),
                request.getDescription(),
                currentStart,
                currentEnd,
                request.getRecurrenceOption(),
                request.getRecurrenceEndDate(),
                request.getRoomId()
        );
    }

}



