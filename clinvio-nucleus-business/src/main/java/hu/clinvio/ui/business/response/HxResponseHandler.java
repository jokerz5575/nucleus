package hu.clinvio.ui.business.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.clinvio.ui.core.renderer.CvRenderer;
import hu.clinvio.ui.htmx.response.HxResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Handles HxResponse return values from controller methods.
 * Renders Thymeleaf fragments and sets HTMX response headers.
 *
 * <p>This handler intercepts methods that return {@link HxResponse} and:</p>
 * <ol>
 *   <li>Sets HTMX response headers (HX-Trigger, HX-Reswap, HX-Retarget, HX-Redirect)</li>
 *   <li>Renders the specified Thymeleaf fragment with the provided model</li>
 *   <li>Writes the HTML to the response body</li>
 * </ol>
 */
public class HxResponseHandler implements HandlerMethodReturnValueHandler {

    private static final Logger log = LoggerFactory.getLogger(HxResponseHandler.class);

    private final CvRenderer renderer;
    private final ObjectMapper objectMapper;

    public HxResponseHandler(CvRenderer renderer) {
        this.renderer = renderer;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return HxResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException {

        HxResponse response = (HxResponse) returnValue;
        HttpServletResponse servletResponse = webRequest.getNativeResponse(HttpServletResponse.class);

        if (servletResponse == null) {
            log.warn("HttpServletResponse is null, cannot process HxResponse");
            return;
        }

        // Set HTMX response headers
        setHtmxHeaders(response, servletResponse);

        // Handle redirect
        if (response.getRedirect() != null) {
            servletResponse.setHeader("HX-Redirect", response.getRedirect());
            servletResponse.setStatus(HttpServletResponse.SC_OK);
            mavContainer.setRequestHandled(true);
            return;
        }

        // Render fragment if present
        if (response.getFragment() != null) {
            servletResponse.setContentType(MediaType.TEXT_HTML_VALUE);
            servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());

            Map<String, Object> model = response.getModel();
            if (model == null) {
                model = Map.of();
            }

            try {
                String html = renderer.renderFragment(response.getFragment(), model);
                servletResponse.getWriter().write(html);
            } catch (Exception e) {
                log.error("Failed to render fragment: {}", response.getFragment(), e);
                servletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                servletResponse.getWriter().write("<div class=\"cv-render-error\">Render error</div>");
            }
        }

        mavContainer.setRequestHandled(true);
    }

    private void setHtmxHeaders(HxResponse response, HttpServletResponse servletResponse) {
        // Set trigger event
        if (response.getTrigger() != null) {
            try {
                String triggerJson = objectMapper.writeValueAsString(response.getTriggerDetail());
                servletResponse.setHeader("HX-Trigger", triggerJson);
            } catch (Exception e) {
                // If trigger detail is not JSON-serializable, use the trigger name directly
                servletResponse.setHeader("HX-Trigger", response.getTrigger());
            }
        }

        // Set swap strategy
        if (response.getSwap() != null) {
            servletResponse.setHeader("HX-Reswap", response.getSwap().getValue());
        }

        // Set target
        if (response.getTarget() != null) {
            servletResponse.setHeader("HX-Retarget", response.getTarget());
        }

        // Set push URL
        if (response.getPushUrl() != null) {
            servletResponse.setHeader("HX-Push-Url", response.getPushUrl());
        }
    }
}
