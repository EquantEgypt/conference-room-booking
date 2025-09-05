package org.orange.oie.internship2025.conferenceroombooking.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orange.oie.internship2025.conferenceroombooking.entity.User;
import org.orange.oie.internship2025.conferenceroombooking.repository.UserRepository;
import org.orange.oie.internship2025.conferenceroombooking.service.UserDetailsServiceImplementation;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

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
        when(userRepository.getUsersByEmail(anyString())).thenReturn(testUser);

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
        when(userRepository.getUsersByEmail(anyString())).thenReturn(null);

        assertThatThrownBy(() -> userDetailsServiceImplementation.loadUserByUsername("nonexistent@orange.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
