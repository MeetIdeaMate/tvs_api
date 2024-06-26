package com.techlambdas.delearmanagementapp.config;


import org.springframework.data.domain.AuditorAware;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            return Optional.of(((UserDetailsImpl) principal).getUserId());
        }

        return Optional.empty();
    }
}
