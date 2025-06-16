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

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiCacheService {
    private final OrderFeignClient orderFeignClient;
    private final ReviewFeignClient reviewFeignClient;
    private final ObjectMapper objectMapper;

    public List<ContentDto> getApiData(long pageNo) throws IOException, PublicApiException {
        // 오류를 대비해 요청한 개수의 10개 더 준비
        long defaultNum = 5000 * (pageNo / 100 + 1);
        String numOfRowsStr = Long.toString(defaultNum);


        // 데이터 요청
        HttpURLConnection conn = null;
        try {
            StringBuilder urlBuilder = new StringBuilder("http://api.kcisa.kr/openapi/API_CCA_145/request"); /*URL*/
            urlBuilder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=3b0e834a-ebcb-45b5-b6fb-728277dd565c"); /*서비스키*/
            urlBuilder.append("&").append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(numOfRowsStr, StandardCharsets.UTF_8)); /*세션당 요청레코드수*/
            urlBuilder.append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("1", StandardCharsets.UTF_8)); /*페이지수*/

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

    @Cacheable(value = "exhibitionListCache",key = "#numOfRows + '-' + #pageNo")
    public List<ContentResDto> getData(Long numOfRows, Long pageNo) throws IOException, PublicApiException {
        List<ContentDto> apiData = getApiData(pageNo);

        // 유효성 검증 후 List 형태로 반환
        return apiData.stream()
                .map(ContentDto::toResDto)
                .filter(Objects::nonNull)
                .distinct()
                .skip(numOfRows * (pageNo - 1))
                .limit(numOfRows)
                .toList();
    }

    public List<ContentUserResDto> getDataByUserKey(Long userKey) throws IOException, PublicApiException {
        List<ContentDto> apiData = getApiData(1).stream().filter(ContentDto::isValid).toList();
        List<OrderInfoResDto> orderList = orderFeignClient.findByAllFeign(userKey).getBody();
        log.info(apiData.toString());

        if(orderList == null || orderList.isEmpty()) {
            return new ArrayList<>();
        }

        List<ContentUserResDto> resDtoList =
                orderList.stream()
                        .map(order ->
                        {
                            for (ContentDto data : apiData) {
                                if(data.getLocalId().equals(order.getContentId())) {
                                    long periodL = 0L;
                                    String startDateStr, endDateStr;

                                    // 기간 정보 추출을 위한 함수
                                    if (data.getPeriod() == null || data.getPeriod().isEmpty()) {
                                        log.error("period가 유효하지 않습니다. : {}", data.getPeriod());
                                        return null;
                                    } else {
                                        try {
                                            String[] dates;
                                            if (!data.getPeriod().contains("~")) {
                                                dates = data.getPeriod().split(" ");
                                            } else {
                                                dates = data.getPeriod().split("~");
                                            }
                                            // 1. 시작일과 종료일 분리
                                            startDateStr = dates[0].trim();
                                            endDateStr = dates[dates.length - 1].trim();

                                            // 2. 문자열을 LocalDate로 파싱
                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
                                            LocalDate endDate = LocalDate.parse(endDateStr, formatter);

                                            // 3. 일수 계산 (양 끝 포함하려면 +1)
                                            periodL = ChronoUnit.DAYS.between(startDate, endDate) + 1;

                                            log.info("startDate:{} endDate:{}", startDateStr, endDateStr);
                                        } catch (Exception e) {
                                            log.error("period 파싱에 실패하였습니다. : {}", data.getPeriod());
                                            return null;
                                        }
                                    }

                                    return ContentUserResDto.builder()
                                            .id(order.getId())
                                            .contentId(order.getContentId())
                                            .userKey(order.getUserKey())
                                            .active(order.isActive())
                                            .totalPrice(order.getTotalPrice())
                                            .registDate(order.getRegistDate())
                                            .contentTitle(data.getTitle())
                                            .contentThumbnail(data.getImageObject())
                                            .isReviewed(false)
                                            .contentVenue(data.getEventSite())
                                            .contentUrl(data.getUrl())
                                            .contentCharge(Long.parseLong(data.getCharge()))
                                            .contentPeriod(periodL)
                                            .startDate(startDateStr)
                                            .endDate(endDateStr)
                                            .build();
                                }
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .toList();

        log.info("*******************************************");
        log.info(resDtoList.toString());
        log.info("*******************************************");

        for (ContentUserResDto dto : resDtoList) {
            log.info(dto.toString());
            Boolean isReviewed = reviewFeignClient.findByApiFeign(dto.getContentId(), userKey).getBody();
            dto.setIsReviewed(isReviewed);
        }

        log.info(resDtoList.toString());
        return resDtoList;

    }
}
