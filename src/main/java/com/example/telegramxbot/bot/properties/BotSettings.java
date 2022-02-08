package com.example.telegramxbot.bot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "bot.huebot")
public class BotSettings {
    private String username;
    private String token;
    private Messages messages;
    private DaysOfWeek daysOfWeek;
    private Birthday birthday;

    @Data
    public static final class Messages {
        private Count count;
    }

    @Data
    public static final class Count {
        private int min;
        private int max;
    }

    @Data
    public static final class DaysOfWeek {
        private String cron;
        private String zone;
    }

    @Data
    public static final class Birthday {
        private String cron;
        private String zone;
    }
}
