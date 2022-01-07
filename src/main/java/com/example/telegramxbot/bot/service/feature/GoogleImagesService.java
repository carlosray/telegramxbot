package com.example.telegramxbot.bot.service.feature;

import com.example.telegramxbot.bot.service.RandomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GoogleImagesService {
    private final Map<DayOfWeek, String> searchStrings = new HashMap<>();
    private final RandomService randomService;

    public GoogleImagesService(RandomService randomService) {
        this.randomService = randomService;
        searchStrings.put(DayOfWeek.MONDAY, "мем понедельник утро");
        searchStrings.put(DayOfWeek.TUESDAY, "мем вторник утро");
        searchStrings.put(DayOfWeek.WEDNESDAY, "мем среда утро");
        searchStrings.put(DayOfWeek.THURSDAY, "мем четверг утро");
        searchStrings.put(DayOfWeek.FRIDAY, "мем пятница утро");
    }

    public InputStream getImageInputStream(DayOfWeek dayOfWeek) throws IOException {
        final List<String> urls = getUrls(dayOfWeek);
        final AtomicInteger random = randomService.getRandomCount(0, urls.size() - 1);
        URL url = new URL(urls.get(random.get()));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return connection.getInputStream();
    }

    private List<String> getUrls(DayOfWeek dayOfWeek) throws IOException{
        // can only grab first 100 results
        String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
        String url = "https://www.google.com/search?tbm=isch&q=" + getSearchString(dayOfWeek);

        List<String> resultUrls = new ArrayList<>();

        Document doc = Jsoup.connect(url).userAgent(userAgent).referrer("https://www.google.com/").get();

        Elements elements = doc.select("img[data-src]");

        for (Element element : elements) {
            resultUrls.add(element.attr("data-src"));
        }
        new ObjectMapper();
        System.out.println("number of results: " + resultUrls.size());

        return resultUrls;
    }

    private String getSearchString(DayOfWeek dayOfWeek) {
        return searchStrings.getOrDefault(dayOfWeek, "мем вася");
    }
}
