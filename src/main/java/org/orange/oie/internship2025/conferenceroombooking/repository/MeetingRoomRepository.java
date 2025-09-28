package org.orange.oie.internship2025.conferenceroombooking.repository;

import org.orange.oie.internship2025.conferenceroombooking.entity.MeetingRoom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeetingRoomRepository extends CrudRepository<MeetingRoom, Long> {
    List<MeetingRoom> findAll();
}


