package com.example.telegramxbot.bot.service.integration;

import java.net.URI;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class BalabobaService {
    private final static URI ADDRESS = URI.create("https://zeapi.yandex.net/lab/api/yalm/text3");
    private final RestTemplate restTemplate;

    public String generate(String text) {
        try {
            final ResponseEntity<Map> exchange = restTemplate.exchange(request(text), Map.class);
            return exchange.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }
    }

    private RequestEntity<QueryReq> request(String text) {
        return new RequestEntity<>(new QueryReq(text), headers(), HttpMethod.POST, ADDRESS);
    }

    private HttpHeaders headers() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }

    @Value
    static class QueryReq {
        String query;
        int intro = 0;
        int filter = 1;
    }
}
