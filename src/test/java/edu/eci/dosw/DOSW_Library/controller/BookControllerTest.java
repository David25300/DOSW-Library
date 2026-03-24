package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Test
    void shouldAddBook() {
        BookService bookService = mock(BookService.class);
        BookController controller = new BookController(bookService);

        Book book = new Book("B001", "Clean Code", "Robert C. Martin", true);

        when(bookService.addBook(book, 2)).thenReturn(book);

        Book result = controller.addBook(book, 2);

        assertNotNull(result);
        assertEquals("B001", result.getId());
        verify(bookService).addBook(book, 2);
    }

    @Test
    void shouldGetAllBooks() {
        BookService bookService = mock(BookService.class);
        BookController controller = new BookController(bookService);

        List<Book> books = List.of(
                new Book("B001", "Clean Code", "Robert C. Martin", true),
                new Book("B002", "Refactoring", "Martin Fowler", true)
        );

        when(bookService.getAllBooks()).thenReturn(books);

        List<Book> result = controller.getAllBooks();

        assertEquals(2, result.size());
        verify(bookService).getAllBooks();
    }

    @Test
    void shouldGetBookById() {
        BookService bookService = mock(BookService.class);
        BookController controller = new BookController(bookService);

        Book book = new Book("B001", "Clean Code", "Robert C. Martin", true);

        when(bookService.getBookById("B001")).thenReturn(book);

        Book result = controller.getBookById("B001");

        assertNotNull(result);
        assertEquals("B001", result.getId());
        verify(bookService).getBookById("B001");
    }

    @Test
    void shouldUpdateAvailability() {
        BookService bookService = mock(BookService.class);
        BookController controller = new BookController(bookService);

        controller.updateAvailability("B001", false);

        verify(bookService).updateAvailability("B001", false);
    }
}