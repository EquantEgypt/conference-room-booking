package service;

import entity.User;
import enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDetailsServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImplementation userDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser() {
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("seif");
        user.setLastName("ehab");
        user.setEmail("seif.ehab@orange.com");
        user.setPassword("$2y$12$IcqyjC86VbvAUbeKk1QpEuyafXHTH0dhBOts.topLuRlrLIG40.sS");
        user.setRole(UserRole.EMPLOYEE);
        return user;
    }

    @Test
    public void testLoadUserByUsername_Success() {
        User user = createUser();
        when(userRepository.findByEmail("seif.ehab@orange.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("seif.ehab@orange.com");

        assertNotNull(userDetails);
        assertEquals("seif.ehab@orange.com", userDetails.getUsername());
        assertEquals("$2y$12$IcqyjC86VbvAUbeKk1QpEuyafXHTH0dhBOts.topLuRlrLIG40.sS", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_EMPLOYEE")));
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("unknown@orange.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("unknown@orange.com"));
    }
}




