//package com.venduit.assetmanagement.api_gateway.config;
//
//import jakarta.servlet.http.HttpServletRequestWrapper;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ResponseStatusException;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//
//import java.io.IOException;
//import java.net.URI;
//import java.util.Enumeration;
//import java.util.Map;
//
//@Component
//public class MultiTenantFilter implements Filter, Ordered {
//
//    private final Map<String, String> tenantToServiceMap = Map.of(
//            "tenant1", "http://localhost:8080",
//            "tenant2", "http://localhost:8082",
//            "tenant3", "http://localhost:8083"
//    );
//
//    private static final String TENANT_HEADER = "X-Tenant-ID";
//
//    @Override
//    public int getOrder() {
//        return 0; // Execute this filter first
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
//
//        Enumeration<String> headerNames = httpRequest.getHeaderNames();
//        boolean hasTenantHeader = false;
//
//        while (headerNames.hasMoreElements()) {
//            if (TENANT_HEADER.equalsIgnoreCase(headerNames.nextElement())) {
//                hasTenantHeader = true;
//                break;
//            }
//        }
//
//        String tenantId = null;
//        if (hasTenantHeader) {
//            tenantId = httpRequest.getHeader(TENANT_HEADER);
//        }
//
//        if (tenantId == null || !tenantToServiceMap.containsKey(tenantId)) {
//            filterChain.doFilter(servletRequest, servletResponse);
//        }
//
//        String targetService = tenantToServiceMap.get(tenantId);
////        URI newUri = URI.create(targetService);
//        String newUri = targetService + httpRequest.getRequestURI();
//
//        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(httpRequest) {
//            @Override
//            public StringBuffer getRequestURL() {
//                return new StringBuffer(newUri);
//            }
//
//            @Override
//            public String getRequestURI() {
//                return newUri;
//            }
//        };
//
//        filterChain.doFilter(wrappedRequest, servletResponse);
//    }
//}
//
//
