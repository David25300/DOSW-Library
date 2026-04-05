package edu.eci.dosw.DOSW_Library.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String id;
    private String name;
    private String username;
    private String role;

    // Campos extendidos
    private String email;
    private String membershipType;
    private LocalDate registrationDate;
}
