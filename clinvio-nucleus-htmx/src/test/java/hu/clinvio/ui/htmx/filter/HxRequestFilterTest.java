package hu.clinvio.ui.htmx.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HxRequestFilterTest {

    private final HxRequestFilter filter = new HxRequestFilter();
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    void doFilter_shouldSetAttributeFalseForNonHtmxRequest() throws ServletException, IOException {
        when(request.getHeader("HX-Request")).thenReturn(null);

        filter.doFilter(request, response, chain);

        verify(request).setAttribute("isHxRequest", false);
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_shouldSetAttributeTrueAndHeadersForHtmxRequest() throws ServletException, IOException {
        when(request.getHeader("HX-Request")).thenReturn("true");

        filter.doFilter(request, response, chain);

        verify(request).setAttribute("isHxRequest", true);
        verify(response).setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        verify(response).setHeader("Pragma", "no-cache");
        verify(response).setDateHeader("Expires", 0);
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_shouldContinueChain() throws ServletException, IOException {
        when(request.getHeader("HX-Request")).thenReturn("true");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_shouldPassThroughNonHttpRequest() throws ServletException, IOException {
        ServletRequest nonHttpRequest = mock(ServletRequest.class);
        ServletResponse nonHttpResponse = mock(ServletResponse.class);

        filter.doFilter(nonHttpRequest, nonHttpResponse, chain);

        verify(chain).doFilter(nonHttpRequest, nonHttpResponse);
    }

    @Test
    void isHxRequest_shouldReturnAttribute() {
        when(request.getAttribute("isHxRequest")).thenReturn(true);
        assertTrue(HxRequestFilter.isHxRequest(request));
    }
}
