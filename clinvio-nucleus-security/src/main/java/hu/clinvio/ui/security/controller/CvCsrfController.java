package hu.clinvio.ui.security.controller;

import hu.clinvio.ui.security.config.SecurityProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class CvCsrfController {

    private final SecurityProperties properties;

    public CvCsrfController(SecurityProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/csrf")
    public ResponseEntity<?> csrf(HttpServletRequest request) {
        if (!properties.getCsrf().isEnabled()) {
            return ResponseEntity.ok(Map.of("enabled", false));
        }
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrf == null) {
            return ResponseEntity.ok(Map.of("enabled", true, "message", "CSRF token not yet initialized"));
        }
        return ResponseEntity.ok(Map.of(
                "enabled", true,
                "token", csrf.getToken(),
                "headerName", csrf.getHeaderName(),
                "parameterName", csrf.getParameterName()
        ));
    }
}
