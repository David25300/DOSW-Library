package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.util.ApiMessages;
import edu.eci.dosw.DOSW_Library.persistence.LoanRepository;
import edu.eci.dosw.DOSW_Library.persistence.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;

    public UserService(UserRepository userRepository, LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));
    }

    @Transactional
    public void deleteUser(String id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));
        if (loanRepository.existsByUserIdAndStatus(id, "ACTIVE")) {
            throw new IllegalStateException(ApiMessages.USER_HAS_ACTIVE_LOANS);
        }
        loanRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}
