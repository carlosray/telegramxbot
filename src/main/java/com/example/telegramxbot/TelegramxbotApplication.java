package com.example.telegramxbot;

import com.example.telegramxbot.bot.properties.BotSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class TelegramxbotApplication implements CommandLineRunner {
    private final BotSettings settings;

    public static void main(String[] args) {
        SpringApplication.run(TelegramxbotApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void run(String... args) {
        log.debug(settings.toString());
    }
}
