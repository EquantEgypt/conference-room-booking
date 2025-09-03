package org.orange.oie.internship2025.conferenceroombooking.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.orange.oie.internship2025.conferenceroombooking.configuration.SecurityConfiguration;
import org.orange.oie.internship2025.conferenceroombooking.controller.UserController;
import org.orange.oie.internship2025.conferenceroombooking.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testBasicAuth() throws Exception {
        LoginRequest loginRequest = new LoginRequest("seif", "123");
        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("seif"))
                .andExpect(jsonPath("$.password").value("123"));
    }
}
