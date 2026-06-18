package hu.clinvio.ui.htmx.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to detect HTMX requests and add request attributes for easy access.
 * HTMX sends specific headers: HX-Request, HX-Trigger, HX-Target, etc.
 */
public class HxRequestFilter implements Filter {

    public static final String HX_REQUEST_HEADER = "HX-Request";
    public static final String HX_TRIGGER_HEADER = "HX-Trigger";
    public static final String HX_TARGET_HEADER = "HX-Target";
    public static final String HX_PROMPT_HEADER = "HX-Prompt";
    public static final String IS_HX_REQUEST_ATTR = "isHxRequest";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest httpRequest) || !(response instanceof HttpServletResponse httpResponse)) {
            chain.doFilter(request, response);
            return;
        }

        boolean isHxRequest = "true".equals(httpRequest.getHeader(HX_REQUEST_HEADER));

        // Set request attribute for easy access in controllers
        httpRequest.setAttribute(IS_HX_REQUEST_ATTR, isHxRequest);

        if (isHxRequest) {
            // Set cache control for HTMX responses
            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setDateHeader("Expires", 0);
        }

        chain.doFilter(request, response);
    }

    /**
     * Check if the current request is an HTMX request.
     */
    public static boolean isHxRequest(HttpServletRequest request) {
        return Boolean.TRUE.equals(request.getAttribute(IS_HX_REQUEST_ATTR));
    }

    /**
     * Get the HTMX trigger element ID from the request.
     */
    public static String getHxTrigger(HttpServletRequest request) {
        return request.getHeader(HX_TRIGGER_HEADER);
    }

    /**
     * Get the HTMX target element ID from the request.
     */
    public static String getHxTarget(HttpServletRequest request) {
        return request.getHeader(HX_TARGET_HEADER);
    }

    /**
     * Get the HTMX prompt value from the request.
     */
    public static String getHxPrompt(HttpServletRequest request) {
        return request.getHeader(HX_PROMPT_HEADER);
    }
}
