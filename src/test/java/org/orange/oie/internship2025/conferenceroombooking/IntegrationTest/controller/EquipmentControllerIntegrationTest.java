package org.orange.oie.internship2025.conferenceroombooking.IntegrationTest.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.orange.oie.internship2025.conferenceroombooking.entity.Equipment;
import org.orange.oie.internship2025.conferenceroombooking.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EquipmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EquipmentRepository equipmentRepository;


    @BeforeEach
    public void setup() {
        equipmentRepository.deleteAll();

        equipmentRepository.saveAll(
                List.of(
                        new Equipment(null, "Projector",null),
                        new Equipment(null, "Whiteboard",null)
                )
        );
    }


    @WithMockUser(username = "testuser", roles = {"USER"})
    @Test
    public void testGetEquipment() throws Exception {
            mockMvc.perform(get("/equipment")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$",hasSize(2)))
                    .andExpect(jsonPath("$[0].type",is("Projector")))
                    .andExpect(jsonPath("$[1].type",is("Whiteboard")));



    }
}
