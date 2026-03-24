package edu.eci.dosw.DOSW_Library.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void shouldCreateOpenApiConfiguration() {
        OpenApiConfig config = new OpenApiConfig();

        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("DOSW Library API", openAPI.getInfo().getTitle());
        assertEquals("1.0", openAPI.getInfo().getVersion());
        assertEquals("API para la gestion de biblioteca", openAPI.getInfo().getDescription());
    }
}