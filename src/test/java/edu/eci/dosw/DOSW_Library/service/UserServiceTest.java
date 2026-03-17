package edu.eci.dosw.DOSW_Library.service;

import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void shouldRegisterUserAndGetUserById() {
        User user = new User("U001", "David");

        userService.registerUser(user);

        User result = userService.getUserById("U001");

        assertNotNull(result);
        assertEquals("U001", result.getId());
        assertEquals("David", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById("U999");
        });
    }
}