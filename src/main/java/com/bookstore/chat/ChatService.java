package com.bookstore.chat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ChatService {
    @Value("${openrouter.api.url}")
    private String chatUrl;

    @Value("${openrouter.api.key}")
    private String chatKey;

    @Value("${openrouter.api.model}")
    private String chatModel;

    private final RestTemplate restTemplate = new RestTemplate();

    public ChatResponseDto getChatResponse(String prompt) {

        List<Map<String, String>> message = List.of(
                Map.of("role", "user", "content", prompt));

        Map<String,Object> requestBody = new HashMap<>();
        requestBody.put("model", chatModel);
        requestBody.put("messages", message);

        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);
        requestHeader.set("HTTP-Referer", "https://localhost:8080");
        requestHeader.setBearerAuth(chatKey);



        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, requestHeader);
        Map resp = restTemplate.postForEntity(chatUrl,
                request, Map.class).getBody();
        assert resp != null;
        ArrayList t = ((ArrayList)resp.get("choices"));
        HashMap a = (HashMap) t.get(0);
        HashMap cont = (HashMap) a.get("message");
        assert cont != null;

        String content = (String)((HashMap<?, ?>)
                          ((HashMap<?, ?>)
                                  ((ArrayList<?>) resp
                                          .get("choices"))
                                          .get(0))
                                  .get("message"))
                          .get("content");


        return new ChatResponseDto(content);
    }

}
