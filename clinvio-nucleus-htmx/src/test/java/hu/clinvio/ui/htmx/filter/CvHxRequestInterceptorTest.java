package hu.clinvio.ui.htmx.filter;

import hu.clinvio.ui.htmx.annotation.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.method.HandlerMethod;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CvHxRequestInterceptorTest {

    private final CvHxRequestInterceptor interceptor = new CvHxRequestInterceptor();
    private HttpServletRequest request;
    private HttpServletResponse response;

    @HxRequest
    static class HxController {
        public void hxEndpoint() {}
    }

    static class PublicController {
        public void publicEndpoint() {}
    }

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void preHandle_shouldReturnTrueForNonHandlerMethod() throws Exception {
        assertTrue(interceptor.preHandle(request, response, "not a handler"));
    }

    @Test
    void preHandle_shouldReturnTrueWhenNoHxRequestAnnotation() throws Exception {
        PublicController controller = new PublicController();
        HandlerMethod handler = new HandlerMethod(controller,
                PublicController.class.getMethod("publicEndpoint"));

        assertTrue(interceptor.preHandle(request, response, handler));
    }

    @Test
    void preHandle_shouldReturnTrueForHxRequestWithHeader() throws Exception {
        HxController controller = new HxController();
        HandlerMethod handler = new HandlerMethod(controller,
                HxController.class.getMethod("hxEndpoint"));

        when(request.getHeader("HX-Request")).thenReturn("true");

        assertTrue(interceptor.preHandle(request, response, handler));
    }

    @Test
    void preHandle_shouldReturnFalseForHxRequestWithoutHeader() throws Exception {
        HxController controller = new HxController();
        HandlerMethod handler = new HandlerMethod(controller,
                HxController.class.getMethod("hxEndpoint"));

        when(request.getHeader("HX-Request")).thenReturn(null);

        assertFalse(interceptor.preHandle(request, response, handler));
        verify(response).sendError(400, "This endpoint requires an HTMX request (HX-Request header)");
    }
}
