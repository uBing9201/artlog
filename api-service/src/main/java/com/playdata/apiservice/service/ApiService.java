package com.playdata.apiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playdata.apiservice.dto.api.ApiResponse;
import com.playdata.apiservice.dto.api.ContentDto;
import com.playdata.apiservice.dto.api.ContentResDto;
import com.playdata.apiservice.exception.PublicApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {
    private final ObjectMapper objectMapper;

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

        return getApiData(5, 1);
    }

    public List<ContentResDto> getDataByNumOfRows(long number) throws IOException, PublicApiException {
        List<ContentDto> apiData = getApiData(number, 1);

        // 유효성 검증 후 List 형태로 반환
        return apiData.stream()
                .map(ContentDto::toResDto)
                .filter(Objects::nonNull)
                .distinct()
                .limit(number)
                .toList();
    }

    public List<ContentDto> getApiData(long numOfRows, long pageNo) throws IOException, PublicApiException {
        // 오류를 대비해 요청한 개수의 2배수의 10개 더 준비
        String numOfRowsStr = Long.toString(numOfRows + numOfRows + 10);
        String pageNoStr = Long.toString(pageNo);

        // 데이터 요청
        HttpURLConnection conn = null;
        try {
            StringBuilder urlBuilder = new StringBuilder("http://api.kcisa.kr/openapi/API_CCA_145/request"); /*URL*/
            urlBuilder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=50c4abb3-0b85-4348-809c-b1df4198f4ef"); /*서비스키*/
            urlBuilder.append("&").append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(numOfRowsStr, StandardCharsets.UTF_8)); /*세션당 요청레코드수*/
            urlBuilder.append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(pageNoStr, StandardCharsets.UTF_8)); /*페이지수*/

            URL url = new URL(urlBuilder.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
        } catch (Exception e) {
            log.error("불러오는데 실패했습니다");

            // 예외 던지기
            throw new IOException("공공 API를 불러오는 데 실패했습니다.");
        }

        // 응답 받기
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        else {
            log.error(conn.getErrorStream().toString());
            throw new PublicApiException("응답 수신에 실패하였습니다.");
        }

        // 정상 응답 Json 변환
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String json = sb.toString();

        try {
            ApiResponse parsed = objectMapper.readValue(json, ApiResponse.class);
            return parsed.getResponse().getBody().getItems().getItem();
        } catch (Exception e) {
            throw new PublicApiException("JSON 파싱 실패: " + e.getMessage(), e);
        }

    }
}
