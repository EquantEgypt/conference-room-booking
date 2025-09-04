package org.orange.oie.internship2025.conferenceroombooking.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.orange.oie.internship2025.conferenceroombooking.configuration.SecurityConfiguration;
import org.orange.oie.internship2025.conferenceroombooking.controller.UserController;
import org.orange.oie.internship2025.conferenceroombooking.dto.LoginRequest;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.repository.UserRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private UserRepository userRepository;

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
    void loginShouldReturnUnAuthorizedAndErrorMessageWhenUsernameAndPasswordAreNotFound() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

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

    @Test
    void whenLoadUserByUsernameFoundThenReturnUserDetailsNotNull() {
        // Given
        User mockUser = new User();
        mockUser.setFirstName("Laila");
        mockUser.setLastName("Mohamed");
        mockUser.setPhone("01052756770");
        mockUser.setEmail("laila.mohamed@orange.com");
        mockUser.setPassword("$2y$10$lmn456hashedpassword");

        UserDetails expectedUserDetails = org.springframework.security.core.userdetails.User.builder()
                .username("laila.mohamed@orange.com")
                .password("$2y$10$lmn456hashedpassword")
                .authorities("USER")
                .build();

        // When
        when(userDetailsServiceImplementation.loadUserByUsername(anyString())).thenReturn(expectedUserDetails);
        UserDetails userDetails = userDetailsServiceImplementation.loadUserByUsername("laila.mohamed@orange.com");

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("laila.mohamed@orange.com");
    }
}