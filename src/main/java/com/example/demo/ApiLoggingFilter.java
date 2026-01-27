package com.example.demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger("api_log");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        long start = System.currentTimeMillis();

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request, 1024);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);

        String traceId = UUID.randomUUID().toString();
        String ip = getClientIp(request);

        // Put into MDC -> appears in logs automatically if encoder supports it
        MDC.put("traceId", traceId);
        MDC.put("clientIp", ip);

        try {
            filterChain.doFilter(req, res);
        } finally {
            long tookMs = System.currentTimeMillis() - start;

            String method = request.getMethod();
            String path = request.getRequestURI();
            String query = request.getQueryString();
            int status = res.getStatus();

            // WARNING: logging full bodies can leak passwords/tokens.
            // Start with a safe approach: limit and skip binary/multipart.
            String requestBody = safeBody(req.getContentAsByteArray());
            String responseBody = safeBody(res.getContentAsByteArray());

            log.info(
                    "api_called method={} path={} query={} status={} tookMs={} clientIp={} request={} response={}",
                    method,
                    path,
                    query,
                    status,
                    tookMs,
                    ip,
                    requestBody,
                    responseBody
            );

            // must copy back response body or client gets empty response
            res.copyBodyToResponse();

            MDC.clear();
        }
    }

    private String safeBody(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return "";
        String s = new String(bytes, StandardCharsets.UTF_8);

        // hard limit
        int limit = 2000;
        if (s.length() > limit) s = s.substring(0, limit) + "...(truncated)";

        // you can also redact tokens/password fields here
        s = s.replaceAll("(?i)\"password\"\\s*:\\s*\".*?\"", "\"password\":\"***\"");
        s = s.replaceAll("(?i)\"token\"\\s*:\\s*\".*?\"", "\"token\":\"***\"");
        return s;
    }

    private String getClientIp(HttpServletRequest request) {
        // if you have ingress / reverse proxy, this is important
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            // first IP is real client
            return xff.split(",")[0].trim();
        }
        String xrip = request.getHeader("X-Real-IP");
        if (xrip != null && !xrip.isBlank()) return xrip.trim();

        return request.getRemoteAddr();
    }
}
