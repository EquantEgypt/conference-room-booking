package org.orange.oie.internship2025.conferenceroombooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "equipment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true, length = 100)
    private String type;

    @ManyToMany(mappedBy = "equipmentList")
    private Set<MeetingRoom> meetingRooms = new HashSet<>();
}