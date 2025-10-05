package org.orange.oie.internship2025.conferenceroombooking.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.orange.oie.internship2025.conferenceroombooking.dto.EquipmentDTO;
import org.orange.oie.internship2025.conferenceroombooking.entity.Department;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.enums.UserRole;
import org.orange.oie.internship2025.conferenceroombooking.repository.UserRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.interfac.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EquipmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String USER = "seif.ehab@orange.com";
    private static final String PASS = "password123";


   @Test
    void getEquipments_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/equipment")
                        .with(httpBasic(USER, PASS)))
                .andExpect(status().isOk());
    }

    @Test
    void getEquipments_shouldReturnListOfEquipments() throws Exception {
        mockMvc.perform(get("/equipment")
                        .with(httpBasic(USER, PASS)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getEquipments_shouldReturnUnauthorizedWithoutAuth() throws Exception {
        mockMvc.perform(get("/equipment"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getEquipmentById_shouldReturnOkOrNotFound() throws Exception {
        mockMvc.perform(get("/equipment/{id}", 1L)
                        .with(httpBasic(USER, PASS)))
                .andExpect(status().isNotFound());
    }

}