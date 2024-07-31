package ru.t1.HomeWork4T1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.t1.HomeWork4T1.model.User;
import ru.t1.HomeWork4T1.repository.UserRepository;
import ru.t1.HomeWork4T1.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        User user = new User();
        user.setUsername("testuser");

        // Perform the save operation
        userService.save(user);

        // Verify that save was called once with the correct user
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetByUsernameWhenUserExists() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username.toLowerCase())).thenReturn(Optional.of(user));

        User result = userService.getByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testGetByUsernameWhenUserDoesNotExist() {
        String username = "testuser";

        when(userRepository.findByUsername(username.toLowerCase())).thenReturn(Optional.empty());

        UsernameNotFoundException thrown = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.getByUsername(username),
                "Expected getByUsername() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Пользователь не найден"));
    }

    @Test
    void testFindByUserName() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username.toLowerCase())).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUserName(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
    }

    @Test
    void testUserDetailsService() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username.toLowerCase())).thenReturn(Optional.of(user));

        UserDetailsService userDetailsService = userService.userDetailsService();
        User result = (User) userDetailsService.loadUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testDeleteByIdWhenUserExistsAndIdNotOne() {
        Long id = 2L;
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.deleteById(id);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).deleteById(id);
    }


    @Test
    void testDeleteByIdWhenUserDoesNotExist() {
        Long id = 2L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = userService.deleteById(id);

        assertFalse(result.isPresent());
        verify(userRepository, never()).deleteById(id);
    }
}