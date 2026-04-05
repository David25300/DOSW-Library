package edu.eci.dosw.DOSW_Library.persistence.nonrelational;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.persistence.LoanRepository;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.mapper.LoanDocumentMapper;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.repository.MongoLoanRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class LoanRepositoryMongoImpl implements LoanRepository {

    private final MongoLoanRepository repository;
    private final LoanDocumentMapper mapper;

    public LoanRepositoryMongoImpl(MongoLoanRepository repository, LoanDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Loan save(Loan loan) {
        return mapper.toDomain(repository.save(mapper.toDocument(loan)));
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
        return repository.existsByBookIdAndStatus(bookId, status);
    }

    @Override
    public boolean existsByUserIdAndStatus(String userId, String status) {
        return repository.existsByUserIdAndStatus(userId, status);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByBookId(String bookId) {
        repository.deleteByBookId(bookId);
    }

    @Override
    public void deleteByUserId(String userId) {
        repository.deleteByUserId(userId);
    }
}
