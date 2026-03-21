package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.util.ApiMessages;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    private final Map<String, Book> books = new HashMap<>();
    private final Map<String, Integer> bookQuantities = new HashMap<>();

    public Book addBook(Book book, int quantity) {
        book.setAvailable(quantity > 0);
        books.put(book.getId(), book);
        bookQuantities.put(book.getId(), quantity);
        return book;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public Book getBookById(String id) {
        return books.get(id);
    }

    public void updateAvailability(String id, boolean available) {
        Book book = books.get(id);
        if (book != null) {
            book.setAvailable(available);
        }
    }

    public boolean isBookAvailable(String id) {
        Integer quantity = bookQuantities.get(id);
        return quantity != null && quantity > 0;
    }

    public void borrowBook(String id) {
        Integer quantity = bookQuantities.get(id);

        if (quantity == null || quantity <= 0) {
            throw new BookNotAvailableException(ApiMessages.BOOK_NOT_AVAILABLE);
        }

        quantity = quantity - 1;
        bookQuantities.put(id, quantity);

        Book book = books.get(id);
        if (book != null) {
            book.setAvailable(quantity > 0);
        }
    }

    public void returnBook(String id) {
        Integer quantity = bookQuantities.get(id);

        if (quantity == null) {
            quantity = 0;
        }

        quantity = quantity + 1;
        bookQuantities.put(id, quantity);

        Book book = books.get(id);
        if (book != null) {
            book.setAvailable(true);
        }
    }
}