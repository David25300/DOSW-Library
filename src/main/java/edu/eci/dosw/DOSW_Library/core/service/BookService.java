package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.util.ApiMessages;
import edu.eci.dosw.DOSW_Library.persistence.entity.BookEntity;
import edu.eci.dosw.DOSW_Library.persistence.mapper.BookPersistenceMapper;
import edu.eci.dosw.DOSW_Library.persistence.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookPersistenceMapper bookPersistenceMapper;

    public BookService(BookRepository bookRepository,
                       BookPersistenceMapper bookPersistenceMapper) {
        this.bookRepository = bookRepository;
        this.bookPersistenceMapper = bookPersistenceMapper;
    }

    public Book addBook(Book book, int quantity) {
        book.setAvailable(quantity > 0);

        BookEntity entity = bookPersistenceMapper.toEntity(book);
        BookEntity savedEntity = bookRepository.save(entity);

        return bookPersistenceMapper.toDomain(savedEntity);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookPersistenceMapper::toDomain)
                .toList();
    }

    public Book getBookById(String id) {
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotAvailableException(ApiMessages.BOOK_NOT_AVAILABLE));
        return bookPersistenceMapper.toDomain(entity);
    }

    public void updateAvailability(String id, boolean available) {
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotAvailableException(ApiMessages.BOOK_NOT_AVAILABLE));

        entity.setAvailable(available);
        bookRepository.save(entity);
    }

    public boolean isBookAvailable(String id) {
        return bookRepository.findById(id)
                .map(BookEntity::isAvailable)
                .orElse(false);
    }

    public void borrowBook(String id) {
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotAvailableException(ApiMessages.BOOK_NOT_AVAILABLE));

        if (!entity.isAvailable()) {
            throw new BookNotAvailableException(ApiMessages.BOOK_NOT_AVAILABLE);
        }

        entity.setAvailable(false);
        bookRepository.save(entity);
    }

    public void returnBook(String id) {
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotAvailableException(ApiMessages.BOOK_NOT_AVAILABLE));

        entity.setAvailable(true);
        bookRepository.save(entity);
    }
}
