package com.mcyldz.hrinventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<UUID> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
                return Optional.empty();
            }

            // TODO: Spring Security'de UserDetails nesnemizi özelleştirdiğimizde,
            //       bu kısım doğrudan user nesnesinden ID'yi alacak şekilde güncellenecektir.

            try {
                return Optional.of(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }
}
