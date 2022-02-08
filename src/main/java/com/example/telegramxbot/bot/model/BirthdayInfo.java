package com.example.telegramxbot.bot.model;

import javax.annotation.Nullable;

import lombok.Data;

@Data
public class BirthdayInfo {
    private String name;
    @Nullable
    private String login;
    private String day;
    private String month;
}
