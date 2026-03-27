package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.util.ApiMessages;
import edu.eci.dosw.DOSW_Library.persistence.entity.UserEntity;
import edu.eci.dosw.DOSW_Library.persistence.mapper.UserPersistenceMapper;
import edu.eci.dosw.DOSW_Library.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    public UserService(UserRepository userRepository,
                       UserPersistenceMapper userPersistenceMapper) {
        this.userRepository = userRepository;
        this.userPersistenceMapper = userPersistenceMapper;
    }

    public User registerUser(User user) {
        UserEntity entity = userPersistenceMapper.toEntity(user);
        UserEntity savedEntity = userRepository.save(entity);
        return userPersistenceMapper.toDomain(savedEntity);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userPersistenceMapper::toDomain)
                .toList();
    }

    public User getUserById(String id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));
        return userPersistenceMapper.toDomain(entity);
    }
}
