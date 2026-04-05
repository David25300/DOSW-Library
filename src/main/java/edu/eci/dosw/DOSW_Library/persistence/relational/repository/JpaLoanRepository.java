package edu.eci.dosw.DOSW_Library.persistence.relational.repository;

import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.persistence.relational.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaLoanRepository extends JpaRepository<LoanEntity, String> {

    List<LoanEntity> findByUserId(String userId);

    boolean existsByBookIdAndStatus(String bookId, LoanStatus status);

    boolean existsByUserIdAndStatus(String userId, LoanStatus status);

    void deleteByBookId(String bookId);

    void deleteByUserId(String userId);
}
