package com.venduit.assetmanagement.api_gateway.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class AddUserHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Extract user info from Spring Security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            String roles = authentication.getAuthorities().stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));

            // Wrap the request to add custom headers
            HttpServletRequest modifiedRequest = new HttpServletRequestWrapper(httpRequest) {
                @Override
                public String getHeader(String name) {
                    if ("X-User-Id".equalsIgnoreCase(name)) {
                        return username;
                    } else if ("X-User-Roles".equalsIgnoreCase(name)) {
                        return roles;
                    }
                    return super.getHeader(name);
                }
            };

            // Continue the filter chain with the modified request
            chain.doFilter(modifiedRequest, httpResponse);
        } else {
            // If no authentication, proceed without modifying the request
            chain.doFilter(request, response);
        }
    }
}