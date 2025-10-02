package org.orange.oie.internship2025.conferenceroombooking.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.orange.oie.internship2025.conferenceroombooking.controller.EquipmentController;
import org.orange.oie.internship2025.conferenceroombooking.dto.EquipmentDTO;
import org.orange.oie.internship2025.conferenceroombooking.service.interfac.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EquipmentController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class EquipmentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipmentService equipmentService;

    private List<EquipmentDTO> equipmentDTOList;

    @BeforeEach
    void init() {
        EquipmentDTO equipmentDto1 = new EquipmentDTO();
        equipmentDto1.setEquipmentId(1L);
        equipmentDto1.setType("Air Conditioner");

        EquipmentDTO equipmentDto2 = new EquipmentDTO();
        equipmentDto2.setEquipmentId(2L);
        equipmentDto2.setType("Microphone");

        equipmentDTOList = new ArrayList<>(Arrays.asList(equipmentDto1, equipmentDto2));
    }

    @Test
    void getEquipmentShouldReturnStatusOkAndEquipmentList() throws Exception {
        when(equipmentService.getEquipment()).thenReturn(equipmentDTOList);

        mockMvc.perform(get("/equipment"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(equipmentDTOList)));
    }

    @Test
    void getEquipmentShouldReturnEmptyListWhenNoEquipment() throws Exception {
        when(equipmentService.getEquipment()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/equipment"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getEquipmentShouldReturnStatusOkWhenServiceReturnsNull() throws Exception {
        when(equipmentService.getEquipment()).thenReturn(null);

        mockMvc.perform(get("/equipment"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

}