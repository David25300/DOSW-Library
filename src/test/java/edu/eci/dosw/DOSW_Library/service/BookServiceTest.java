package edu.eci.dosw.DOSW_Library.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
    }

    @Test
    void shouldAddBookAndGetBookById() {
        Book book = new Book("B001", "Clean Code", "Robert C. Martin", true);

        bookService.addBook(book, 2);

        Book result = bookService.getBookById("B001");

        assertNotNull(result);
        assertEquals("B001", result.getId());
        assertEquals("Clean Code", result.getTitle());
    }

    @Test
    void shouldUpdateBookAvailability() {
        Book book = new Book("B001", "Clean Code", "Robert C. Martin", true);
        bookService.addBook(book, 2);

        bookService.updateAvailability("B001", false);

        Book result = bookService.getBookById("B001");
        assertFalse(result.isAvailable());
    }

    @Test
    void shouldThrowExceptionWhenBookIsNotAvailable() {
        Book book = new Book("B001", "Clean Code", "Robert C. Martin", true);
        bookService.addBook(book, 0);

        assertThrows(BookNotAvailableException.class, () -> {
            bookService.borrowBook("B001");
        });
    }
}