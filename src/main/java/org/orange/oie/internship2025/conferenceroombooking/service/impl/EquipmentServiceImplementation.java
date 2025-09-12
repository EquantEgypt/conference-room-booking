package org.orange.oie.internship2025.conferenceroombooking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.orange.oie.internship2025.conferenceroombooking.dto.EquipmentDTO;
import org.orange.oie.internship2025.conferenceroombooking.repository.EquipmentRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.interfac.EquipmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentServiceImplementation implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ObjectMapper objectMapper;

    public EquipmentServiceImplementation(EquipmentRepository equipmentRepository, ObjectMapper objectMapper) {
        this.equipmentRepository = equipmentRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<EquipmentDTO> getEquipment() {
        return equipmentRepository.findAll()
                .stream().map(equipment ->
                        objectMapper.convertValue(equipment, EquipmentDTO.class)).toList();
    }
}
