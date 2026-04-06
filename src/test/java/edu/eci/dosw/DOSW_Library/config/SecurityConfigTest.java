package edu.eci.dosw.DOSW_Library.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Mock
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Test
    @DisplayName("SecurityConfig crea PasswordEncoder BCrypt")
    void passwordEncoder() {
        SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter, jwtAuthEntryPoint, jwtAccessDeniedHandler);
        PasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
        String encoded = encoder.encode("test123");
        assertTrue(encoder.matches("test123", encoded));
        assertFalse(encoder.matches("wrong", encoded));
    }

    @Test
    @DisplayName("SecurityConfig crea CorsConfigurationSource")
    void corsConfigurationSource() {
        SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter, jwtAuthEntryPoint, jwtAccessDeniedHandler);

        assertNotNull(config.corsConfigurationSource());
    }
}
