package org.orange.oie.internship2025.conferenceroombooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipment_id;


    @Column(nullable = false, unique = true, length = 100)
    private String type;


    @ManyToMany(mappedBy = "equipmentList" , cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<MeetingRoom> meetingRooms;
}