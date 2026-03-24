package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Test
    void shouldRegisterUser() {
        UserService userService = mock(UserService.class);
        UserController controller = new UserController(userService);

        User user = new User("U001", "David");

        when(userService.registerUser(user)).thenReturn(user);

        User result = controller.registerUser(user);

        assertNotNull(result);
        assertEquals("U001", result.getId());
        assertEquals("David", result.getName());
        verify(userService).registerUser(user);
    }

    @Test
    void shouldGetAllUsers() {
        UserService userService = mock(UserService.class);
        UserController controller = new UserController(userService);

        List<User> users = List.of(
                new User("U001", "David"),
                new User("U002", "Juan")
        );

        when(userService.getAllUsers()).thenReturn(users);

        List<User> result = controller.getAllUsers();

        assertEquals(2, result.size());
        verify(userService).getAllUsers();
    }

    @Test
    void shouldGetUserById() {
        UserService userService = mock(UserService.class);
        UserController controller = new UserController(userService);

        User user = new User("U001", "David");

        when(userService.getUserById("U001")).thenReturn(user);

        User result = controller.getUserById("U001");

        assertNotNull(result);
        assertEquals("U001", result.getId());
        assertEquals("David", result.getName());
        verify(userService).getUserById("U001");
    }
}