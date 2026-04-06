package edu.eci.dosw.DOSW_Library.persistence.nonrelational.mapper;

import edu.eci.dosw.DOSW_Library.core.model.*;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.BookDocument;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.LoanDocument;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.UserDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NonRelationalMapperTest {

    private final BookDocumentMapper bookMapper = new BookDocumentMapper();
    private final UserDocumentMapper userMapper = new UserDocumentMapper();
    private final LoanDocumentMapper loanMapper = new LoanDocumentMapper();

    // --- BookDocumentMapper ---

    @Test
    @DisplayName("BookDocumentMapper toDomain con todos los campos")
    void bookMapper_toDomain_full() {
        BookDocument doc = new BookDocument();
        doc.setId("b1");
        doc.setTitle("Title");
        doc.setAuthor("Author");
        doc.setIsbn("isbn-123");
        doc.setCategories(List.of("Cat1", "Cat2"));
        doc.setPublicationType("LIBRO");
        doc.setPublicationDate(LocalDate.of(2020, 1, 1));
        doc.setAddedToCatalogDate(LocalDate.now());
        doc.setMetaData(new BookDocument.MetaData(300, "ES", "Publisher"));
        doc.setAvailability(new BookDocument.Availability("AVAILABLE", 5, 3, 2));

        Book book = bookMapper.toDomain(doc);

        assertEquals("b1", book.getId());
        assertEquals("isbn-123", book.getIsbn());
        assertEquals(2, book.getCategories().size());
        assertEquals(300, book.getPages());
        assertEquals("ES", book.getLanguage());
        assertEquals("Publisher", book.getPublisher());
        assertEquals(5, book.getTotalStock());
        assertEquals(3, book.getAvailableCopies());
    }

    @Test
    @DisplayName("BookDocumentMapper toDomain sin metadata ni availability")
    void bookMapper_toDomain_nullSubObjects() {
        BookDocument doc = new BookDocument();
        doc.setId("b1");
        doc.setTitle("Title");
        doc.setAuthor("Author");

        Book book = bookMapper.toDomain(doc);

        assertEquals("b1", book.getId());
        assertEquals(0, book.getPages());
        assertEquals(0, book.getTotalStock());
    }

    @Test
    @DisplayName("BookDocumentMapper toDomain null")
    void bookMapper_toDomain_null() {
        assertNull(bookMapper.toDomain(null));
    }

    @Test
    @DisplayName("BookDocumentMapper toDocument con todos los campos")
    void bookMapper_toDocument_full() {
        Book book = new Book();
        book.setId("b1");
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setTotalStock(5);
        book.setAvailableCopies(3);
        book.setIsbn("isbn-123");
        book.setCategories(List.of("Cat1"));
        book.setPublicationType("EBOOK");
        book.setPublicationDate(LocalDate.of(2020, 1, 1));
        book.setAddedToCatalogDate(LocalDate.now());
        book.setPages(200);
        book.setLanguage("EN");
        book.setPublisher("Pub");

        BookDocument doc = bookMapper.toDocument(book);

        assertEquals("b1", doc.getId());
        assertEquals("isbn-123", doc.getIsbn());
        assertEquals("EBOOK", doc.getPublicationType());
        assertNotNull(doc.getMetaData());
        assertEquals(200, doc.getMetaData().getPages());
        assertEquals("EN", doc.getMetaData().getLanguage());
        assertNotNull(doc.getAvailability());
        assertEquals(5, doc.getAvailability().getTotalCopies());
        assertEquals(3, doc.getAvailability().getAvailableCopies());
        assertEquals(2, doc.getAvailability().getBorrowedCopies());
        assertEquals("AVAILABLE", doc.getAvailability().getStatus());
    }

    @Test
    @DisplayName("BookDocumentMapper toDocument sin addedToCatalogDate asigna hoy")
    void bookMapper_toDocument_nullDate() {
        Book book = new Book();
        book.setId("b1");
        book.setTitle("T");
        book.setAuthor("A");
        book.setTotalStock(1);
        book.setAvailableCopies(1);

        BookDocument doc = bookMapper.toDocument(book);

        assertEquals(LocalDate.now(), doc.getAddedToCatalogDate());
    }

    @Test
    @DisplayName("BookDocumentMapper toDocument con 0 copias = UNAVAILABLE")
    void bookMapper_toDocument_unavailable() {
        Book book = new Book();
        book.setId("b1");
        book.setTitle("T");
        book.setAuthor("A");
        book.setTotalStock(5);
        book.setAvailableCopies(0);

        BookDocument doc = bookMapper.toDocument(book);

        assertEquals("UNAVAILABLE", doc.getAvailability().getStatus());
    }

    @Test
    @DisplayName("BookDocumentMapper toDocument null")
    void bookMapper_toDocument_null() {
        assertNull(bookMapper.toDocument(null));
    }

    // --- UserDocumentMapper ---

    @Test
    @DisplayName("UserDocumentMapper toDomain con todos los campos")
    void userMapper_toDomain_full() {
        UserDocument doc = new UserDocument();
        doc.setId("u1");
        doc.setName("Name");
        doc.setUsername("user");
        doc.setPassword("pass");
        doc.setRole("LIBRARIAN");
        doc.setEmail("test@mail.com");
        doc.setMembershipType("VIP");
        doc.setRegistrationDate(LocalDate.now());

        User user = userMapper.toDomain(doc);

        assertEquals("u1", user.getId());
        assertEquals(Role.LIBRARIAN, user.getRole());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("VIP", user.getMembershipType());
    }

    @Test
    @DisplayName("UserDocumentMapper toDomain sin role")
    void userMapper_toDomain_nullRole() {
        UserDocument doc = new UserDocument();
        doc.setId("u1");
        doc.setName("Name");
        doc.setUsername("user");
        doc.setPassword("pass");

        User user = userMapper.toDomain(doc);

        assertNull(user.getRole());
    }

    @Test
    @DisplayName("UserDocumentMapper toDomain null")
    void userMapper_toDomain_null() {
        assertNull(userMapper.toDomain(null));
    }

    @Test
    @DisplayName("UserDocumentMapper toDocument con todos los campos")
    void userMapper_toDocument_full() {
        User user = new User();
        user.setId("u1");
        user.setName("Name");
        user.setUsername("user");
        user.setPassword("pass");
        user.setRole(Role.USER);
        user.setEmail("e@m.com");
        user.setMembershipType("PLATINUM");
        user.setRegistrationDate(LocalDate.now());

        UserDocument doc = userMapper.toDocument(user);

        assertEquals("u1", doc.getId());
        assertEquals("USER", doc.getRole());
        assertEquals("e@m.com", doc.getEmail());
    }

    @Test
    @DisplayName("UserDocumentMapper toDocument sin role")
    void userMapper_toDocument_nullRole() {
        User user = new User();
        user.setId("u1");
        user.setName("Name");
        user.setUsername("user");
        user.setPassword("pass");

        UserDocument doc = userMapper.toDocument(user);

        assertNull(doc.getRole());
    }

    @Test
    @DisplayName("UserDocumentMapper toDocument null")
    void userMapper_toDocument_null() {
        assertNull(userMapper.toDocument(null));
    }

    // --- LoanDocumentMapper ---

    @Test
    @DisplayName("LoanDocumentMapper toDomain con todos los campos")
    void loanMapper_toDomain_full() {
        LoanDocument doc = new LoanDocument();
        doc.setId("l1");
        doc.setBookId("b1");
        doc.setUserId("u1");
        doc.setLoanDate(LocalDate.now());
        doc.setStatus("ACTIVE");
        List<LoanDocument.LoanHistory> history = new ArrayList<>();
        history.add(new LoanDocument.LoanHistory("ACTIVE", LocalDate.now()));
        doc.setHistory(history);

        Loan loan = loanMapper.toDomain(doc);

        assertEquals("l1", loan.getId());
        assertEquals("b1", loan.getBook().getId());
        assertEquals("u1", loan.getUser().getId());
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
        assertEquals(1, loan.getHistory().size());
    }

    @Test
    @DisplayName("LoanDocumentMapper toDomain sin book ni user ni status ni history")
    void loanMapper_toDomain_nullFields() {
        LoanDocument doc = new LoanDocument();
        doc.setId("l1");

        Loan loan = loanMapper.toDomain(doc);

        assertEquals("l1", loan.getId());
        assertNull(loan.getBook());
        assertNull(loan.getUser());
        assertNull(loan.getStatus());
    }

    @Test
    @DisplayName("LoanDocumentMapper toDomain null")
    void loanMapper_toDomain_null() {
        assertNull(loanMapper.toDomain(null));
    }

    @Test
    @DisplayName("LoanDocumentMapper toDocument con todos los campos")
    void loanMapper_toDocument_full() {
        Book book = new Book();
        book.setId("b1");
        User user = new User();
        user.setId("u1");

        Loan loan = new Loan();
        loan.setId("l1");
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());
        List<Loan.LoanHistoryEntry> history = new ArrayList<>();
        history.add(new Loan.LoanHistoryEntry("ACTIVE", LocalDate.now()));
        history.add(new Loan.LoanHistoryEntry("RETURNED", LocalDate.now()));
        loan.setHistory(history);

        LoanDocument doc = loanMapper.toDocument(loan);

        assertEquals("l1", doc.getId());
        assertEquals("b1", doc.getBookId());
        assertEquals("u1", doc.getUserId());
        assertEquals("RETURNED", doc.getStatus());
        assertEquals(2, doc.getHistory().size());
    }

    @Test
    @DisplayName("LoanDocumentMapper toDocument sin book ni user ni status ni history")
    void loanMapper_toDocument_nullFields() {
        Loan loan = new Loan();
        loan.setId("l1");
        loan.setHistory(null);

        LoanDocument doc = loanMapper.toDocument(loan);

        assertNull(doc.getBookId());
        assertNull(doc.getUserId());
        assertNull(doc.getStatus());
        assertNotNull(doc.getHistory());
        assertTrue(doc.getHistory().isEmpty());
    }

    @Test
    @DisplayName("LoanDocumentMapper toDocument null")
    void loanMapper_toDocument_null() {
        assertNull(loanMapper.toDocument(null));
    }
}
