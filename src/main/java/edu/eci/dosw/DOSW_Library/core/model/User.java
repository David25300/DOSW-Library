package edu.eci.dosw.DOSW_Library.core.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @NotBlank(message = "El ID del usuario es obligatorio")
    private String id;

    @NotBlank(message = "El nombre del usuario es obligatorio")
    private String name;
}
