package com.venduit.assetmanagement.transaction_service.config;

import com.venduit.assetmanagement.transaction_service.model.POJO.UserContext;
import com.venduit.assetmanagement.transaction_service.model.POJO.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;


@Component
public class UserContextInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String userId = request.getHeader("X-User-Id");
        String roles = request.getHeader("X-User-Roles");

        UserContextHolder.setContext(new UserContext(userId, Collections.singletonList(roles)));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Clear thread-local context to avoid leaks
        UserContextHolder.clear();
    }
}