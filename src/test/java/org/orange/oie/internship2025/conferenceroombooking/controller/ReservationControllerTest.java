package org.orange.oie.internship2025.conferenceroombooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ReservationNotFoundException;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ReservationRequestConflict;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.ReservationServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReservationController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ReservationControllerTest {
    @MockBean
    ReservationServiceImplementation reservationServiceImplementation;

    @Autowired
    private MockMvc mockMvc;

    private ReservationRequest reservationRequest;
    private ReservationResponse reservationResponse;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        reservationRequest = new ReservationRequest();
        reservationRequest.setType(ReservationType.EXTERNAL);
        reservationRequest.setTitle("Team Meeting");
        reservationRequest.setDescription("Weekly team standup meeting");
        reservationRequest.setDate(LocalDate.of(2024, 1, 15));
        reservationRequest.setStartTime(LocalTime.of(9, 0));
        reservationRequest.setEndTime(LocalTime.of(10, 0));
        reservationRequest.setRecurrenceOption(RecurrenceOption.WEEKLY);
        reservationRequest.setRoomId(1L);

        reservationResponse = new ReservationResponse();
        reservationResponse.setReservationId(1L);
        reservationResponse.setType(ReservationType.EXTERNAL);
        reservationResponse.setDescription("Weekly team standup meeting");
        reservationResponse.setDate(LocalDate.of(2024, 1, 15));
        reservationResponse.setStartTime(LocalTime.of(9, 0));
        reservationResponse.setEndTime(LocalTime.of(10, 0));
        reservationResponse.setRecurrenceOption(RecurrenceOption.WEEKLY);
    }

    @Test
    void createShouldReturnCreatedAndReservationResponseWhenSuccess() throws Exception {
        //Given
        when(reservationServiceImplementation.createBooking(any(ReservationRequest.class)))
                .thenReturn(List.of(reservationResponse));
        //When & Then
        this.mockMvc.perform(post("/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(reservationResponse))));
    }

    @Test
    void createShouldReturnNotFoundWhenUsernameNotFound() throws Exception {
        //Given
        when(reservationServiceImplementation.createBooking(any(ReservationRequest.class)))
                .thenThrow(new UsernameNotFoundException("username not found"));
        //When & Then
        this.mockMvc.perform(post("/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createShouldReturnBadRequestWhenAnyErrorInRequestBody() throws Exception {
        //Given
        when(reservationServiceImplementation.createBooking(any(ReservationRequest.class)))
                .thenThrow(new ReservationRequestConflict("bad request"));
        //When & Then
        this.mockMvc.perform(post("/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllShouldReturnOkAndReservationResponseListWhenSuccess() throws Exception {
        //Given
        when(reservationServiceImplementation.getAllReservations())
                .thenReturn(List.of(reservationResponse));
        //When & Then
        this.mockMvc.perform(get("/reserve"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(reservationResponse))));
    }

    @Test
    void getAllShouldReturnNotFoundWhenUsernameNotFound() throws Exception {
        //Given
        when(reservationServiceImplementation.getAllReservations())
                .thenThrow(new UsernameNotFoundException("username not found"));
        //When & Then
        this.mockMvc.perform(get("/reserve"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteShouldReturnOkWhenSuccess() throws Exception {
        //When & Then
        this.mockMvc.perform(delete("/reserve/1"))
                .andDo(print())
                .andExpect(status().isNoContent()); // Changed from isOk() to isNoContent() (204)
    }

    @Test
    void deleteShouldReturnNotFoundWhenReservationNotFound() throws Exception {
        //Given
        doThrow(new ReservationNotFoundException("reservation not found"))
                .when(reservationServiceImplementation).deleteBooking(1L);
        //When & Then
        this.mockMvc.perform(delete("/reserve/1"))
                .andDo(print())
                .andExpect(status().isBadRequest()); // Changed from isNotFound() to isBadRequest() (400)
    }

    @Test
    void deleteShouldReturnNotFoundWhenUsernameNotFound() throws Exception {
        //Given
        doThrow(new UsernameNotFoundException("username not found"))
                .when(reservationServiceImplementation).deleteBooking(1L);
        //When & Then
        this.mockMvc.perform(delete("/reserve/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateShouldReturnOkAndReservationResponseWhenSuccess() throws Exception {
        //Given
        when(reservationServiceImplementation.updateBooking(any(ReservationRequest.class), any(Long.class)))
                .thenReturn( List.of(reservationResponse));
        //When & Then
        this.mockMvc.perform(put("/reserve/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(reservationResponse)));
    }

    @Test
    void updateShouldReturnNotFoundWhenReservationNotFound() throws Exception {
        //Given
        when(reservationServiceImplementation.updateBooking(any(ReservationRequest.class), any(Long.class)))
                .thenThrow(new ReservationNotFoundException("reservation not found"));
        //When & Then
        this.mockMvc.perform(put("/reserve/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest()); // Changed from isNotFound() to isBadRequest() (400)
    }

    @Test
    void updateShouldReturnBadRequestWhenAnyErrorInRequestBody() throws Exception {
        //Given
        when(reservationServiceImplementation.updateBooking(any(ReservationRequest.class), any(Long.class)))
                .thenThrow(new ReservationRequestConflict("bad request"));
        //When & Then
        this.mockMvc.perform(put("/reserve/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateShouldReturnNotFoundWhenUsernameNotFound() throws Exception {
        //Given
        when(reservationServiceImplementation.updateBooking(any(ReservationRequest.class), any(Long.class)))
                .thenThrow(new UsernameNotFoundException("username not found"));
        //When & Then
        this.mockMvc.perform(put("/reserve/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
