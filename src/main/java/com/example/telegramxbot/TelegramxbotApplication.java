package com.example.telegramxbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelegramxbotApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelegramxbotApplication.class, args);
    }
}
