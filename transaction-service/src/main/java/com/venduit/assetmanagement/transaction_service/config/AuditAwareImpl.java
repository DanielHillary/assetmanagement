package com.venduit.assetmanagement.transaction_service.config;


import com.venduit.assetmanagement.transaction_service.model.POJO.UserContextHolder;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
public class AuditAwareImpl implements AuditorAware<String> {
    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(UserContextHolder.getContext().getUserId());
    }
}
