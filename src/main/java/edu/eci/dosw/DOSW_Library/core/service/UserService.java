package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    public User registerUser(User user) {
        users.add(user);
        return user;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
    }
}