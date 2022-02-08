package com.example.telegramxbot.bot.entity;

import java.util.Collection;

import javax.annotation.Nullable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import com.example.telegramxbot.bot.entity.base.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NamedQueries({
        @NamedQuery(name = "UserEntity.existsBy", query = "select (count(u) > 0) from UserEntity u")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserEntity extends BaseEntity {
    @Nullable
    @Column(name = "login")
    private String login;

    @NotBlank
    @ElementCollection
    @Column(name = "group_chat_id")
    private Collection<String> groupChatIds;

    @Max(255)
    @Column(name = "first_name")
    private String firstName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "day", column = @Column(name = "birthday_day", nullable = false)),
            @AttributeOverride(name = "month", column = @Column(name = "birthday_month", nullable = false))
    })
    private DayWithMonth birthday;


}
