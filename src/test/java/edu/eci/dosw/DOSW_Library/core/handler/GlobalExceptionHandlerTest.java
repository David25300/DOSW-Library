package edu.eci.dosw.DOSW_Library.core.handler;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.LoanLimitExceededException;
import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleBookNotAvailableException() {
        ErrorResponse response = handler.handleBookNotAvailable(
                new BookNotAvailableException("El libro no está disponible")
        );

        assertEquals("El libro no está disponible", response.getError());
    }

    @Test
    void shouldHandleUserNotFoundException() {
        ErrorResponse response = handler.handleUserNotFound(
                new UserNotFoundException("Usuario no encontrado")
        );

        assertEquals("Usuario no encontrado", response.getError());
    }

    @Test
    void shouldHandleLoanLimitExceededException() {
        ErrorResponse response = handler.handleLoanLimitExceeded(
                new LoanLimitExceededException("Límite excedido")
        );

        assertEquals("Límite excedido", response.getError());
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        List<FieldError> fieldErrors = List.of(
                new FieldError("book", "id", "El ID del libro es obligatorio"),
                new FieldError("book", "title", "El título es obligatorio")
        );

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        Map<String, String> result = handler.handleMethodArgumentNotValid(ex);

        assertEquals("El ID del libro es obligatorio", result.get("id"));
        assertEquals("El título es obligatorio", result.get("title"));
    }

    @Test
    void shouldHandleConstraintViolationException() {
        ConstraintViolationException ex =
                new ConstraintViolationException("Error de validación", Set.of());

        ErrorResponse response = handler.handleConstraintViolation(ex);

        assertEquals("Error de validación", response.getError());
    }
}