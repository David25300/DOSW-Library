package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public Loan createLoan(@RequestParam String bookId, @RequestParam String userId) {
        return loanService.createLoan(bookId, userId);
    }

    @PutMapping("/{loanId}/return")
    public Loan returnLoan(@PathVariable String loanId) {
        return loanService.returnLoan(loanId);
    }

    @GetMapping
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }
}