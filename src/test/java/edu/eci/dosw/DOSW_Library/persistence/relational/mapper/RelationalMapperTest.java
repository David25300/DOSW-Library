package edu.eci.dosw.DOSW_Library.persistence.relational.mapper;

import edu.eci.dosw.DOSW_Library.core.model.*;
import edu.eci.dosw.DOSW_Library.persistence.relational.entity.BookEntity;
import edu.eci.dosw.DOSW_Library.persistence.relational.entity.LoanEntity;
import edu.eci.dosw.DOSW_Library.persistence.relational.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RelationalMapperTest {

    private final BookEntityMapper bookMapper = new BookEntityMapper();
    private final UserEntityMapper userMapper = new UserEntityMapper();
    private final LoanEntityMapper loanMapper = new LoanEntityMapper(bookMapper, userMapper);

    // --- BookEntityMapper ---

    @Test
    @DisplayName("BookEntityMapper toDomain")
    void bookMapper_toDomain() {
        BookEntity entity = new BookEntity("b1", "Title", "Author", 5, 3);
        Book book = bookMapper.toDomain(entity);
        assertEquals("b1", book.getId());
        assertEquals("Title", book.getTitle());
        assertEquals("Author", book.getAuthor());
        assertEquals(5, book.getTotalStock());
        assertEquals(3, book.getAvailableCopies());
    }

    @Test
    @DisplayName("BookEntityMapper toDomain null")
    void bookMapper_toDomain_null() {
        assertNull(bookMapper.toDomain(null));
    }

    @Test
    @DisplayName("BookEntityMapper toEntity")
    void bookMapper_toEntity() {
        Book book = new Book();
        book.setId("b1");
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setTotalStock(5);
        book.setAvailableCopies(3);
        BookEntity entity = bookMapper.toEntity(book);
        assertEquals("b1", entity.getId());
        assertEquals(5, entity.getTotalStock());
    }

    @Test
    @DisplayName("BookEntityMapper toEntity null")
    void bookMapper_toEntity_null() {
        assertNull(bookMapper.toEntity(null));
    }

    // --- UserEntityMapper ---

    @Test
    @DisplayName("UserEntityMapper toDomain")
    void userMapper_toDomain() {
        UserEntity entity = new UserEntity("u1", "Name", "user", "pass", Role.USER);
        User user = userMapper.toDomain(entity);
        assertEquals("u1", user.getId());
        assertEquals("Name", user.getName());
        assertEquals("user", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    @DisplayName("UserEntityMapper toDomain null")
    void userMapper_toDomain_null() {
        assertNull(userMapper.toDomain(null));
    }

    @Test
    @DisplayName("UserEntityMapper toEntity")
    void userMapper_toEntity() {
        User user = new User();
        user.setId("u1");
        user.setName("Name");
        user.setUsername("user");
        user.setPassword("pass");
        user.setRole(Role.LIBRARIAN);
        UserEntity entity = userMapper.toEntity(user);
        assertEquals("u1", entity.getId());
        assertEquals(Role.LIBRARIAN, entity.getRole());
    }

    @Test
    @DisplayName("UserEntityMapper toEntity null")
    void userMapper_toEntity_null() {
        assertNull(userMapper.toEntity(null));
    }

    // --- LoanEntityMapper ---

    @Test
    @DisplayName("LoanEntityMapper toDomain")
    void loanMapper_toDomain() {
        BookEntity bookEntity = new BookEntity("b1", "Title", "Author", 5, 3);
        UserEntity userEntity = new UserEntity("u1", "Name", "user", "pass", Role.USER);
        LoanEntity entity = new LoanEntity("l1", bookEntity, userEntity, LocalDate.now(), LoanStatus.ACTIVE, null);

        Loan loan = loanMapper.toDomain(entity);
        assertEquals("l1", loan.getId());
        assertEquals("b1", loan.getBook().getId());
        assertEquals("u1", loan.getUser().getId());
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
    }

    @Test
    @DisplayName("LoanEntityMapper toDomain null")
    void loanMapper_toDomain_null() {
        assertNull(loanMapper.toDomain(null));
    }

    @Test
    @DisplayName("LoanEntityMapper toEntity")
    void loanMapper_toEntity() {
        Book book = new Book();
        book.setId("b1");
        book.setTitle("T");
        book.setAuthor("A");
        book.setTotalStock(5);
        book.setAvailableCopies(3);

        User user = new User();
        user.setId("u1");
        user.setName("N");
        user.setUsername("u");
        user.setPassword("p");
        user.setRole(Role.USER);

        Loan loan = new Loan();
        loan.setId("l1");
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setStatus(LoanStatus.ACTIVE);

        LoanEntity entity = loanMapper.toEntity(loan);
        assertEquals("l1", entity.getId());
        assertEquals("b1", entity.getBook().getId());
        assertEquals("u1", entity.getUser().getId());
    }

    @Test
    @DisplayName("LoanEntityMapper toEntity null")
    void loanMapper_toEntity_null() {
        assertNull(loanMapper.toEntity(null));
    }
}
