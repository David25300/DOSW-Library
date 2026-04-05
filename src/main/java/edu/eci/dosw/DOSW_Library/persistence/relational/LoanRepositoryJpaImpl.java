package edu.eci.dosw.DOSW_Library.persistence.relational;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.persistence.LoanRepository;
import edu.eci.dosw.DOSW_Library.persistence.relational.mapper.LoanEntityMapper;
import edu.eci.dosw.DOSW_Library.persistence.relational.repository.JpaLoanRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class LoanRepositoryJpaImpl implements LoanRepository {

    private final JpaLoanRepository repository;
    private final LoanEntityMapper mapper;

    public LoanRepositoryJpaImpl(JpaLoanRepository repository, LoanEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Loan save(Loan loan) {
        return mapper.toDomain(repository.save(mapper.toEntity(loan)));
    }

    @Override
    public Optional<Loan> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return repository.findByUserId(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByBookIdAndStatus(String bookId, String status) {
        return repository.existsByBookIdAndStatus(bookId, LoanStatus.valueOf(status));
    }

    @Override
    public boolean existsByUserIdAndStatus(String userId, String status) {
        return repository.existsByUserIdAndStatus(userId, LoanStatus.valueOf(status));
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByBookId(String bookId) {
        repository.deleteByBookId(bookId);
    }

    @Override
    @Transactional
    public void deleteByUserId(String userId) {
        repository.deleteByUserId(userId);
    }
}
