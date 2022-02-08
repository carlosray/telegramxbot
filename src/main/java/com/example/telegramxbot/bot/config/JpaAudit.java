package com.example.telegramxbot.bot.config;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@RequiredArgsConstructor
public class JpaAudit {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("xbot");
    }
}
