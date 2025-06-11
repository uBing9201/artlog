package com.playdata.apiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playdata.apiservice.dto.api.ApiResponse;
import com.playdata.apiservice.dto.api.ContentDto;
import com.playdata.apiservice.dto.api.ContentResDto;
import com.playdata.apiservice.dto.api.ContentUserResDto;
import com.playdata.apiservice.dto.common.OrderInfoResDto;
import com.playdata.apiservice.exception.PublicApiException;
import com.playdata.apiservice.feign.OrderFeignClient;
import com.playdata.apiservice.feign.ReviewFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {
    private final ApiCacheService apiCacheService;

    public List<ContentDto> first() throws IOException {
//        String baseUrl = "http://api.kcisa.kr/openapi/API_CCA_145/request";
//
//        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .queryParam("serviceKey", "50c4abb3-0b85-4348-809c-b1df4198f4ef")
//                .queryParam("numOfRows", "5")
//                .queryParam("pageNo", "1")
//                .build(true)
//                .toUri();
//        RestTemplate restTemplate = new RestTemplate();
//
//        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
//        String json = response.getBody();
//
//        try {
//            ApiResponse parsed = objectMapper.readValue(json, ApiResponse.class);
//            return parsed.getResponse().getBody().getItems().getItem();
//        } catch (Exception e) {
//            throw new RuntimeException("JSON 파싱 실패: " + e.getMessage(), e);
//        }

        return apiCacheService.getApiData(1);
    }

    public List<ContentResDto> getDataByNumOfRows(long number) throws IOException, PublicApiException {
        List<ContentDto> apiData = apiCacheService.getApiData(1);

        // 유효성 검증 후 List 형태로 반환
        return apiData.stream()
                .map(ContentDto::toResDto)
                .filter(Objects::nonNull)
                .distinct()
                .limit(number)
                .toList();
    }





    public List<ContentUserResDto> getDataByUserKeyPaging(Long userKey, Long numOfRows, Long pageNo) throws IOException, PublicApiException, IllegalArgumentException {
        List<ContentUserResDto> dataByUserKey = apiCacheService.getDataByUserKey(userKey);
        if(dataByUserKey == null || dataByUserKey.isEmpty()) {
            return new ArrayList<>();
        }

        return dataByUserKey.stream()
                .skip(numOfRows * (pageNo - 1))
                .limit(numOfRows)
                .toList();
    }

}
