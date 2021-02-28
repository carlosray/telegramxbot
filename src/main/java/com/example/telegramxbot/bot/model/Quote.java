package com.example.telegramxbot.bot.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@EqualsAndHashCode
public class Quote {
    private String text;
    private String author;

    @Override
    public String toString() {
        return "<strong>" + text + "</strong> @" + "<em>" + author + "</em>";
    }
}
