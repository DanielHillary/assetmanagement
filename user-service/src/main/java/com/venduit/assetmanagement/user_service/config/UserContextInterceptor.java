package com.venduit.assetmanagement.user_service.config;

import com.venduit.assetmanagement.user_service.model.POJO.UserContext;
import com.venduit.assetmanagement.user_service.model.POJO.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;


@Component
@RequiredArgsConstructor
public class UserContextInterceptor implements HandlerInterceptor, Ordered {

    private final RequestContext requestContext;

    private static final String REGISTER_USER_ENDPOINT = "/api/v1/user/registeruser";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        System.out.println(request.getRequestURI());
        requestContext.setRequestPath(request.getRequestURI());

//        return true;

        if(request.getRequestURI().startsWith(REGISTER_USER_ENDPOINT)){
            return true;
        }

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

    @Override
    public int getOrder() {
        return -1;
    }
}