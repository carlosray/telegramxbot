package com.example.telegramxbot.bot.service.utils;

import javax.annotation.Nullable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class ResourceUtils {

    public static @Nullable <T> T mapFromResource(String filename, TypeReference<T> reference) {
        try {
            ClassPathResource resource = new ClassPathResource(filename);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(resource.getInputStream(), reference);
        } catch (Exception ex) {
            log.error("Ошибка десериализации ресурса " + filename, ex);
            return null;
        }
    }
}
