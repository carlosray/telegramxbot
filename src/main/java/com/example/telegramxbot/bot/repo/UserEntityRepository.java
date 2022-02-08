package com.example.telegramxbot.bot.repo;

import java.util.Set;

import com.example.telegramxbot.bot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    @Query(nativeQuery = true, value = "select distinct group_chat_id from user_entity_group_chat_ids")
    Set<String> findAllDistinctGroupChatIds();
}
