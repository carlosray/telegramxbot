package com.example.telegramxbot.bot.service.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.telegramxbot.bot.service.DefaultAnswerService;
import com.google.common.collect.Sets;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HueBotService {
    private Map<Character, Character> vowels;
    private Set<Character> consonants;
    private final DefaultAnswerService defaultAnswerService;

    {
        putVowels();
        putConsonants();
    }

    //а, у, о, ы, и, э, я, ю, ё, е
    private void putVowels() {
        vowels = new HashMap<>();
        vowels.put('а', 'я');
        vowels.put('у', 'ю');
        vowels.put('о', 'е');
        vowels.put('ы', 'и');
        vowels.put('и', 'и');
        vowels.put('э', 'е');
        vowels.put('я', 'я');
        vowels.put('ю', 'ю');
        vowels.put('е', 'е');
        vowels.put('ё', 'ё');
    }

    //б, в, г, д, ж, з, й, к, л, м, н, п, р, с, т, ф, х, ц, ч, ш, щ
    private void putConsonants() {
        consonants = Sets.newHashSet('б', 'в', 'г', 'д', 'ж', 'з', 'й', 'к', 'л', 'м', 'н', 'п', 'р', 'с', 'т', 'ф',
                'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ь');
    }

    public String getHueString(String userText) {
        String emojiRemoved = EmojiParser.removeAllEmojis(userText);
        if (StringUtils.isNotBlank(emojiRemoved)) {
            return processString(emojiRemoved);
        } else {
            log.info("Message is a Smile");
            return defaultAnswerService.getSmileMessage();
        }
    }

    private String processString(String text) {
        String lastOfRow = getLastOfRow(text);
        String lastWordOfSentence = getLastWordOfSentence(lastOfRow);
        if (isCyrillic(lastWordOfSentence)) {
            return concatXue(lastWordOfSentence.toLowerCase());
        } else {
            log.info("Message is not Cyrillic");
            return wildMessage();
        }
    }

    private boolean isCyrillic(String word) {
        Pattern pattern = Pattern.compile("[а-яёА-ЯЁ]+");
        Matcher matcher = pattern.matcher(word);
        return matcher.matches();
    }

    private String getLastOfRow(String text) {
        String[] split = text.split("\\s");
        return split[split.length - 1];
    }

    private String getLastWordOfSentence(String sentence) {
        Pattern pattern = Pattern.compile("\\s*(\\s|,|!|\\.|\\?)\\s*");
        String[] split = pattern.split(sentence);
        return split[split.length - 1];
    }

    private String concatXue(String lastWordOfSentence) {
        StringBuilder builder = new StringBuilder();
        boolean isReplaced = false;
        for (char c : lastWordOfSentence.toCharArray()) {
            if (!isReplaced && !consonants.contains(c)) {
                builder.append(vowels.get(c));
                isReplaced = true;
            } else if (isReplaced) {
                builder.append(c);
            }
        }
        return Optional.of(builder.toString())
                .filter(s -> !s.isEmpty())
                .filter(message -> message.chars()
                        .mapToObj(c -> (char) c)
                        .anyMatch(c -> vowels.containsKey(c)))
                .map("ху"::concat)
                .orElse(wildMessage());
    }

    private String wildMessage() {
        log.info("Getting wild message");
        return defaultAnswerService.getWildMessage();
    }

}
