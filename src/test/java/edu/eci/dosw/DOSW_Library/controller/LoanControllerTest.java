package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanControllerTest {

    @Test
    void shouldCreateLoan() {
        LoanService loanService = mock(LoanService.class);
        LoanController controller = new LoanController(loanService);

        Loan loan = new Loan(
                "L001",
                new Book("B001", "Clean Code", "Robert C. Martin", true),
                new User("U001", "David"),
                LocalDate.now(),
                LoanStatus.ACTIVE,
                null
        );

        when(loanService.createLoan("B001", "U001")).thenReturn(loan);

        Loan result = controller.createLoan("B001", "U001");

        assertNotNull(result);
        assertEquals("L001", result.getId());
        verify(loanService).createLoan("B001", "U001");
    }

    @Test
    void shouldReturnLoan() {
        LoanService loanService = mock(LoanService.class);
        LoanController controller = new LoanController(loanService);

        Loan loan = new Loan(
                "L001",
                new Book("B001", "Clean Code", "Robert C. Martin", true),
                new User("U001", "David"),
                LocalDate.now(),
                LoanStatus.RETURNED,
                LocalDate.now()
        );

        when(loanService.returnLoan("L001")).thenReturn(loan);

        Loan result = controller.returnLoan("L001");

        assertNotNull(result);
        assertEquals(LoanStatus.RETURNED, result.getStatus());
        verify(loanService).returnLoan("L001");
    }

    @Test
    void shouldGetAllLoans() {
        LoanService loanService = mock(LoanService.class);
        LoanController controller = new LoanController(loanService);

        List<Loan> loans = List.of(
                new Loan("L001", null, null, LocalDate.now(), LoanStatus.ACTIVE, null)
        );

        when(loanService.getAllLoans()).thenReturn(loans);

        List<Loan> result = controller.getAllLoans();

        assertEquals(1, result.size());
        verify(loanService).getAllLoans();
    }
}