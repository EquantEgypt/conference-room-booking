package org.orange.oie.internship2025.conferenceroombooking.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.orange.oie.internship2025.conferenceroombooking.controller.UserController;
import org.orange.oie.internship2025.conferenceroombooking.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginShouldReturnOkAndTokenWhenUsernameAndPasswordAreFound() throws Exception {
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
    void loginShouldReturnUnAuthorizedAndErrorMessageWhenUsernameAndPasswordAreNotFound() throws Exception {
        LoginRequest loginRequest = new LoginRequest("semaziz2003@yahoo.com", "password");
        Map<String, String> errorMessageMap = new HashMap<>();
        errorMessageMap.put("error", "invalid username or password");
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(objectMapper.writeValueAsString(errorMessageMap)));
    }

}
