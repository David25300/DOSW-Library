package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.controller.dto.BookDTO;
import edu.eci.dosw.DOSW_Library.controller.mapper.BookMapper;
import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Validated
@Tag(name = "Books", description = "Operaciones relacionadas con libros")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @Operation(summary = "Agregar un libro con stock inicial (solo LIBRARIAN)")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookDTO) {
        Book book = bookMapper.toDomain(bookDTO);
        Book savedBook = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.toDTO(savedBook));
    }

    @Operation(summary = "Obtener todos los libros (cualquier usuario autenticado)")
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN')")
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks()
                .stream()
                .map(bookMapper::toDTO)
                .toList();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Obtener un libro por ID (cualquier usuario autenticado)")
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN')")
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable String id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(bookMapper.toDTO(book));
    }

    @Operation(summary = "Actualizar stock de un libro (solo LIBRARIAN)")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}/stock")
    public ResponseEntity<BookDTO> updateStock(@PathVariable String id,
                                               @RequestParam int totalStock,
                                               @RequestParam int availableCopies) {
        Book updated = bookService.updateStock(id, totalStock, availableCopies);
        return ResponseEntity.ok(bookMapper.toDTO(updated));
    }

    @Operation(summary = "Eliminar un libro (solo LIBRARIAN)")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
