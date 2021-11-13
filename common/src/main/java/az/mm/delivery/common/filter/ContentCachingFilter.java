package az.mm.delivery.common.filter;

import az.mm.delivery.common.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "ContentCachingFilter", urlPatterns = "/*")
@RequiredArgsConstructor
@Slf4j
public class ContentCachingFilter extends OncePerRequestFilter {

    private final WebUtil webUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("ContentCachingFilter is calling for uri: {} {}", request.getMethod(), request.getRequestURI());

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        webUtil.initializeRequestWrapper(wrappedRequest);

        filterChain.doFilter(wrappedRequest, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !HttpMethod.POST.matches(request.getMethod());
    }

}
