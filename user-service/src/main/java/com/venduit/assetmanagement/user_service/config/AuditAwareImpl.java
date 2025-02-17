package com.venduit.assetmanagement.user_service.config;


import com.venduit.assetmanagement.user_service.model.POJO.UserContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component("auditorAware")
@RequiredArgsConstructor
public class AuditAwareImpl implements AuditorAware<String> {

    private final RequestContext requestContext;
    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
//        if(requestContext.getRequestPath().startsWith("/api/v1/user/registeruser")){
//            return Optional.empty();
//        }
//        return Optional.of(UserContextHolder.getContext().getUserId());
        return Optional.of("LoggedIn-User");
    }
}
