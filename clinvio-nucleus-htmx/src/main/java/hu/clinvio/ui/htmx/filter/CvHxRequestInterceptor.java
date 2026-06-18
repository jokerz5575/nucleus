package hu.clinvio.ui.htmx.filter;

import hu.clinvio.ui.htmx.annotation.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

public class CvHxRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        HxRequest hxRequest = handlerMethod.getMethodAnnotation(HxRequest.class);
        if (hxRequest == null) {
            hxRequest = handlerMethod.getBeanType().getAnnotation(HxRequest.class);
        }
        if (hxRequest == null) {
            return true;
        }

        boolean isHxRequest = "true".equals(request.getHeader("HX-Request"));
        if (!isHxRequest) {
            response.sendError(HttpStatus.BAD_REQUEST.value(),
                    "This endpoint requires an HTMX request (HX-Request header)");
            return false;
        }

        return true;
    }
}
