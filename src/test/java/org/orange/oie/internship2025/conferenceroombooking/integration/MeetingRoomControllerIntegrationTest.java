package org.orange.oie.internship2025.conferenceroombooking.integration;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MeetingRoomControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String USER = "seif.ehab@orange.com";
    private static final String PASS = "password123";

    @Test
    void getAvailableRooms_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/rooms")
                        .with(httpBasic(USER, PASS)))
                .andExpect(status().isOk());
    }

    @Test
    void getRoomById_shouldReturnOkOrNotFound() throws Exception {
        mockMvc.perform(get("/rooms/{id}", 1L)
                        .with(httpBasic(USER, PASS)))
                .andExpect(status().isOk());
    }

    @Test
    void getRooms_shouldReturnUnauthorizedWithoutAuth() throws Exception {
        mockMvc.perform(get("/rooms"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getRoomById_shouldReturnUnauthorizedWithoutAuth() throws Exception {
        mockMvc.perform(get("/rooms/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getRoomById_shouldReturnNotFoundForNonExistingId() throws Exception {
        mockMvc.perform(get("/rooms/{id}", 9999L)
                        .with(httpBasic(USER, PASS)))
                .andExpect(status().isNotFound());
    }


}
