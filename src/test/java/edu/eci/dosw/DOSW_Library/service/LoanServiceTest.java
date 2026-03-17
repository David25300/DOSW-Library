package edu.eci.dosw.DOSW_Library.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import edu.eci.dosw.DOSW_Library.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanServiceTest {

    private BookService bookService;
    private UserService userService;
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        userService = new UserService();
        loanService = new LoanService(bookService, userService);
    }

    @Test
    void shouldCreateLoanSuccessfully() {
        Book book = new Book("B001", "Clean Code", "Robert C. Martin", true);
        User user = new User("U001", "David");

        bookService.addBook(book, 1);
        userService.registerUser(user);

        Loan loan = loanService.createLoan("B001", "U001");

        assertNotNull(loan);
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
        assertEquals("B001", loan.getBook().getId());
        assertEquals("U001", loan.getUser().getId());
    }

    @Test
    void shouldReturnLoanSuccessfully() {
        Book book = new Book("B001", "Clean Code", "Robert C. Martin", true);
        User user = new User("U001", "David");

        bookService.addBook(book, 1);
        userService.registerUser(user);

        Loan loan = loanService.createLoan("B001", "U001");
        Loan returnedLoan = loanService.returnLoan(loan.getId());

        assertEquals(LoanStatus.RETURNED, returnedLoan.getStatus());
        assertNotNull(returnedLoan.getReturnDate());
    }

    @Test
    void shouldThrowExceptionWhenBookIsNotAvailableForLoan() {
        Book book = new Book("B001", "Clean Code", "Robert C. Martin", true);
        User user = new User("U001", "David");

        bookService.addBook(book, 0);
        userService.registerUser(user);

        assertThrows(BookNotAvailableException.class, () -> {
            loanService.createLoan("B001", "U001");
        });
    }
}