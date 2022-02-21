package com.example.telegramxbot.bot.repo;

import java.util.Optional;

import com.example.telegramxbot.bot.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query(value = "select * from Quote order by random() limit 1", nativeQuery = true)
    Optional<Quote> findRandom();
}
