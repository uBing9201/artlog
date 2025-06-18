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
import org.springframework.cache.annotation.CacheEvict;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiCacheService {
    private final OrderFeignClient orderFeignClient;
    private final ReviewFeignClient reviewFeignClient;
    private final ObjectMapper objectMapper;
    private final ApiDataService apiDataService;

    @Cacheable(value = "exhibitionListCache", key = "#numOfRows + '-' + #pageNo")
    public List<ContentResDto> getData(Long numOfRows, Long pageNo) throws IOException, PublicApiException {
        List<ContentResDto> apiData = apiDataService.getApiData((pageNo / 100) + 1);
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(apiData);
            log.info("캐시 크기 : {} bytes", bytes.length);
        } catch (Exception e) {
            log.error("크기 측정 실패 ㅠㅠ");
        }

        // 유효성 검증 후 List 형태로 반환
        return apiData.stream()
                .skip(numOfRows * (pageNo - 1))
                .limit(numOfRows)
                .toList();
    }

    @Cacheable(value = "OrderInfoListCache", key = "#userKey")
    public List<OrderInfoResDto> getOrderList(Long userKey) {
        return orderFeignClient.findByAllFeign(userKey).getBody();
    }

    @Cacheable(value = "exhibitionUserListCache", key = "#userKey")
    public List<ContentUserResDto> getDataByUserKey(Long userKey) throws IOException, PublicApiException {
        // api data 받기
        List<ContentResDto> apiData = apiDataService.getApiData(1);
        log.info(apiData.toString());

        // 탐색 성능 증가를 위해 map으로 변환
        Map<String, ContentResDto> map = apiData.stream().collect(Collectors.toMap(ContentResDto::getContentId, Function.identity()));

        // order 리스트 받기
        List<OrderInfoResDto> orderList = getOrderList(userKey);
        if (orderList == null || orderList.isEmpty()) {
            return new ArrayList<>();
        }


        // 응답 객체 만들기
        List<ContentUserResDto> resDtoList =
                orderList.stream()
                        .map(order ->
                        {
                            // map에서 일치하는 데이터 꺼내기
                            String localID = order.getContentId();
                            ContentResDto data = map.getOrDefault(localID, new ContentResDto());
                            if (data == null) return null;

                            return ContentUserResDto.builder()
                                    .id(order.getId())
                                    .contentId(order.getContentId())
                                    .userKey(order.getUserKey())
                                    .active(order.isActive())
                                    .totalPrice(order.getTotalPrice())
                                    .registDate(order.getRegistDate())
                                    .contentTitle(data.getContentTitle())
                                    .contentThumbnail(data.getContentThumbnail())
                                    .isReviewed(false)
                                    .contentVenue(data.getContentVenue())
                                    .contentUrl(data.getContentUrl())
                                    .contentCharge(data.getContentCharge())
                                    .contentPeriod(data.getContentPeriod())
                                    .startDate(data.getStartDate())
                                    .endDate(data.getEndDate())
                                    .build();
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

    public void deleteUserCache(Long userKey) {
        deleteUserDataCache(userKey);
        deleteUserOrderCache(userKey);
    }

    @CacheEvict(value = "exhibitionUserListCache", key = "#userKey", allEntries = true)
    public void deleteUserDataCache(Long userKey) {
        log.info("사용자 전시 데이터 캐시 삭제");
    }

    @CacheEvict(value = "OrderInfoListCache", key = "#userKey", allEntries = true)
    public void deleteUserOrderCache(Long userKey) {
        log.info("주문 정보 캐시 삭제");
    }
}
