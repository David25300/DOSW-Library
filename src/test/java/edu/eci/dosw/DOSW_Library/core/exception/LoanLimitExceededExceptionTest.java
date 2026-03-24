package edu.eci.dosw.DOSW_Library.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanLimitExceededExceptionTest {

    @Test
    void shouldCreateLoanLimitExceededExceptionWithMessage() {
        LoanLimitExceededException exception =
                new LoanLimitExceededException("Límite de préstamos excedido");

        assertEquals("Límite de préstamos excedido", exception.getMessage());
    }
}