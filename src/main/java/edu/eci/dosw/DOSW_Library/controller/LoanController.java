package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.controller.dto.LoanDTO;
import edu.eci.dosw.DOSW_Library.controller.mapper.LoanMapper;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import edu.eci.dosw.DOSW_Library.core.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@Tag(name = "Loans", description = "Operaciones relacionadas con préstamos")
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;
    private final SecurityUtils securityUtils;

    public LoanController(LoanService loanService, LoanMapper loanMapper, SecurityUtils securityUtils) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
        this.securityUtils = securityUtils;
    }

    @Operation(summary = "Crear un nuevo préstamo (USER o LIBRARIAN)")
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN')")
    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestParam String bookId,
                                              @RequestParam String userId) {
        if (!securityUtils.isLibrarian() && !securityUtils.isOwner(userId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "No puedes crear préstamos para otros usuarios");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanMapper.toDTO(loanService.createLoan(bookId, userId)));
    }

    @Operation(summary = "Devolver un préstamo (USER dueño o LIBRARIAN)")
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN')")
    @PutMapping("/{loanId}/return")
    public ResponseEntity<LoanDTO> returnLoan(@PathVariable String loanId) {
        loanService.validateLoanOwnership(loanId, securityUtils.getCurrentUserId(), securityUtils.isLibrarian());
        return ResponseEntity.ok(loanMapper.toDTO(loanService.returnLoan(loanId)));
    }

    @Operation(summary = "Consultar todos los préstamos (solo LIBRARIAN)")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> loans = loanService.getAllLoans()
                .stream()
                .map(loanMapper::toDTO)
                .toList();
        return ResponseEntity.ok(loans);
    }

    @Operation(summary = "Consultar mis préstamos (USER ve solo los suyos, LIBRARIAN ve los de cualquiera)")
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanDTO>> getLoansByUser(@PathVariable String userId) {
        if (!securityUtils.isLibrarian() && !securityUtils.isOwner(userId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "No puedes consultar préstamos de otros usuarios");
        }
        List<LoanDTO> loans = loanService.getLoansByUserId(userId)
                .stream()
                .map(loanMapper::toDTO)
                .toList();
        return ResponseEntity.ok(loans);
    }

    @Operation(summary = "Consultar mis préstamos (atajo para el usuario autenticado)")
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN')")
    @GetMapping("/my")
    public ResponseEntity<List<LoanDTO>> getMyLoans() {
        String currentUserId = securityUtils.getCurrentUserId();
        List<LoanDTO> loans = loanService.getLoansByUserId(currentUserId)
                .stream()
                .map(loanMapper::toDTO)
                .toList();
        return ResponseEntity.ok(loans);
    }

    @Operation(summary = "Eliminar un préstamo devuelto (solo LIBRARIAN)")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{loanId}")
    public ResponseEntity<Void> deleteLoan(@PathVariable String loanId) {
        loanService.deleteLoan(loanId);
        return ResponseEntity.noContent().build();
    }
}
