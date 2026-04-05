package edu.eci.dosw.DOSW_Library.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {

    private String id;
    private String bookId;
    private String bookTitle;
    private String userId;
    private String userName;
    private LocalDate loanDate;
    private String status;
    private LocalDate returnDate;

    // Historial del préstamo
    private List<LoanHistoryDTO> history;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoanHistoryDTO {
        private String status;
        private LocalDate date;
    }
}
