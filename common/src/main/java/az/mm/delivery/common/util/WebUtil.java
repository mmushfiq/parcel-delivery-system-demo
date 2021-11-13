package az.mm.delivery.common.util;

import az.mm.delivery.common.constant.CommonConstants.HttpAttribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class WebUtil {

    @Autowired
    private HttpServletRequest httpServletRequest;

    private ContentCachingRequestWrapper requestWrapper;

    public void initializeRequestWrapper(ContentCachingRequestWrapper requestWrapper) {
        this.requestWrapper = requestWrapper;
    }

    public String getClientIp() {
        if (httpServletRequest == null) return "";
        return Optional
                .ofNullable(httpServletRequest.getHeader(HttpAttribute.X_FORWARDED_FOR))
                .orElse(httpServletRequest.getRemoteAddr());
    }

    public String getRequestId() {
        return Optional.ofNullable(httpServletRequest)
                .map(req -> String.valueOf(req.getAttribute(HttpAttribute.REQUEST_ID)))
                .orElse(null);
    }

    public String getRequestUri() {
        return Optional.ofNullable(httpServletRequest)
                .map(req -> req.getMethod() + " " + req.getRequestURI())
                .orElse("");
    }

    public String getRequestUriWithQueryString() {
        return Optional.ofNullable(httpServletRequest)
                .map(req -> {
                    String uri = req.getQueryString() != null ?
                            String.join("?", req.getRequestURI(), req.getQueryString()) :
                            req.getRequestURI();
                    return req.getMethod() + " " + uri;
                })
                .orElse("");
    }

    public Map<String, String> getRequestHeadersInfo() {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = httpServletRequest.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public Long getElapsedTime() {
        return Optional.ofNullable(httpServletRequest)
                .map(req -> String.valueOf(req.getAttribute(HttpAttribute.ELAPSED_TIME)))
                .filter(StringUtils::isNumeric)
                .map(t -> System.currentTimeMillis() - Long.parseLong(t))
                .orElse(-1L);
    }

    public String getBearerTokenFromAuthorizationHeader() {
        return Optional
                .ofNullable(httpServletRequest.getHeader(HttpAttribute.AUTHORIZATION))
                .filter(token -> token.startsWith(HttpAttribute.BEARER))
                .orElse(null);
    }

    public String getRequestBody() {
        try {
            return Optional.ofNullable(requestWrapper)
                    .map(ContentCachingRequestWrapper::getContentAsByteArray)
                    .map(String::new)
                    .orElse("[null]");
        } catch (Exception e) {
            log.error("{} error occurs while getting request body for uri {}", e.getMessage(), getRequestUri(), e);
            return "[failed]";
        }
    }

}
