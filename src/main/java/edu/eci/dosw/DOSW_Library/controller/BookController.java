package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public Book addBook(@RequestBody Book book, @RequestParam int quantity) {
        return bookService.addBook(book, quantity);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable String id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}/availability")
    public void updateAvailability(@PathVariable String id, @RequestParam boolean available) {
        bookService.updateAvailability(id, available);
    }
}