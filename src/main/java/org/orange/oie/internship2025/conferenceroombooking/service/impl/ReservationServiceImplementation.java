package org.orange.oie.internship2025.conferenceroombooking.service.impl;

import jakarta.validation.constraints.NotNull;
import org.orange.oie.internship2025.conferenceroombooking.dto.*;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.enums.*;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.*;
import org.orange.oie.internship2025.conferenceroombooking.mapper.ReservationMapper;
import org.orange.oie.internship2025.conferenceroombooking.repository.MeetingRoomRepository;
import org.orange.oie.internship2025.conferenceroombooking.repository.ReservationRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.interfac.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
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
                        reservationRequest.getRoomId())
                .orElseThrow(() -> new ApiException(
                        ApiError.ROOM_NOT_FOUND,
                        "Meeting room not found with id: " + reservationRequest.getRoomId()
                ));

        validateReservation(reservationRequest, null); // validation

        List<Reservation> reservations = new ArrayList<>();
        List<ReservationResponse> reservationResponses;

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
    public List<List<ReservationResponse>> getReservationWithFilter(
            DateScope dateScope,
            ReservationType reservationType,
            RecurrenceOption recurrenceOption
    ) {
        LocalDate start = null, end = null;
        if (dateScope != null) {
            if (dateScope == DateScope.TODAY) {
                start = LocalDate.now();
                end = LocalDate.now();
            } else if (dateScope == DateScope.NEXT_DAY) {
                start = LocalDate.now().plusDays(1);
                end = LocalDate.now().plusDays(1);
            } else {
                start = LocalDate.now();
                end = LocalDate.now().plusDays(6);
            }
        }

        Long userId = userDetailsServiceImplementation.getCurrentUser().getUserId();

        List<ReservationResponse> reservationResponse = reservationRepository
                .getReservationWithFilter(start, end, reservationType, recurrenceOption, userId);

        Map<Long, List<ReservationResponse>> map = new TreeMap<>();

        for (ReservationResponse res : reservationResponse) {
            Long id = res.getParentId() != null ? res.getParentId() : res.getReservationId();

            if (map.containsKey(id)) {
                map.get(id).add(res);
            } else {
                List<ReservationResponse> list = new ArrayList<>();
                list.add(res);
                map.put(id, list);
            }
        }
        return new ArrayList<>(map.values());
    }

    @Override
    public ReservationResponse getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApiException(ApiError.RESERVATION_NOT_FOUND,
                        "Reservation not found with id: " + reservationId)
                );
        return reservationMapper.toResponse(reservation);
    }

    @Override
    public List<CalendarViewResponse> getReservationByDate(LocalDate reservationDate) {
        List<CalendarViewResponse> cvResponse = new ArrayList<>();
        List<CalendarView> cvDB = reservationRepository.findRoomsWithReservationsByDate(reservationDate);
        Long userId = userDetailsServiceImplementation.getCurrentUser().getUserId();
        Map<Long, List<CalendarView>> map = new TreeMap<>();

        for (CalendarView item : cvDB) {

            CalendarView reservation = new CalendarView(
                    item.getRoomId(),
                    item.getRoomName(),
                    item.getRoomCapacity(),
                    item.getReservationId(),
                    item.getReservationType(),
                    item.getReservationTitle(),
                    item.getReservationDate(),
                    item.getReservationStartTime(),
                    item.getReservationEndTime(),
                    item.getReservationRecurrenceOption(),
                    item.getUserId()
            );

            if (map.containsKey(item.getRoomId())) {
                map.get(item.getRoomId()).add(reservation);
            } else {
                List<CalendarView> list = new ArrayList<>();
                list.add(reservation);
                map.put(item.getRoomId(), list);
            }
        }

        for (Map.Entry<Long, List<CalendarView>> entry : map.entrySet()) {
            CalendarViewResponse calenderViewResponse = new CalendarViewResponse();

            calenderViewResponse.setRoomId(entry.getKey());
            calenderViewResponse.setRoomName(entry.getValue().getFirst().getRoomName());
            calenderViewResponse.setRoomCapacity(entry.getValue().getFirst().getRoomCapacity());
            calenderViewResponse.setReservations(new ArrayList<>());

            for (CalendarView row : entry.getValue()) {
                if (row.getReservationId() != null) {
                    calenderViewResponse.getReservations().add(
                            new CalendarViewReservation(
                                    row.getReservationId(),
                                    row.getReservationType(),
                                    row.getReservationTitle(),
                                    row.getReservationDate(),
                                    row.getReservationStartTime(),
                                    row.getReservationEndTime(),
                                    row.getReservationRecurrenceOption(),
                                    row.getUserId().equals(userId)
                            )
                    );
                }
            }
            cvResponse.add(calenderViewResponse);
        }
        return cvResponse;
    }

    @Override
    public List<ReservationResponse> getUpcomingReservation() {
        List<Reservation> reservations = reservationRepository.findUpcomingReservation(LocalDate.now(),
                userDetailsServiceImplementation.getCurrentUser().getUserId());

        if (reservations == null) throw new ApiException(ApiError.RESERVATION_NOT_FOUND,"No upcoming reservations");

        return reservations.stream().map(reservationMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public void deleteBooking(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApiException(ApiError.RESERVATION_NOT_FOUND,
                        "Reservation not found with id: " + reservationId)
                );

        Reservation parent = (reservation.getParentReservation() == null)
                ? reservation
                : reservation.getParentReservation();

        reservationRepository.delete(parent);
    }


    @Override
    @Transactional
    public List<ReservationResponse> updateBooking(ReservationRequest request, Long reservation_id){
        User user = userDetailsServiceImplementation.getCurrentUser();

        MeetingRoom meetingRoom = meetingRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ApiException(
                        ApiError.ROOM_NOT_FOUND,
                        "Meeting room not found with id: " + request.getRoomId()
                ));

        if (!reservationRepository.existsByReservationIdAndUser(reservation_id, user))
            throw new ApiException(ApiError.RESERVATION_NOT_FOUND);

        // reservation found
        Reservation oldReservation = reservationRepository.findByReservationIdAndUser(reservation_id, user);


        List<Reservation> reservations = new ArrayList<>();
        List<ReservationResponse> responses;

        // get the parent of reservation
        Reservation parent = (oldReservation.getParentReservation() == null)
                ? oldReservation
                : oldReservation.getParentReservation();

        // simple change
        boolean simpleChange = checkSimpleChange(parent, request);

        if (simpleChange) {

            // set the parent of reservation values from request values
            parent.setType(request.getType());
            parent.setTitle(request.getTitle());
            parent.setDescription(request.getDescription());

            // adding parent to the list
            reservations.add(parent);

            // if parent has children set the children reservations values from request values
            if (parent.getChildReservations() != null) {
                for (Reservation child : parent.getChildReservations()) {
                    child.setType(request.getType());
                    child.setTitle(request.getTitle());
                    child.setDescription(request.getDescription());
                    reservations.add(child);
                }
            }
            responses = reservations.stream().map(reservationMapper::toResponse).collect(Collectors.toList());
            return responses;
        } else { // complex change startTime, endTime, date, recurrence option, no of occurrence
            deleteBooking(parent.getReservationId());
            responses = createBooking(request);
            return responses;
        }
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

        MeetingRoom meetingRoom = meetingRoomRepository.findById(reservationRequest.getRoomId())
                .orElseThrow(() -> new ApiException(
                        ApiError.ROOM_NOT_FOUND,
                        "Meeting room not found with id: " + reservationRequest.getRoomId()
                ));

        if (meetingRoom.getStatus() == MeetingRoomStatus.UNDER_MAINTENANCE) {
            throw new ApiException(ApiError.ROOM_UNDER_MAINTENANCE);
        }

        if ((meetingRoom.getRoomType() == RoomType.REGULAR) && (reservationRequest.getType() == ReservationType.EXTERNAL)) {
            throw new ApiException(ApiError.INVALID_ROOM_TYPE);
        }

        if (!reservationRequest.getStartTime().isBefore(reservationRequest.getEndTime())) {
            throw new ApiException(ApiError.START_AFTER_END);
        }

        if (reservationRequest.getRecurrenceOption() != RecurrenceOption.ONE_TIME && reservationRequest.getNumberOfOccurrences() < 2) {
            throw new ApiException(ApiError.INVALID_RECURRENCE);
        }

        if ((reservation == null && isRoomAvailable(meetingRoom, reservationRequest.getDate(),
                reservationRequest.getStartTime(), reservationRequest.getEndTime()))) {
            throw new ApiException(ApiError.ROOM_ALREADY_BOOKED);
        } else if ((reservation != null && !canUpdateDateTime(Objects.requireNonNull(reservation), meetingRoom, reservationRequest.getDate(),
                reservationRequest.getStartTime(), reservationRequest.getEndTime()))) {
            throw new ApiException(ApiError.ROOM_ALREADY_BOOKED_UPDATE);
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
                throw new ApiException(ApiError.ROOM_ALREADY_BOOKED,"Conflict found for time: " + startTime + " and " + endTime);
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
        reservation.setNumberOfOccurrences(request.getNumberOfOccurrences());

        return reservation;
    }


    private LocalDate getEndDate(@NotNull LocalDate date, RecurrenceOption option, Long numOfOccurrence) {
        switch (option) {
            case DAILY -> {
                return date.plusDays(numOfOccurrence - 1);
            }
            case WEEKLY -> {
                return date.plusWeeks(numOfOccurrence - 1);
            }
            default -> throw new ApiException(ApiError.RESERVATION_REQUEST_CONFLICT,"Unknown Recurrence Option");
        }
    }

    private boolean checkSimpleChange(Reservation oldReservation, ReservationRequest request) {
        return request.getRecurrenceOption().equals(oldReservation.getRecurrenceOption())
                && request.getDate().equals(oldReservation.getDate())
                && request.getStartTime().equals(oldReservation.getStartTime())
                && request.getEndTime().equals(oldReservation.getEndTime());
    }

}

