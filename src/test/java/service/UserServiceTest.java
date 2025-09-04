package service;

import entity.User;
import enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser() {
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("Seif");
        user.setLastName("Ehab");
        user.setEmail("seif.ehab@orange.com");
        user.setPassword("$2y$12$IcqyjC86VbvAUbeKk1QpEuyafXHTH0dhBOts.topLuRlrLIG40.sS");
        user.setRole(UserRole.EMPLOYEE);
        return user;
    }

    @Test
    public void testLogin_Success() {
        User user = createUser();
        when(userRepository.findByEmail("seif.ehab@orange.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "$2y$12$IcqyjC86VbvAUbeKk1QpEuyafXHTH0dhBOts.topLuRlrLIG40.sS")).thenReturn(true);

        boolean result = userService.login("seif.ehab@orange.com", "password123");
        assertTrue(result);
    }

    @Test
    public void testLogin_Failure_WrongPassword() {
        User user = createUser();
        when(userRepository.findByEmail("seif.ehab@orange.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "$2y$12$IcqyjC86VbvAUbeKk1QpEuyafXHTH0dhBOts.topLuRlrLIG40.sS")).thenReturn(false);

        boolean result = userService.login("seif.ehab@orange.com", "wrongpass");
        assertFalse(result);
    }

    @Test
    public void testLogin_Failure_UserNotFound() {
        when(userRepository.findByEmail("unknown@orange.com")).thenReturn(Optional.empty());
        boolean result = userService.login("unknown@orange.com", "password123");
        assertFalse(result);
    }

    @Test
    public void testLogin_Failure_EmptyPassword() {
        User user = createUser();
        when(userRepository.findByEmail("seif.ehab@orange.com")).thenReturn(Optional.of(user));
        boolean result = userService.login("seif.ehab@orange.com", "");
        assertFalse(result);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    public void testLogin_Failure_EmptyEmail() {
        boolean result = userService.login("", "password123");
        assertFalse(result);
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    public void testLogin_Failure_NullValues() {
        boolean result = userService.login(null, null);
        assertFalse(result);
        verify(userRepository, never()).findByEmail(anyString());
    }
}
