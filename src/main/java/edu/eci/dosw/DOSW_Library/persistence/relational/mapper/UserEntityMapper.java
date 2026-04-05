package edu.eci.dosw.DOSW_Library.persistence.relational.mapper;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.persistence.relational.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setRole(entity.getRole());
        return user;
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) return null;
        return new UserEntity(
                domain.getId(),
                domain.getName(),
                domain.getUsername(),
                domain.getPassword(),
                domain.getRole()
        );
    }
}