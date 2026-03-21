package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/loans")
@Tag(name = "Loans", description = "Operaciones relacionadas con prestamos")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Operation(summary = "Crear un nuevo préstamo")
    @PostMapping
    public Loan createLoan(@RequestParam String bookId, @RequestParam String userId) {
        return loanService.createLoan(bookId, userId);
    }

    @Operation(summary = "devolver un préstamo")
    @PutMapping("/{loanId}/return")
    public Loan returnLoan(@PathVariable String loanId) {
        return loanService.returnLoan(loanId);
    }

    @Operation(summary = "Consultar todos los préstamos")
    @GetMapping
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }
}