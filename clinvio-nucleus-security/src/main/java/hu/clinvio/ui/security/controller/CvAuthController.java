package hu.clinvio.ui.security.controller;

import hu.clinvio.ui.security.service.CvJwtService;
import hu.clinvio.ui.security.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class CvAuthController {

    private static final Logger log = LoggerFactory.getLogger(CvAuthController.class);

    private final CvJwtService jwtService;
    private final TokenBlacklistService blacklistService;

    public CvAuthController(CvJwtService jwtService, TokenBlacklistService blacklistService) {
        this.jwtService = jwtService;
        this.blacklistService = blacklistService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "refreshToken is required"));
        }

        if (blacklistService.isBlacklisted(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Refresh token has been revoked"));
        }

        if (!jwtService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired refresh token"));
        }

        String userId = jwtService.getUserId(refreshToken);
        String username = jwtService.getUsername(refreshToken);
        String[] roles = jwtService.getRoles(refreshToken);

        String newAccessToken = jwtService.generateToken(userId, username, roles);

        blacklistService.blacklist(refreshToken, jwtService.getClaims(refreshToken).getExpiration().getTime());

        log.info("Token refreshed for user: {}", username);
        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "tokenType", "Bearer"
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = jwtService.extractToken(header);

        if (token != null && jwtService.validateToken(token)) {
            long expiry = jwtService.getClaims(token).getExpiration().getTime();
            blacklistService.blacklist(token, expiry);
            log.info("Token blacklisted for logout");
        }

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
