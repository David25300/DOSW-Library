package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.controller.dto.LoginRequest;
import edu.eci.dosw.DOSW_Library.controller.dto.LoginResponse;
import edu.eci.dosw.DOSW_Library.controller.dto.RegisterRequest;
import edu.eci.dosw.DOSW_Library.controller.dto.UserDTO;
import edu.eci.dosw.DOSW_Library.controller.mapper.UserMapper;
import edu.eci.dosw.DOSW_Library.core.model.Role;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Autenticación y registro de usuarios")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    public AuthController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Iniciar sesión y obtener token JWT")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar un nuevo usuario (rol USER por defecto)")
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(
                request.getName(),
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getMembershipType()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDTO(user));
    }

    @Operation(summary = "Registrar un nuevo bibliotecario (solo LIBRARIAN)")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/register-librarian")
    public ResponseEntity<UserDTO> registerLibrarian(@Valid @RequestBody RegisterRequest request) {
        User user = authService.registerWithRole(
                request.getName(),
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getMembershipType(),
                Role.LIBRARIAN
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDTO(user));
    }
}
