package org.orange.oie.internship2025.conferenceroombooking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.exceptions.ApiException;
import org.orange.oie.internship2025.conferenceroombooking.repository.UserRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.impl.UserDetailsServiceImplementation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setFirstName("Laila");
        testUser.setLastName("Mohamed");
        testUser.setPhone("01052756770");
        testUser.setEmail("laila.mohamed@orange.com");
        testUser.setPassword("$2y$10$lmn456hashedpassword");
    }

    @Test
    void whenLoadUserByUsernameFoundThenReturnUserDetailsNotNull() {
        // Given
        Optional<User> userOptional = Optional.of(testUser);
        when(userRepository.findByEmail("laila.mohamed@orange.com")).thenReturn(userOptional);

        // When
        UserDetails userDetails = userDetailsServiceImplementation.loadUserByUsername("laila.mohamed@orange.com");

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("laila.mohamed@orange.com");
        assertThat(userDetails.getPassword()).isEqualTo("$2y$10$lmn456hashedpassword");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void whenLoadUserByUsernameNotFoundThenThrowUsernameNotFoundException() {
        when(userRepository.findByEmail("nonexistent@orange.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsServiceImplementation.loadUserByUsername("nonexistent@orange.com"))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void whenGetCurrentUserReturnUserWhenSuccess() throws Exception {
        //Given
        when(userRepository.findByEmail("laila.mohamed@orange.com")).thenReturn(Optional.of(testUser));
        try (MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic
                     = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("laila.mohamed@orange.com");
            //When
            User user = userDetailsServiceImplementation.getCurrentUser();
            //Then
            assertEquals(testUser.getUserId(), user.getUserId());
        }
    }

    @Test
    void whenGetCurrentUserShouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        //Given
        when(userRepository.findByEmail("test@orange.com")).thenReturn(Optional.empty());
        try (MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic
                     = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("test@orange.com");
            //When & Then
            assertThrows(ApiException.class, () -> {
                userDetailsServiceImplementation.getCurrentUser();
            });
        }
    }
}
