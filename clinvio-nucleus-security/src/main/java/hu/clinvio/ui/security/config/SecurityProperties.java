package hu.clinvio.ui.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Clinvio Security.
 *
 * <pre>
 * clinvio.security.enabled=true
 * clinvio.security.jwt.secret=your-secret-key-min-32-chars-long
 * clinvio.security.jwt.expiration=86400000
 * clinvio.security.login-page=/login
 * clinvio.security.permit-paths=/login,/register,/api/public/**
 * </pre>
 */
@ConfigurationProperties(prefix = "clinvio.security")
public class SecurityProperties {

    private boolean enabled = true;
    private Jwt jwt = new Jwt();
    private String loginPage = "/login";
    private String[] permitPaths = {"/login", "/register", "/css/**", "/js/**", "/images/**"};

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Jwt getJwt() { return jwt; }
    public void setJwt(Jwt jwt) { this.jwt = jwt; }

    public String getLoginPage() { return loginPage; }
    public void setLoginPage(String loginPage) { this.loginPage = loginPage; }

    public String[] getPermitPaths() { return permitPaths; }
    public void setPermitPaths(String[] permitPaths) { this.permitPaths = permitPaths; }

    public static class Jwt {
        private String secret = "clinvio-default-secret-change-in-production-min-32-chars";
        private long expiration = 86400000; // 24 hours
        private String header = "Authorization";
        private String prefix = "Bearer ";

        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        public long getExpiration() { return expiration; }
        public void setExpiration(long expiration) { this.expiration = expiration; }
        public String getHeader() { return header; }
        public void setHeader(String header) { this.header = header; }
        public String getPrefix() { return prefix; }
        public void setPrefix(String prefix) { this.prefix = prefix; }
    }
}
