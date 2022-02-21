package com.example.telegramxbot.bot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

import com.example.telegramxbot.bot.entity.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DefaultAnswer extends BaseEntity {
    @NotBlank
    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnswerType type;

    public enum AnswerType {
        SMILE,
        SORRY,
        WILD
    }
}
