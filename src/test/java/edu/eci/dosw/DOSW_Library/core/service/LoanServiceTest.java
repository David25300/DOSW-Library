package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.model.Role;
import edu.eci.dosw.DOSW_Library.persistence.BookRepository;
import edu.eci.dosw.DOSW_Library.persistence.LoanRepository;
import edu.eci.dosw.DOSW_Library.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanService loanService;

    private User testUser;
    private Book testBook;
    private Loan testLoan;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user-001");
        testUser.setName("Juan Pérez");
        testUser.setUsername("juan");
        testUser.setRole(Role.USER);

        testBook = new Book();
        testBook.setId("book-001");
        testBook.setTitle("Clean Code");
        testBook.setAuthor("Robert C. Martin");
        testBook.setTotalStock(5);
        testBook.setAvailableCopies(5);

        testLoan = new Loan();
        testLoan.setId("loan-001");
        testLoan.setBook(testBook);
        testLoan.setUser(testUser);
        testLoan.setLoanDate(LocalDate.now());
        testLoan.setStatus(LoanStatus.ACTIVE);
        testLoan.setHistory(new ArrayList<>());
    }

    @Test
    @DisplayName("Dado que tengo 1 reserva registrada, cuando lo consulto a nivel de servicio, entonces la consulta será exitosa validando el campo id")
    void givenOneLoan_whenFindAll_thenReturnsLoanWithCorrectId() {
        when(loanRepository.findAll()).thenReturn(List.of(testLoan));

        List<Loan> loans = loanService.getAllLoans();

        assertFalse(loans.isEmpty());
        assertEquals(1, loans.size());
        assertEquals("loan-001", loans.get(0).getId());
    }

    @Test
    @DisplayName("Dado que no hay ninguna reserva registrada, cuando la consulto a nivel de servicio, entonces la consulta no retorna ningún resultado")
    void givenNoLoans_whenFindAll_thenReturnsEmptyList() {
        when(loanRepository.findAll()).thenReturn(Collections.emptyList());

        List<Loan> loans = loanService.getAllLoans();

        assertTrue(loans.isEmpty());
        assertEquals(0, loans.size());
    }

    @Test
    @DisplayName("Dado que no hay ninguna reserva registrada, cuando la creo a nivel de servicio, entonces la creación será exitosa")
    void givenNoLoans_whenCreateLoan_thenCreationIsSuccessful() {
        when(userRepository.findById("user-001")).thenReturn(Optional.of(testUser));
        when(bookRepository.findById("book-001")).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan createdLoan = loanService.createLoan("book-001", "user-001");

        assertNotNull(createdLoan);
        assertNotNull(createdLoan.getId());
        assertEquals(LoanStatus.ACTIVE, createdLoan.getStatus());
        assertEquals(LocalDate.now(), createdLoan.getLoanDate());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    @DisplayName("Dado que tengo 1 reserva registrada, cuando la elimino a nivel de servicio, entonces la eliminación será exitosa")
    void givenOneLoan_whenDelete_thenDeletionIsSuccessful() {
        testLoan.setStatus(LoanStatus.RETURNED);
        testLoan.setReturnDate(LocalDate.now());

        when(loanRepository.findById("loan-001")).thenReturn(Optional.of(testLoan));
        doNothing().when(loanRepository).deleteById("loan-001");

        assertDoesNotThrow(() -> loanService.deleteLoan("loan-001"));
        verify(loanRepository, times(1)).deleteById("loan-001");
    }

    @Test
    @DisplayName("Dado que tengo 1 reserva registrada, cuando la elimino y consulto a nivel de servicio, entonces el resultado de la consulta no retorna ningún resultado")
    void givenOneLoan_whenDeleteAndFindAll_thenReturnsEmpty() {
        testLoan.setStatus(LoanStatus.RETURNED);
        testLoan.setReturnDate(LocalDate.now());

        when(loanRepository.findById("loan-001")).thenReturn(Optional.of(testLoan));
        doNothing().when(loanRepository).deleteById("loan-001");

        loanService.deleteLoan("loan-001");

        when(loanRepository.findAll()).thenReturn(Collections.emptyList());

        List<Loan> loans = loanService.getAllLoans();
        assertTrue(loans.isEmpty());
        assertEquals(0, loans.size());
    }
}
