package edu.eci.dosw.DOSW_Library.controller.mapper;

import edu.eci.dosw.DOSW_Library.controller.dto.BookDTO;
import edu.eci.dosw.DOSW_Library.controller.dto.LoanDTO;
import edu.eci.dosw.DOSW_Library.controller.dto.UserDTO;
import edu.eci.dosw.DOSW_Library.core.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {

    // --- BookMapper ---

    private final BookMapper bookMapper = new BookMapper();
    private final UserMapper userMapper = new UserMapper();
    private final LoanMapper loanMapper = new LoanMapper();

    @Test
    @DisplayName("BookMapper toDTO convierte correctamente")
    void bookMapper_toDTO() {
        Book book = new Book();
        book.setId("b1");
        book.setTitle("Test");
        book.setAuthor("Author");
        book.setTotalStock(5);
        book.setAvailableCopies(3);
        book.setIsbn("isbn-123");
        book.setCategories(List.of("Cat1"));
        book.setPublicationType("LIBRO");
        book.setPages(100);
        book.setLanguage("ES");
        book.setPublisher("Pub");
        book.setPublicationDate(LocalDate.of(2020, 1, 1));
        book.setAddedToCatalogDate(LocalDate.now());

        BookDTO dto = bookMapper.toDTO(book);

        assertEquals("b1", dto.getId());
        assertEquals("Test", dto.getTitle());
        assertEquals(5, dto.getTotalStock());
        assertEquals(3, dto.getAvailableCopies());
        assertEquals(2, dto.getBorrowedCopies());
        assertTrue(dto.isAvailable());
        assertEquals("isbn-123", dto.getIsbn());
        assertEquals("LIBRO", dto.getPublicationType());
    }

    @Test
    @DisplayName("BookMapper toDTO null retorna null")
    void bookMapper_toDTO_null() {
        assertNull(bookMapper.toDTO(null));
    }

    @Test
    @DisplayName("BookMapper toDomain convierte correctamente")
    void bookMapper_toDomain() {
        BookDTO dto = new BookDTO();
        dto.setId("b1");
        dto.setTitle("Test");
        dto.setAuthor("Author");
        dto.setTotalStock(5);
        dto.setAvailableCopies(3);

        Book book = bookMapper.toDomain(dto);

        assertEquals("b1", book.getId());
        assertEquals(5, book.getTotalStock());
    }

    @Test
    @DisplayName("BookMapper toDomain null retorna null")
    void bookMapper_toDomain_null() {
        assertNull(bookMapper.toDomain(null));
    }

    // --- UserMapper ---

    @Test
    @DisplayName("UserMapper toDTO convierte correctamente")
    void userMapper_toDTO() {
        User user = new User();
        user.setId("u1");
        user.setName("Test");
        user.setUsername("test");
        user.setRole(Role.USER);
        user.setEmail("test@mail.com");
        user.setMembershipType("VIP");
        user.setRegistrationDate(LocalDate.now());

        UserDTO dto = userMapper.toDTO(user);

        assertEquals("u1", dto.getId());
        assertEquals("USER", dto.getRole());
        assertEquals("test@mail.com", dto.getEmail());
        assertEquals("VIP", dto.getMembershipType());
    }

    @Test
    @DisplayName("UserMapper toDTO null retorna null")
    void userMapper_toDTO_null() {
        assertNull(userMapper.toDTO(null));
    }

    @Test
    @DisplayName("UserMapper toDTO con role null")
    void userMapper_toDTO_nullRole() {
        User user = new User();
        user.setId("u1");
        user.setName("Test");
        user.setUsername("test");

        UserDTO dto = userMapper.toDTO(user);

        assertNull(dto.getRole());
    }

    @Test
    @DisplayName("UserMapper toDomain convierte correctamente")
    void userMapper_toDomain() {
        UserDTO dto = new UserDTO();
        dto.setId("u1");
        dto.setName("Test");
        dto.setUsername("test");
        dto.setRole("LIBRARIAN");
        dto.setEmail("t@m.com");
        dto.setMembershipType("PLATINUM");

        User user = userMapper.toDomain(dto);

        assertEquals("u1", user.getId());
        assertEquals(Role.LIBRARIAN, user.getRole());
    }

    @Test
    @DisplayName("UserMapper toDomain null retorna null")
    void userMapper_toDomain_null() {
        assertNull(userMapper.toDomain(null));
    }

    @Test
    @DisplayName("UserMapper toDomain con role null")
    void userMapper_toDomain_nullRole() {
        UserDTO dto = new UserDTO();
        dto.setId("u1");
        dto.setName("Test");
        dto.setUsername("test");

        User user = userMapper.toDomain(dto);

        assertNull(user.getRole());
    }

    // --- LoanMapper ---

    @Test
    @DisplayName("LoanMapper toDTO convierte correctamente")
    void loanMapper_toDTO() {
        Book book = new Book();
        book.setId("b1");
        book.setTitle("Test Book");

        User user = new User();
        user.setId("u1");
        user.setName("Test User");

        Loan loan = new Loan();
        loan.setId("l1");
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setStatus(LoanStatus.ACTIVE);

        List<Loan.LoanHistoryEntry> history = new ArrayList<>();
        history.add(new Loan.LoanHistoryEntry("ACTIVE", LocalDate.now()));
        loan.setHistory(history);

        LoanDTO dto = loanMapper.toDTO(loan);

        assertEquals("l1", dto.getId());
        assertEquals("b1", dto.getBookId());
        assertEquals("Test Book", dto.getBookTitle());
        assertEquals("u1", dto.getUserId());
        assertEquals("Test User", dto.getUserName());
        assertEquals("ACTIVE", dto.getStatus());
        assertEquals(1, dto.getHistory().size());
    }

    @Test
    @DisplayName("LoanMapper toDTO null retorna null")
    void loanMapper_toDTO_null() {
        assertNull(loanMapper.toDTO(null));
    }

    @Test
    @DisplayName("LoanMapper toDTO con book y user null")
    void loanMapper_toDTO_nullBookAndUser() {
        Loan loan = new Loan();
        loan.setId("l1");
        loan.setStatus(LoanStatus.ACTIVE);

        LoanDTO dto = loanMapper.toDTO(loan);

        assertNull(dto.getBookId());
        assertNull(dto.getUserId());
    }

    @Test
    @DisplayName("LoanMapper toDTO con historial null")
    void loanMapper_toDTO_nullHistory() {
        Loan loan = new Loan();
        loan.setId("l1");
        loan.setHistory(null);

        LoanDTO dto = loanMapper.toDTO(loan);

        assertNotNull(dto.getHistory());
        assertTrue(dto.getHistory().isEmpty());
    }

    @Test
    @DisplayName("LoanMapper toDTO con status null")
    void loanMapper_toDTO_nullStatus() {
        Loan loan = new Loan();
        loan.setId("l1");

        LoanDTO dto = loanMapper.toDTO(loan);

        assertNull(dto.getStatus());
    }
}
