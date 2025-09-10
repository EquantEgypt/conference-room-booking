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
        User user = userDetailsServiceImplementation.getCurrentUser();

        MeetingRoom meetingRoom = meetingRoomRepository.findById(
                reservationRequest.getRoomId()).orElseThrow(() -> new BadRequestException("MeetingRoom is not Found"));

        if (meetingRoom.getStatus() == MeetingRoomStatus.UNDER_MAINTENANCE) {
            throw new BadRequestException("Room is UNDER_MAINTENANCE");
        }

        if ((meetingRoom.getRoomType() == RoomType.NORMAL) && (reservationRequest.getType() == ReservationType.EXTERNAL)) {
            throw new BadRequestException("External meeting can not be normal rooms");
        }

        if (!isAvailable(meetingRoom, reservationRequest.getStartTime(), reservationRequest.getEndTime())) {
            throw new BadRequestException("meeting room is booked in these range");
        }
        Reservation reservation = reservationMapper.toEntity(reservationRequest, user, meetingRoom);
        return reservationMapper.toResponse(reservationRepository.save(reservation));
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

    private boolean isAvailable(MeetingRoom room, LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> reservationList = reservationRepository.findAllByRoomAndStartTimeBetween(room, startTime, endTime);
        if (!reservationList.isEmpty()) return false;
        reservationList = reservationRepository.findAllByRoomAndEndTimeBetween(room, startTime, endTime);
        return reservationList.isEmpty();
    }
}
