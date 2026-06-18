package hu.clinvio.ui.security.filter;

import hu.clinvio.ui.security.annotation.Secured;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CvSecuredInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Secured secured = handlerMethod.getMethodAnnotation(Secured.class);
        if (secured == null) {
            secured = handlerMethod.getBeanType().getAnnotation(Secured.class);
        }
        if (secured == null) {
            return true;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Authentication required");
        }

        String[] requiredRoles = secured.roles();
        if (requiredRoles.length == 0) {
            return true;
        }

        Set<String> userRoles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r)
                .collect(Collectors.toSet());

        boolean hasRole = Arrays.stream(requiredRoles).anyMatch(userRoles::contains);
        if (!hasRole) {
            throw new AccessDeniedException("Insufficient roles. Required: " + Arrays.toString(requiredRoles));
        }

        return true;
    }
}
