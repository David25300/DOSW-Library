package edu.eci.dosw.DOSW_Library.persistence.mapper;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.persistence.entity.LoanEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanPersistenceMapper {

    private final BookPersistenceMapper bookPersistenceMapper;
    private final UserPersistenceMapper userPersistenceMapper;

    public LoanPersistenceMapper(BookPersistenceMapper bookPersistenceMapper, UserPersistenceMapper userPersistenceMapper) {
        this.bookPersistenceMapper = bookPersistenceMapper;
        this.userPersistenceMapper = userPersistenceMapper;
    }

    public Loan toDomain(LoanEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Loan(
                entity.getId(),
                bookPersistenceMapper.toDomain(entity.getBook()),
                userPersistenceMapper.toDomain(entity.getUser()),
                entity.getLoanDate(),
                entity.getStatus(),
                entity.getReturnDate()
        );
    }

    public LoanEntity toEntity(Loan domain) {
        if (domain == null) {
            return null;
        }
        return new LoanEntity(
                domain.getId(),
                bookPersistenceMapper.toEntity(domain.getBook()),
                userPersistenceMapper.toEntity(domain.getUser()),
                domain.getLoanDate(),
                domain.getStatus(),
                domain.getReturnDate()
        );
    }
}
