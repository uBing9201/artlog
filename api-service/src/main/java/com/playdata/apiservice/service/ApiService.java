package com.playdata.apiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playdata.apiservice.dto.ApiResponse;
import com.playdata.apiservice.dto.ContentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {
    private final ObjectMapper objectMapper;

    public List<ContentDto> first() {
        String baseUrl = "http://api.kcisa.kr/openapi/API_CCA_145/request"; // ← 니가 호출할 API 주소

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("serviceKey", "50c4abb3-0b85-4348-809c-b1df4198f4ef")
                .queryParam("numOfRows", "5")
                .queryParam("pageNo", "1")
                .build(true) // 인코딩까지 자동으로
                .toUri();
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String json= response.getBody();

        try {
            ApiResponse parsed = objectMapper.readValue(json, ApiResponse.class);
            return parsed.getResponse().getBody().getItems().getItem();
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패: " + e.getMessage(), e);
        }
    }


}
