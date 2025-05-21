package com.progettomedusa.user_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class HeadersFilter extends OncePerRequestFilter {
    private static final String HEADER_APP_KEY_NAME = "X-APP-KEY";

    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String appKey = request.getHeader(HEADER_APP_KEY_NAME);

        if (appKey == null || !securityProperties.getLicensedApps().contains(appKey)) {
            log.error("Internal filter - Header retrieved is invalid -> {}", appKey);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        log.debug("Internal filter - Header retrieved from request -> {}", appKey);
        filterChain.doFilter(request, response);
    }
}
