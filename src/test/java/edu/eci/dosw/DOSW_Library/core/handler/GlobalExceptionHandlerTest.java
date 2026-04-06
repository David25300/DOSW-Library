package edu.eci.dosw.DOSW_Library.core.handler;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("BookNotAvailableException retorna 404")
    void handleBookNotAvailable() {
        ResponseEntity<ErrorResponse> response = handler.handleBookNotAvailable(
                new BookNotAvailableException("Libro no encontrado"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Libro no encontrado", response.getBody().getMessage());
    }

    @Test
    @DisplayName("UserNotFoundException retorna 404")
    void handleUserNotFound() {
        ResponseEntity<ErrorResponse> response = handler.handleUserNotFound(
                new UserNotFoundException("Usuario no encontrado"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    @DisplayName("IllegalArgumentException retorna 400")
    void handleIllegalArgument() {
        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgument(
                new IllegalArgumentException("Argumento inválido"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
    }

    @Test
    @DisplayName("IllegalStateException retorna 409")
    void handleIllegalState() {
        ResponseEntity<ErrorResponse> response = handler.handleIllegalState(
                new IllegalStateException("Estado inválido"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().getStatus());
    }

    @Test
    @DisplayName("AccessDeniedException retorna 403")
    void handleAccessDenied() {
        ResponseEntity<ErrorResponse> response = handler.handleAccessDenied(
                new AccessDeniedException("Sin permisos"));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(403, response.getBody().getStatus());
    }

    @Test
    @DisplayName("RuntimeException retorna 500")
    void handleRuntime() {
        ResponseEntity<ErrorResponse> response = handler.handleRuntime(
                new RuntimeException("Error interno"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getStatus());
    }

    // --- ErrorResponse ---

    @Test
    @DisplayName("ErrorResponse constructor y getters")
    void errorResponse_constructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse error = new ErrorResponse(404, "Not found", now);

        assertEquals(404, error.getStatus());
        assertEquals("Not found", error.getMessage());
        assertEquals(now, error.getTimestamp());
    }

    @Test
    @DisplayName("ErrorResponse setters")
    void errorResponse_setters() {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(500);
        error.setMessage("Error");
        error.setTimestamp(LocalDateTime.now());

        assertEquals(500, error.getStatus());
        assertEquals("Error", error.getMessage());
        assertNotNull(error.getTimestamp());
    }
}
