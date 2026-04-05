package edu.eci.dosw.DOSW_Library.persistence.nonrelational.repository;

import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.LoanDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoLoanRepository extends MongoRepository<LoanDocument, String> {

    List<LoanDocument> findByUserId(String userId);

    List<LoanDocument> findByBookId(String bookId);

    boolean existsByBookIdAndStatus(String bookId, String status);

    boolean existsByUserIdAndStatus(String userId, String status);

    void deleteByBookId(String bookId);

    void deleteByUserId(String userId);
}
