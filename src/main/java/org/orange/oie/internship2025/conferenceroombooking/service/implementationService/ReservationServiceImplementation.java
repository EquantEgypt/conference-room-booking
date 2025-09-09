package org.orange.oie.internship2025.conferenceroombooking.service.implementationService;

import org.apache.coyote.BadRequestException;
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
import org.orange.oie.internship2025.conferenceroombooking.service.interfaceService.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public List<ReservationResponse> createBooking(ReservationRequest reservationRequest)
            throws BadRequestException, UsernameNotFoundException {

        User user = userDetailsServiceImplementation.getCurrentUser();

        MeetingRoom meetingRoom = meetingRoomRepository.findById(reservationRequest.getRoomId())
                .orElseThrow(() -> new BadRequestException("Meeting room not found"));




        if (meetingRoom.getStatus() == MeetingRoomStatus.UNDER_MAINTENANCE) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room is UNDER_MAINTENANCE");
        }

        if ((meetingRoom.getRoomType() == RoomType.NORMAL) &&
                (reservationRequest.getType() == ReservationType.EXTERNAL)) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "External meetings are not allowed in Regular rooms");
        }

        if (reservationRequest.getStartTime() == null || reservationRequest.getEndTime() == null) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start and end time are required");
        }


        if (reservationRequest.getStartTime().isAfter(reservationRequest.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time must be before end time");
        }

       
        reservationRequest.setRecurrenceEndDate(null);
        if (!isRoomAvailable(meetingRoom, reservationRequest.getStartTime(), reservationRequest.getEndTime())) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meeting room is booked in this time range");
        }


        RecurrenceOption recurrenceOption = reservationRequest.getRecurrenceOption();
        if (recurrenceOption != null && recurrenceOption != RecurrenceOption.ONE_TIME) {
            LocalDateTime recurrenceEnd = reservationRequest.getStartTime().plusMonths(3);
            reservationRequest.setRecurrenceEndDate(recurrenceEnd);

            List<Reservation> reservations = generateRecurringReservations(reservationRequest, user, meetingRoom);

            for (Reservation reservation : reservations) {
                if (!isRoomAvailable(meetingRoom, reservation.getStartTime(), reservation.getEndTime())) {
                    throw new BadRequestException("Conflict found for time: " + reservation.getStartTime());
                }
            }

            if (reservationRequest.getRoomId() == null || reservationRequest.getType() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reservation data");
            }


            List<Reservation> saved = reservationRepository.saveAll(reservations);
            return saved.stream().map(reservationMapper::toResponse).toList();
        }

        Reservation reservation = reservationMapper.toEntity(reservationRequest, user, meetingRoom);
        if (reservation == null) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create reservation");
        }
        Reservation saved = reservationRepository.save(reservation);
        return List.of(reservationMapper.toResponse(saved));
    }


    private boolean isRoomAvailable(MeetingRoom room, LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> conflictingReservations = reservationRepository.findConflicts(room, startTime, endTime);
        return conflictingReservations.isEmpty();
    }
    private List<Reservation> generateRecurringReservations(ReservationRequest request, User user, MeetingRoom room) {
        List<Reservation> reservations = new ArrayList<>();
        LocalDateTime currentStart = request.getStartTime();
        LocalDateTime currentEnd = request.getEndTime();
        LocalDateTime recurrenceEnd = request.getRecurrenceEndDate();
        RecurrenceOption option = request.getRecurrenceOption();

        while (currentStart.isBefore(recurrenceEnd)) {
            if (!isRoomAvailable(room, currentStart, currentEnd)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conflict found for time: " + currentStart);
            }

            ReservationRequest occurrence = createOccurrenceRequest(request, currentStart, currentEnd);
            Reservation reservation = reservationMapper.toEntity(occurrence, user, room);

            if (reservation != null) {
                reservations.add(reservation);
            }

            switch (option) {
                case DAILY -> { currentStart = currentStart.plusDays(1); currentEnd = currentEnd.plusDays(1); }
                case WEEKLY -> { currentStart = currentStart.plusWeeks(1); currentEnd = currentEnd.plusWeeks(1); }
                case MONTHLY -> { currentStart = currentStart.plusMonths(1); currentEnd = currentEnd.plusMonths(1); }
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown Recurrence Option");
            }
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

