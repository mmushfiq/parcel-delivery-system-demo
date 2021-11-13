package az.mm.delivery.common.filter;

import az.mm.delivery.common.security.TokenProvider;
import az.mm.delivery.common.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.debug("JwtTokenFilter is calling for uri: {} {}",
                httpServletRequest.getMethod(), httpServletRequest.getRequestURI());

        String jwt = SecurityUtil.resolveToken(httpServletRequest);
        String requestUri = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenProvider.isValidToken(jwt)) {
            Authentication authentication = tokenProvider.parseAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Set Authentication to security context for '{}', uri: {}",
                    authentication.getName(), requestUri);
        }

        filterChain.doFilter(request, response);
    }

}
