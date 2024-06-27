package com.example.memo.memo.service;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.memo.memo.service.models.AiResponse;

@Service
public class RestTemplateService {
    RestTemplate restTemplate = new RestTemplate();

    @Value("${AI_URL}")
    private String aiUrl;

    public List<String> getTags(String id, String content) {
        return null;
    }

    public AiResponse searchMemo(String content) {
        System.out.println(content);
        URI uri = UriComponentsBuilder
            .fromUriString(aiUrl)
            .path("/user_query")
            .queryParam("query", content)
            .build(false)
            .toUri();

        System.out.println(uri);
        ResponseEntity<AiResponse> aiResponse = restTemplate.getForEntity(uri, AiResponse.class);
        if (aiResponse.getStatusCode().is5xxServerError()) {
            throw new RuntimeException("AI 서비스 서버 에러");
        } else if (aiResponse.getStatusCode().is4xxClientError() || aiResponse.getBody() == null) {
            throw new RuntimeException("AI 서비스 클라이언트 에러");
        }
        return aiResponse.getBody();
    }
}
