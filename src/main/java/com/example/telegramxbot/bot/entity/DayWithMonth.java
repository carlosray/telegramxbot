package com.example.telegramxbot.bot.entity;

import java.time.Month;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;

import lombok.Data;

@Data
@Embeddable
public class DayWithMonth {
    @Max(30)
    @Column(name = "day", nullable = false)
    private int day;

    @Column(name = "month", nullable = false)
    @Enumerated(EnumType.STRING)
    private Month month;
}
