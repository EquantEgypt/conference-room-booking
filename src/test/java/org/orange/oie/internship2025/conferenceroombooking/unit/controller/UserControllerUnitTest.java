package org.orange.oie.internship2025.conferenceroombooking.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.orange.oie.internship2025.conferenceroombooking.configuration.SecurityConfiguration;
import org.orange.oie.internship2025.conferenceroombooking.controller.UserController;
import org.orange.oie.internship2025.conferenceroombooking.dto.LoginRequest;
import org.orange.oie.internship2025.conferenceroombooking.enums.ApiError;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ApiException;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
public class UserControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;


    @MockBean
    private UserDetailsServiceImplementation userDetailsServiceImplementation;


    @Test
    void loginShouldReturnOkAndTokenWhenUsernameAndPasswordAreFound() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("semaziz2004@yahoo.com", "password123"));

        LoginRequest loginRequest = new LoginRequest("semaziz2004@yahoo.com", "password123");
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", "c2VtYXppejIwMDRAeWFob28uY29tOnBhc3N3b3JkMTIz");

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tokenMap)));
    }

    @Test
    void loginShouldReturnUnBadRequestAndErrorMessageWhenUsernameAndPasswordAreNotFound() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new ApiException(ApiError.USERNAME_AND_PASSWORD_MISSING));

        LoginRequest loginRequest = new LoginRequest("semaziz2003@yahoo.com", "password");
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("httpStatus", "BAD_REQUEST");
        errorResponse.put("errorMessage", "Username and password must be provided");

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(errorResponse)));

    }

    @Test
    void loginShouldReturnUnauthorizedWhenUsernameIsCorrectButPasswordIsIncorrect() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new ApiException(ApiError.USERNAME_OR_PASSWORD_INVALID));

        LoginRequest loginRequest = new LoginRequest("semaziz2004@yahoo.com", "Incorrect password");
        Map<String, String> errorMessageMap = new HashMap<>();
        errorMessageMap.put("errorMessage", "Username or password is invalid");

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(objectMapper.writeValueAsString(errorMessageMap)));
    }

    @Test
    void loginShouldReturnUnauthorizedWhenUsernameIsIncorrectButPasswordIsCorrect() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new ApiException(ApiError.USERNAME_OR_PASSWORD_INVALID));


        LoginRequest loginRequest = new LoginRequest("incorrectEmail@example.com", "password123");
        Map<String, String> errorMessageMap = new HashMap<>();

        errorMessageMap.put("errorMessage", "Username or password is invalid");

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(objectMapper.writeValueAsString(errorMessageMap)));
    }

    @Test
    void loginShouldReturnBadRequestWhenUsernameIsEmpty() throws Exception {

        LoginRequest loginRequest = new LoginRequest("", "password123");
        Map<String, String> errorMessageMap = new HashMap<>();
        errorMessageMap.put("httpStatus", "BAD_REQUEST");
        errorMessageMap.put("errorMessage", "username: username is required");

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(errorMessageMap)));
    }

    @Test
    void loginShouldReturnUnAuthorizedWhenPasswordIsEmpty() throws Exception {

        LoginRequest loginRequest = new LoginRequest("semaziz2003@yahoo.com", "");
        Map<String, String> errorMessageMap = new HashMap<>();
        errorMessageMap.put("errorMessage", "password: password is required");


        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(errorMessageMap)));
    }


}