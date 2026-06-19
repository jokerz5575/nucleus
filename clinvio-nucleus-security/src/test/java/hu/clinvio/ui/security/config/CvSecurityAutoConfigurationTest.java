package hu.clinvio.ui.security.config;

import hu.clinvio.ui.security.service.CvJwtService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CvSecurityAutoConfigurationTest {

    private final CvSecurityAutoConfiguration configuration = new CvSecurityAutoConfiguration();

    @Test
    void cvJwtServiceRejectsDefaultSecretInProductionProfile() {
        SecurityProperties properties = new SecurityProperties();
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles("production");

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> configuration.cvJwtService(properties, environment));

        assertTrue(ex.getMessage().contains("Default JWT secret"));
    }

    @Test
    void cvJwtServiceAllowsDefaultSecretOutsideProductionProfile() {
        SecurityProperties properties = new SecurityProperties();
        MockEnvironment environment = new MockEnvironment();

        CvJwtService service = configuration.cvJwtService(properties, environment);

        assertNotNull(service);
    }

    @Test
    void cvJwtServiceAllowsCustomSecretInProductionProfile() {
        SecurityProperties properties = new SecurityProperties();
        properties.getJwt().setSecret("custom-production-secret-at-least-32-chars");
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles("prod");

        CvJwtService service = configuration.cvJwtService(properties, environment);

        assertNotNull(service);
    }

    @Test
    void corsDefaultsAllowWildcardWithoutCredentials() {
        SecurityProperties properties = new SecurityProperties();
        CorsConfigurationSource source = configuration.corsConfigurationSource(properties);

        CorsConfiguration cors = source.getCorsConfiguration(new MockHttpServletRequest("GET", "/"));

        assertNotNull(cors);
        assertTrue(cors.getAllowedOrigins().contains("*"));
        assertTrue(Boolean.FALSE.equals(cors.getAllowCredentials()));
    }
}
