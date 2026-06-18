package hu.clinvio.ui.security.filter;

import hu.clinvio.ui.security.config.SecurityProperties;
import hu.clinvio.ui.security.service.CvJwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CvJwtAuthenticationFilter extends OncePerRequestFilter {

    private final CvJwtService jwtService;
    private final SecurityProperties properties;

    public CvJwtAuthenticationFilter(CvJwtService jwtService, SecurityProperties properties) {
        this.jwtService = jwtService;
        this.properties = properties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(properties.getPermitPaths())
                .anyMatch(pattern -> pathMatches(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(properties.getJwt().getHeader());
        String token = jwtService.extractToken(header);

        if (token != null && jwtService.validateToken(token)) {
            String userId = jwtService.getUserId(token);
            String username = jwtService.getUsername(token);
            String[] roles = jwtService.getRoles(token);

            List<SimpleGrantedAuthority> authorities = (roles != null)
                    ? Arrays.stream(roles).map(r -> "ROLE_" + r).map(SimpleGrantedAuthority::new).toList()
                    : List.of();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, userId, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private static boolean pathMatches(String pattern, String path) {
        if (pattern.endsWith("/**")) {
            return path.startsWith(pattern.substring(0, pattern.length() - 3));
        }
        if (pattern.endsWith("/*")) {
            String prefix = pattern.substring(0, pattern.length() - 2);
            return path.startsWith(prefix) && path.indexOf('/', prefix.length()) == -1;
        }
        return pattern.equals(path);
    }
}
