package edu.eci.dosw.DOSW_Library.persistence.relational;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.persistence.UserRepository;
import edu.eci.dosw.DOSW_Library.persistence.relational.mapper.UserEntityMapper;
import edu.eci.dosw.DOSW_Library.persistence.relational.repository.JpaUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class UserRepositoryJpaImpl implements UserRepository {

    private final JpaUserRepository repository;
    private final UserEntityMapper mapper;

    public UserRepositoryJpaImpl(JpaUserRepository repository, UserEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        return mapper.toDomain(repository.save(mapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
