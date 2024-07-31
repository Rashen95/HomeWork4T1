package ru.t1.HomeWork4T1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.t1.HomeWork4T1.dto.SignInRequest;
import ru.t1.HomeWork4T1.dto.SignUpRequest;
import ru.t1.HomeWork4T1.model.User;
import ru.t1.HomeWork4T1.model.Role;
import ru.t1.HomeWork4T1.security.JwtAuthenticationResponse;
import ru.t1.HomeWork4T1.service.AuthenticationService;
import ru.t1.HomeWork4T1.service.JwtService;
import ru.t1.HomeWork4T1.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp() {
        SignUpRequest request = new SignUpRequest();
        request.setFirstName("john");
        request.setLastName("doe");
        request.setUsername("johndoe");
        request.setPassword("password123");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        authenticationService.signUp(request);

        User expectedUser = User.builder()
                .username(request.getUsername().toLowerCase())
                .password("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .role(Role.ROLE_USER)
                .build();

        verify(userService, times(1)).save(expectedUser);
    }

    @Test
    void testSignIn() {
        SignInRequest request = new SignInRequest();
        request.setUsername("johndoe");
        request.setPassword("password123");

        UserDetails userDetails = User.builder()
                .username("johndoe")
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .build();

        String jwtToken = "jwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        when(userDetailsService.loadUserByUsername(request.getUsername().toLowerCase()))
                .thenReturn(userDetails);
        when(userService.userDetailsService()).thenReturn(userDetailsService);

        when(jwtService.generateToken(userDetails)).thenReturn(jwtToken);

        JwtAuthenticationResponse response = authenticationService.signIn(request);

        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername(request.getUsername().toLowerCase());
        verify(jwtService, times(1)).generateToken(userDetails);
    }
}