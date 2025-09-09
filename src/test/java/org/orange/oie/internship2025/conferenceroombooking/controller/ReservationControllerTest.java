package org.orange.oie.internship2025.conferenceroombooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationRequest;
import org.orange.oie.internship2025.conferenceroombooking.dto.ReservationResponse;
import org.orange.oie.internship2025.conferenceroombooking.enums.RecurrenceOption;
import org.orange.oie.internship2025.conferenceroombooking.enums.ReservationType;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.ReservationServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        reservationRequest.setDescription("Weekly team standup meeting");
        reservationRequest.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        reservationRequest.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        reservationRequest.setRecurrenceOption(RecurrenceOption.WEEKLY);
        reservationRequest.setRoomId(1L);

        reservationResponse = new ReservationResponse();
        reservationResponse.setReservationId(1L);
        reservationResponse.setType(ReservationType.EXTERNAL);
        reservationResponse.setDescription("Weekly team standup meeting");
        reservationResponse.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        reservationResponse.setEndTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        reservationResponse.setRecurrenceOption(RecurrenceOption.WEEKLY);
    }

    @Test
    void createShouldReturnOkAndReservationResponseWhenSuccess() throws Exception {
        //Given
        when(reservationServiceImplementation.createBooking(any(ReservationRequest.class)))
                .thenReturn(reservationResponse);
        //When & Then
        this.mockMvc.perform(post("/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(reservationResponse)));
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
                .andExpect(status().isNotFound());
    }

    @Test
    void createShouldReturnBadRequestWhenAnyErrorInRequestBody() throws Exception {
        //Given
        when(reservationServiceImplementation.createBooking(any(ReservationRequest.class)))
                .thenThrow(new BadRequestException("bad request"));
        //When & Then
        this.mockMvc.perform(post("/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
