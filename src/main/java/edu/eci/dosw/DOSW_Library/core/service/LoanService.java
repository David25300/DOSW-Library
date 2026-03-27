package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.core.util.ApiMessages;
import edu.eci.dosw.DOSW_Library.persistence.entity.BookEntity;
import edu.eci.dosw.DOSW_Library.persistence.entity.LoanEntity;
import edu.eci.dosw.DOSW_Library.persistence.entity.UserEntity;
import edu.eci.dosw.DOSW_Library.persistence.mapper.LoanPersistenceMapper;
import edu.eci.dosw.DOSW_Library.persistence.repository.BookRepository;
import edu.eci.dosw.DOSW_Library.persistence.repository.LoanRepository;
import edu.eci.dosw.DOSW_Library.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanPersistenceMapper loanPersistenceMapper;

    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository,
                       LoanPersistenceMapper loanPersistenceMapper) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanPersistenceMapper = loanPersistenceMapper;
    }

    @Transactional
    public Loan createLoan(String bookId, String userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotAvailableException(ApiMessages.BOOK_NOT_AVAILABLE));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException(ApiMessages.BOOK_NOT_AVAILABLE);
        }

        book.setAvailable(false);
        bookRepository.save(book);

        LoanEntity loan = new LoanEntity(
                UUID.randomUUID().toString(),
                book,
                user,
                LocalDate.now(),
                LoanStatus.ACTIVE,
                null
        );

        LoanEntity savedLoan = loanRepository.save(loan);
        return loanPersistenceMapper.toDomain(savedLoan);
    }

    @Transactional
    public Loan returnLoan(String loanId) {
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException(ApiMessages.LOAN_NOT_FOUND));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new RuntimeException("El préstamo ya fue devuelto");
        }

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());

        BookEntity book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        LoanEntity savedLoan = loanRepository.save(loan);
        return loanPersistenceMapper.toDomain(savedLoan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(loanPersistenceMapper::toDomain)
                .toList();
    }
}
