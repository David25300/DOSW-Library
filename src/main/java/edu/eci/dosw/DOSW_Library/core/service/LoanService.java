package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.util.ApiMessages;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LoanService {

    private final List<Loan> loans = new ArrayList<>();
    private final BookService bookService;
    private final UserService userService;

    public LoanService(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    public Loan createLoan(String bookId, String userId) {
        User user = userService.getUserById(userId);
        Book book = bookService.getBookById(bookId);

        bookService.borrowBook(bookId);

        Loan loan = new Loan(
                UUID.randomUUID().toString(),
                book,
                user,
                LocalDate.now(),
                LoanStatus.ACTIVE,
                null
        );

        loans.add(loan);
        return loan;
    }

    public Loan returnLoan(String loanId) {
        Loan loan = loans.stream()
                .filter(currentLoan -> currentLoan.getId().equals(loanId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(ApiMessages.LOAN_NOT_FOUND));

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());

        bookService.returnBook(loan.getBook().getId());

        return loan;
    }

    public List<Loan> getAllLoans() {
        return loans;
    }
}
