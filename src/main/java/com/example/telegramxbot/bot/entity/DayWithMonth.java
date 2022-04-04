package com.example.telegramxbot.bot.entity;

import java.time.LocalDate;
import java.time.Month;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DayWithMonth {
    @Max(30)
    @Column(name = "day", nullable = false)
    private int day;

    @Column(name = "month", nullable = false)
    @Enumerated(EnumType.STRING)
    private Month month;

    public boolean isToday() {
        return this.equals(now());
    }

    public static DayWithMonth now() {
        final LocalDate now = LocalDate.now();
        return new DayWithMonth(now.getDayOfMonth(), now.getMonth());
    }
}
