package com.example.CalendarDevelop.cys.config.filter;

import com.example.CalendarDevelop.cys.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class AuthenticationFilter implements Filter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Paths that don't require authentication
    private final List<String> excludedPaths = Arrays.asList(
            "/members", // POST - Registration
            "/login",   // POST - Login
            "/logout"   // GET - Logout
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        // Skip authentication for excluded paths
        if (isExcludedPath(requestURI, httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // Check authentication
        Long userId = AuthService.getLoggedInUserId(httpRequest.getSession(false));

        if (userId == null) {
            // User not authenticated
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "UNAUTHORIZED");
            errorResponse.put("code", 401);
            errorResponse.put("message", "로그인이 필요한 서비스입니다.");

            httpResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        // Continue with the request
        chain.doFilter(request, response);
    }

    private boolean isExcludedPath(String requestURI, String method) {
        if ("POST".equalsIgnoreCase(method) && pathMatcher.match("/members", requestURI)) {
            return true;
        }

        return excludedPaths.stream()
                .anyMatch(path -> pathMatcher.match(path, requestURI));
    }
}