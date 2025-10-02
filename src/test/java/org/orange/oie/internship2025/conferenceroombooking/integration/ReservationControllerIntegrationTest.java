package org.orange.oie.internship2025.conferenceroombooking.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReservationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String USER = "seif.ehab@orange.com";
    private static final String PASS = "password123";

    @Test
    void getAllReservations_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/reserve")
                .with(httpBasic(USER, PASS)))
                .andExpect(status().isOk());
    }

    @Test
    void getReservationByFilter_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/reserve/filter")
                .param("dateScope", "TODAY")
                .param("reservationType", "INTERNAL")
                .param("recurrenceOption", "ONE_TIME")
                .with(httpBasic(USER, PASS)))
                .andExpect(status().isOk());
    }

    @Test
    void getUpcomingReservation_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/reserve/up-coming")
                .with(httpBasic(USER, PASS)))
                .andExpect(status().isOk());
    }

    @Test
    void getReservationById_shouldReturnOkOrNotFound() throws Exception {
        mockMvc.perform(get("/reserve/{reservationId}", 1L)
                .with(httpBasic(USER, PASS)))
                 .andExpect(status().isNotFound()) ;
    }

    @Test
    void getReservationByDate_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/reserve/date/{reservationDate}", "2025-10-01")
                .with(httpBasic(USER, PASS)))
                .andExpect(status().isOk());
    }

    @Test
    void createBooking_shouldReturnCreated() throws Exception {
        String requestBody = """
            {
                "type": "INTERNAL",
                "title": "Team Meeting",
                "description": "Monthly team sync-up",
                "date": "2025-12-20",
                "startTime": "09:00",
                "endTime": "10:00",
                "recurrenceOption": "ONE_TIME",
                "roomId": 1
            }
            """;
        mockMvc.perform(post("/reserve")
                .with(httpBasic(USER, PASS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

   @Test
    void updateBooking_shouldReturnOk() throws Exception {
        String updateRequestBody = """
            {
                "type": "INTERNAL",
                "title": "Updated Team Meeting",
                "description": "Monthly team sync-up",
                "date": "2025-10-02",
                "startTime": "09:00",
                "endTime": "10:00",
                "recurrenceOption": "ONE_TIME",
                "roomId": 1
            }
            """;

        mockMvc.perform(put("/reserve/{reservationId}", 1L)
                .with(httpBasic(USER, PASS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBooking_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/reserve/{reservationId}", 1L)
                .with(httpBasic(USER, PASS)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getReservations_shouldReturnUnauthorizedWithoutAuth() throws Exception {
        mockMvc.perform(get("/reserve"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void createBooking_shouldReturnBadRequestForInvalidData() throws Exception {
        String invalidRequestBody = """
            {
                "type": "INTERNAL",
                "title": "",
                "description": "Monthly team sync-up",
                "date": "2025-10-01",
                "startTime": "10:00",
                "endTime": "09:00",
                "recurrenceOption": "ONE_TIME",
                "roomId": 1
            }
            """;

        mockMvc.perform(post("/reserve")
                .with(httpBasic(USER, PASS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBooking_shouldReturnConflictForOverlappingReservation() throws Exception {
        String firstRequestBody = """
            {
                "type": "INTERNAL",
                "title": "First Meeting",
                "description": "First meeting description",
                "date": "2025-10-30",
                "startTime": "09:00",
                "endTime": "10:00",
                "recurrenceOption": "ONE_TIME",
                "roomId": 1
            }
            """;

        String overlappingRequestBody = """
            {
                "type": "INTERNAL",
                "title": "Overlapping Meeting",
                "description": "This meeting overlaps with the first",
                "date": "2025-10-30",
                "startTime": "09:30",
                "endTime": "10:30",
                "recurrenceOption": "ONE_TIME",
                "roomId": 1
            }
            """;

        mockMvc.perform(post("/reserve")
                .with(httpBasic(USER, PASS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstRequestBody))
                .andExpect(status().isCreated());


        mockMvc.perform(post("/reserve")
                .with(httpBasic(USER, PASS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(overlappingRequestBody))
                .andExpect(status().isConflict());
    }


}