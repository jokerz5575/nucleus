package hu.clinvio.ui.security.filter;

import hu.clinvio.ui.security.annotation.Secured;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CvSecuredInterceptorTest {

    private final CvSecuredInterceptor interceptor = new CvSecuredInterceptor();
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Secured
    static class SecuredController {
        public void securedEndpoint() {}
    }

    @Secured(roles = "ADMIN")
    static class AdminController {
        public void adminEndpoint() {}
    }

    static class PublicController {
        public void publicEndpoint() {}
    }

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void preHandle_shouldReturnTrueWhenNoSecuredAnnotation() throws Exception {
        PublicController controller = new PublicController();
        HandlerMethod handler = new HandlerMethod(controller,
                PublicController.class.getMethod("publicEndpoint"));

        assertTrue(interceptor.preHandle(request, response, handler));
    }

    @Test
    void preHandle_shouldReturnTrueWhenNonHandlerMethod() throws Exception {
        assertTrue(interceptor.preHandle(request, response, "not a handler method"));
    }

    @Test
    void preHandle_shouldReturnTrueWhenUserHasRequiredRole() throws Exception {
        AdminController controller = new AdminController();
        HandlerMethod handler = new HandlerMethod(controller,
                AdminController.class.getMethod("adminEndpoint"));

        Authentication auth = new TestAuthentication(true, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertTrue(interceptor.preHandle(request, response, handler));
    }

    @Test
    void preHandle_shouldThrowWhenUserLacksRequiredRole() throws Exception {
        AdminController controller = new AdminController();
        HandlerMethod handler = new HandlerMethod(controller,
                AdminController.class.getMethod("adminEndpoint"));

        Authentication auth = new TestAuthentication(true, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(AccessDeniedException.class,
                () -> interceptor.preHandle(request, response, handler));
    }

    @Test
    void preHandle_shouldThrowWhenUnauthenticated() throws Exception {
        SecuredController controller = new SecuredController();
        HandlerMethod handler = new HandlerMethod(controller,
                SecuredController.class.getMethod("securedEndpoint"));

        assertThrows(AccessDeniedException.class,
                () -> interceptor.preHandle(request, response, handler));
    }

    private static class TestAuthentication implements Authentication {
        private final boolean authenticated;
        private final Collection<? extends GrantedAuthority> authorities;

        TestAuthentication(boolean authenticated, Collection<? extends GrantedAuthority> authorities) {
            this.authenticated = authenticated;
            this.authorities = authorities;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
        @Override
        public Object getCredentials() { return null; }
        @Override
        public Object getDetails() { return null; }
        @Override
        public Object getPrincipal() { return null; }
        @Override
        public boolean isAuthenticated() { return authenticated; }
        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}
        @Override
        public String getName() { return "test"; }
    }
}
