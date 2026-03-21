package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Validated
@Tag(name = "Books", description = "Operaciones relacionadas con libros")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Agregar un libro")
    @PostMapping
    public Book addBook(@Valid @RequestBody Book book,
                        @RequestParam @Min(value = 1, message = "La cantidad debe ser mayor que 0") int quantity) {
        return bookService.addBook(book, quantity);
    }

    @Operation(summary = "Obtener todos los libros")
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Obtener un libro por ID")
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable String id) {
        return bookService.getBookById(id);
    }

    @Operation(summary = "Actualizar disponibilidad de un libro")
    @PutMapping("/{id}/availability")
    public void updateAvailability(@PathVariable String id, @RequestParam boolean available) {
        bookService.updateAvailability(id, available);
    }
}