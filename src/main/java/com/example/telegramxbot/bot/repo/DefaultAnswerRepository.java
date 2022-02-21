package com.example.telegramxbot.bot.repo;

import java.util.Set;

import com.example.telegramxbot.bot.entity.DefaultAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultAnswerRepository extends JpaRepository<DefaultAnswer, Long> {
    Set<DefaultAnswer> findAllByType(DefaultAnswer.AnswerType type);
}
