package com.example.telegramxbot.bot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.example.telegramxbot.bot.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Quote extends BaseEntity {
    @Column(name = "text", nullable = false, length = 1024)
    private String text;
    @Column(name = "author", nullable = false)
    private String author;

    @Override
    public String toString() {
        return "<strong>" + text + "</strong> @" + "<em>" + author + "</em>";
    }
}
