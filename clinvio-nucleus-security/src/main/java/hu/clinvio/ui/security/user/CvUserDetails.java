package hu.clinvio.ui.security.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User details implementation for Clinvio applications.
 *
 * <pre>{@code
 * CvUserDetails user = CvUserDetails.builder()
 *     .id(1L)
 *     .username("admin")
 *     .password(hashedPassword)
 *     .roles(Set.of("ADMIN", "USER"))
 *     .build();
 * }</pre>
 */
public class CvUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final Set<String> roles;
    private final boolean active;

    private CvUserDetails(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.password = builder.password;
        this.roles = builder.roles;
        this.active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() { return id; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return active; }

    public Set<String> getRoles() { return roles; }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public static class Builder {
        private Long id;
        private String username;
        private String password;
        private Set<String> roles = Set.of();
        private boolean active = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder roles(Set<String> roles) { this.roles = roles; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public CvUserDetails build() { return new CvUserDetails(this); }
    }
}
