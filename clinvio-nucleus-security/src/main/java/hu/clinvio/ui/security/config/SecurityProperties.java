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
    private Cors cors = new Cors();
    private Csrf csrf = new Csrf();
    private String loginPage = "/login";
    private String[] permitPaths = {"/login", "/register", "/css/**", "/js/**", "/images/**", "/api/auth/**"};

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Jwt getJwt() { return jwt; }
    public void setJwt(Jwt jwt) { this.jwt = jwt; }

    public Cors getCors() { return cors; }
    public void setCors(Cors cors) { this.cors = cors; }

    public Csrf getCsrf() { return csrf; }
    public void setCsrf(Csrf csrf) { this.csrf = csrf; }

    public String getLoginPage() { return loginPage; }
    public void setLoginPage(String loginPage) { this.loginPage = loginPage; }

    public String[] getPermitPaths() { return permitPaths; }
    public void setPermitPaths(String[] permitPaths) { this.permitPaths = permitPaths; }

    public static class Jwt {
        private String secret = "clinvio-default-secret-change-in-production-min-32-chars";
        private long expiration = 86400000; // 24 hours
        private long refreshExpiration = 604800000; // 7 days
        private String header = "Authorization";
        private String prefix = "Bearer ";

        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        public long getExpiration() { return expiration; }
        public void setExpiration(long expiration) { this.expiration = expiration; }
        public long getRefreshExpiration() { return refreshExpiration; }
        public void setRefreshExpiration(long refreshExpiration) { this.refreshExpiration = refreshExpiration; }
        public String getHeader() { return header; }
        public void setHeader(String header) { this.header = header; }
        public String getPrefix() { return prefix; }
        public void setPrefix(String prefix) { this.prefix = prefix; }
    }

    public static class Cors {
        private String[] allowedOrigins = {"*"};
        private String[] allowedMethods = {"GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"};
        private String[] allowedHeaders = {"*"};
        private boolean allowCredentials = false;
        private long maxAge = 3600;

        public String[] getAllowedOrigins() { return allowedOrigins; }
        public void setAllowedOrigins(String[] allowedOrigins) { this.allowedOrigins = allowedOrigins; }
        public String[] getAllowedMethods() { return allowedMethods; }
        public void setAllowedMethods(String[] allowedMethods) { this.allowedMethods = allowedMethods; }
        public String[] getAllowedHeaders() { return allowedHeaders; }
        public void setAllowedHeaders(String[] allowedHeaders) { this.allowedHeaders = allowedHeaders; }
        public boolean isAllowCredentials() { return allowCredentials; }
        public void setAllowCredentials(boolean allowCredentials) { this.allowCredentials = allowCredentials; }
        public long getMaxAge() { return maxAge; }
        public void setMaxAge(long maxAge) { this.maxAge = maxAge; }
    }

    public static class Csrf {
        private boolean enabled = true;
        private String headerName = "X-CSRF-Token";
        private String parameterName = "_csrf";

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getHeaderName() { return headerName; }
        public void setHeaderName(String headerName) { this.headerName = headerName; }
        public String getParameterName() { return parameterName; }
        public void setParameterName(String parameterName) { this.parameterName = parameterName; }
    }
}
