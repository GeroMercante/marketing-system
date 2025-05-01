package com.davinci.geromercante.marketing.infrastructure.config.audit;

import com.davinci.geromercante.marketing.module.auth.model.entity.Credential;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Credential credential) {
                return Optional.ofNullable(credential.getUser().getId());
            }
        }
        return Optional.of(1L);
    }
}
