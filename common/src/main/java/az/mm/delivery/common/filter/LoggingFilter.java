package az.mm.delivery.common.filter;

import az.mm.delivery.common.config.properties.FilterProperties;
import az.mm.delivery.common.constant.CommonConstants.HttpAttribute;
import az.mm.delivery.common.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(value = Ordered.HIGHEST_PRECEDENCE + 1)
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    private final FilterProperties filterProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("LoggingFilter is calling for uri: {} {}", request.getMethod(), request.getRequestURI());

        MDC.clear();
        final String requestId = CommonUtil.generateId();
        request.setAttribute(HttpAttribute.REQUEST_ID, requestId);
        request.setAttribute(HttpAttribute.ELAPSED_TIME, System.currentTimeMillis());
        MDC.put(HttpAttribute.REQUEST_ID, requestId);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return filterProperties.getSkippedUrlsForLoggingFilter().stream()
                .map(AntPathRequestMatcher::new)
                .anyMatch(antPath -> antPath.matches(request));
    }

}
