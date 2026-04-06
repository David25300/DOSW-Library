package edu.eci.dosw.DOSW_Library.config;

import edu.eci.dosw.DOSW_Library.core.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigTest {

    @Mock private JwtService jwtService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    // --- JwtAuthenticationFilter ---

    @Test
    @DisplayName("Filter sin header Authorization pasa sin autenticar")
    void filter_noAuthHeader() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Filter con header sin Bearer pasa sin autenticar")
    void filter_nonBearerHeader() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        when(request.getHeader("Authorization")).thenReturn("Basic abc123");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Filter con token válido autentica")
    void filter_validToken() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtService.extractUsername("validtoken")).thenReturn("juan");
        when(jwtService.extractRole("validtoken")).thenReturn("USER");
        when(jwtService.extractUserId("validtoken")).thenReturn("user-001");
        when(jwtService.isTokenValid("validtoken", "juan")).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("juan", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    @DisplayName("Filter con token inválido no autentica")
    void filter_invalidToken() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        when(request.getHeader("Authorization")).thenReturn("Bearer badtoken");
        when(jwtService.extractUsername("badtoken")).thenThrow(new RuntimeException("Invalid token"));

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Filter con token no válido para username")
    void filter_tokenNotValidForUser() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtService.extractUsername("token123")).thenReturn("juan");
        when(jwtService.extractRole("token123")).thenReturn("USER");
        when(jwtService.extractUserId("token123")).thenReturn("u1");
        when(jwtService.isTokenValid("token123", "juan")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    // --- JwtAuthEntryPoint ---

    @Test
    @DisplayName("AuthEntryPoint retorna 401 con JSON")
    void entryPoint_returns401() throws Exception {
        JwtAuthEntryPoint entryPoint = new JwtAuthEntryPoint();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override public void write(int b) { outputStream.write(b); }
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(jakarta.servlet.WriteListener listener) {}
        };
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        entryPoint.commence(request, response, mock(AuthenticationException.class));

        verify(response).setStatus(401);
        verify(response).setContentType("application/json");
        String body = outputStream.toString();
        assertTrue(body.contains("401"));
    }

    // --- JwtAccessDeniedHandler ---

    @Test
    @DisplayName("AccessDeniedHandler retorna 403 con JSON")
    void accessDeniedHandler_returns403() throws Exception {
        JwtAccessDeniedHandler handler = new JwtAccessDeniedHandler();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override public void write(int b) { outputStream.write(b); }
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(jakarta.servlet.WriteListener listener) {}
        };
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        handler.handle(request, response, new AccessDeniedException("Denied"));

        verify(response).setStatus(403);
        verify(response).setContentType("application/json");
        String body = outputStream.toString();
        assertTrue(body.contains("403"));
    }
}
