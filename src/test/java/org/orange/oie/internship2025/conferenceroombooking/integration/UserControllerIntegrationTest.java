
package org.orange.oie.internship2025.conferenceroombooking.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.orange.oie.internship2025.conferenceroombooking.dto.LoginRequest;
import org.orange.oie.internship2025.conferenceroombooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;


    @Test
    public void whenLoginSuccessReturnOkAndToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("seif.ehab@orange.com", "password123");

        System.out.println("All users: ");
        userRepository.findAll().forEach(System.out::println);

        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }


    @Test
    public void whenLoginFailReturnUnAuthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest("wrong.email@orange.com", "password123");

        System.out.println("All users: ");
        userRepository.findAll().forEach(System.out::println);

        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenLoginWithWrongPasswordReturnUnAuthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest("seif.ehab@orange.com", "wrongpassword");
        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


}
