package com.example.telegramxbot.bot.service.feature;

import java.util.Objects;
import java.util.Set;

import com.example.telegramxbot.bot.HueBot;
import com.example.telegramxbot.bot.cache.ChatIdCache;
import com.example.telegramxbot.bot.entity.DayWithMonth;
import com.example.telegramxbot.bot.entity.UserEntity;
import com.example.telegramxbot.bot.repo.UserEntityRepository;
import com.example.telegramxbot.bot.service.integration.BalabobaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BirthdayService {
    @Autowired
    private ApplicationContext applicationContext;
    private final ChatIdCache chatIdCache;
    private final BalabobaService balabobaService;
    private final UserEntityRepository userRepository;

    private HueBot bot;

    private void initBot() {
        if (Objects.isNull(bot)) {
            this.bot = applicationContext.getBean(HueBot.class);
        }
    }

    @Scheduled(cron = "#{botSettings.birthday.cron}", zone = "#{botSettings.birthday.zone}")
    public void sendBirthday() {
        initBot();
        userRepository.findByBirthday(DayWithMonth.now())
                .stream()
                .filter(user -> user.getBirthday().isToday())
                .forEach(this::generateAndSendCongratulation);
    }

    private void generateAndSendCongratulation(UserEntity userWithBirthday) {
        log.info("Today users birthday: {}", userWithBirthday);

        final String congratulation = getCongratulation(userWithBirthday);

        final Set<Long> cachedChatIds = chatIdCache.getChatIds();
        userWithBirthday.getGroupChatIds()
                .stream()
                .map(Long::parseLong)
                .filter(cachedChatIds::contains)
                .map(Object::toString)
                .forEach(chatId -> sendCongratulation(chatId, congratulation));
    }

    private String getCongratulation(UserEntity user) {
        StringBuilder congratulation = new StringBuilder();
        if (StringUtils.isNotBlank(user.getLogin())) {
            congratulation.append(user.getLogin()).append("\n");
        }
        final String generated = balabobaService.generate("%s, с днем рождения!".formatted(user.getFirstName()));
        return congratulation.append(generated).toString();
    }

    private void sendCongratulation(String chatId, String congratulation) {
        bot.sendAnswer(chatId, null, congratulation, false);
    }
}
