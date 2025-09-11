package org.orange.oie.internship2025.conferenceroombooking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orange.oie.internship2025.conferenceroombooking.dto.EquipmentDTO;
import org.orange.oie.internship2025.conferenceroombooking.entity.Equipment;
import org.orange.oie.internship2025.conferenceroombooking.repository.EquipmentRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.EquipmentServiceImplementation;
import org.orange.oie.internship2025.conferenceroombooking.service.interfac.EquipmentService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceImplementationTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EquipmentServiceImplementation equipmentService; // target to be tested

    private List<Equipment> equipmentList;
    private List<EquipmentDTO> equipmentDTOList;

    @BeforeEach
    void init() {
        // Equipment

        Equipment equipment1 = new Equipment();
        equipment1.setEquipment_id(1L);
        equipment1.setType("Air Conditioner");

        Equipment equipment2 = new Equipment();
        equipment2.setEquipment_id(2L);
        equipment2.setType("Microphone");

        equipmentList = new ArrayList<>(Arrays.asList(equipment1, equipment2));

        // EquipmentDTO

        EquipmentDTO equipmentDto1 = new EquipmentDTO();
        equipmentDto1.setEquipment_id(1L);
        equipmentDto1.setType("Air Conditioner");

        EquipmentDTO equipmentDto2 = new EquipmentDTO();
        equipmentDto2.setEquipment_id(2L);
        equipmentDto2.setType("Microphone");

        equipmentDTOList = new ArrayList<>(Arrays.asList(equipmentDto1, equipmentDto2));
    }

    @Test
    void getEquipment_shouldReturnAllEquipmentAsDTOs(){
        when(equipmentRepository.findAll()).thenReturn(equipmentList);

        when(objectMapper.convertValue(equipmentList.get(0), EquipmentDTO.class)).thenReturn(equipmentDTOList.get(0));
        when(objectMapper.convertValue(equipmentList.get(1), EquipmentDTO.class)).thenReturn(equipmentDTOList.get(1));

        List<EquipmentDTO> result = (equipmentService).getEquipment();

        assertEquals(2, result.size());
        assertEquals("Air Conditioner", result.get(0).getType());
        assertEquals("Microphone", result.get(1).getType());

    }
}