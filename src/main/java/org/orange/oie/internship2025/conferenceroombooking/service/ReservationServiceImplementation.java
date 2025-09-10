package org.orange.oie.internship2025.conferenceroombooking.service;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.orange.oie.internship2025.conferenceroombooking.entity.Reservation;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.enums.MeetingRoomStatus;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;
import org.orange.oie.internship2025.conferenceroombooking.enums.RoomType;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ResourceNotFoundException;
import org.orange.oie.internship2025.conferenceroombooking.mapper.ReservationMapper;
import org.orange.oie.internship2025.conferenceroombooking.repository.MeetingRoomRepository;
import org.orange.oie.internship2025.conferenceroombooking.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


    @Override
    public ReservationResponse createBooking(ReservationRequest reservationRequest) throws BadRequestException, UsernameNotFoundException {
        return handleReservationForUser(reservationRequest, null);
    }

    @Override
    @Transactional
    public void deleteBooking(Long reservationId) throws ResourceNotFoundException, UsernameNotFoundException {
        User user = userDetailsServiceImplementation.getCurrentUser();
        if (!reservationRepository.existsByReservationIdAndUser(reservationId, user)) {
            throw new ResourceNotFoundException("reservation is not found");
        }
        reservationRepository.deleteByReservationIdAndUser(reservationId, user);
    }

    @Override
    public ReservationResponse updateBooking(ReservationRequest reservationRequest, Long reservation_id) throws ResourceNotFoundException
            , BadRequestException
            , UsernameNotFoundException {
        User user = userDetailsServiceImplementation.getCurrentUser();
        if (!reservationRepository.existsByReservationIdAndUser(reservation_id, user))
            throw new ResourceNotFoundException("reservation not found");
        Reservation reservation = reservationRepository.findByReservationIdAndUser(reservation_id, user);
        return handleReservationForUser(reservationRequest, reservation);
    }

    private boolean isAvailable(MeetingRoom room, LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> reservationList = reservationRepository.findAllByRoomAndStartTimeBetween(room, startTime, endTime);
        if (!reservationList.isEmpty()) return false;
        reservationList = reservationRepository.findAllByRoomAndEndTimeBetween(room, startTime, endTime);
        return reservationList.isEmpty();
    }

    private boolean canUpdateDateTime(Reservation reservation, MeetingRoom room,
                                      LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> reservationList = reservationRepository.findAllByRoomAndStartTimeBetween(room, startTime, endTime);
        if (!reservationList.isEmpty() && (!Objects.equals(reservationList.getFirst().getReservationId(), reservation.getReservationId())
                || reservationList.size() != 1L)) return false;
        reservationList = reservationRepository.findAllByRoomAndEndTimeBetween(room, startTime, endTime);
        return (reservationList.isEmpty()) || (Objects.equals(reservationList.getFirst().getReservationId(), reservation.getReservationId())
                || reservationList.size() != 1L);
    }

    private ReservationResponse handleReservationForUser(ReservationRequest reservationRequest, Reservation reservation) throws BadRequestException, UsernameNotFoundException {
        User user = userDetailsServiceImplementation.getCurrentUser();

        MeetingRoom meetingRoom = meetingRoomRepository.findById(
                reservationRequest.getRoomId()).orElseThrow(() -> new BadRequestException("MeetingRoom is not Found"));

        if (meetingRoom.getStatus() == MeetingRoomStatus.UNDER_MAINTENANCE) {
            throw new BadRequestException("Room is UNDER_MAINTENANCE");
        }

        if ((meetingRoom.getRoomType() == RoomType.NORMAL) && (reservationRequest.getType() == ReservationType.EXTERNAL)) {
            throw new BadRequestException("External meeting can not be normal rooms");
        }

        if ((reservation == null && !isAvailable(meetingRoom, reservationRequest.getStartTime(), reservationRequest.getEndTime()))) {
            throw new BadRequestException("meeting room is booked in these range");
        } else if ((reservation != null && !canUpdateDateTime(Objects.requireNonNull(reservation), meetingRoom, reservationRequest.getStartTime(), reservationRequest.getEndTime()))) {
            throw new BadRequestException("meeting room is booked in these range can't update");
        }

        Reservation reservationMapperEntity = reservationMapper.toEntity(reservationRequest, user, meetingRoom);
        if (reservation != null)
            reservationMapperEntity.setReservationId(reservation.getReservationId());
        return reservationMapper.toResponse(reservationRepository.save(reservationMapperEntity));
    }
}
