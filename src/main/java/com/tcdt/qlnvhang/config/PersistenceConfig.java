package com.tcdt.qlnvhang.config;


import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PersistenceConfig {
  @Bean
  AuditorAware<Long> auditorProvider() {
    return new AuditorAwareImpl();
  }
}

class AuditorAwareImpl implements AuditorAware<Long> {

  @Override
  public Optional<Long> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      if (authentication.getPrincipal() instanceof String)
        return Optional.ofNullable(0l);
      return Optional.ofNullable(((CustomUserDetails) authentication.getPrincipal()).getUser().getId());
    }
    return Optional.empty();
  }

}
